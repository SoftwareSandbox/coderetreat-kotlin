package be.swsb.coderetreat.battleship.logic

import be.swsb.coderetreat.battleship.common.Id
import be.swsb.coderetreat.battleship.logic.Piece.*

class Game
private constructor(
    private val id: GameId,
    private val eventStream: EventStream,
) {
    companion object {
        fun startNewGame(
            eventStream: EventStream = EventStream(),
            player1: Player = "Player 1",
            player2: Player = "Player 2"
        ): Game {
            val gameId = GameId()
            return Game(gameId, eventStream)
                .broadcast(GameStarted(gameId, player1, player2))
        }
    }

    private val gameEvents get() = eventStream.filter { it.gameId == id }
    private val player1 get() = gameEvents.filterIsInstance<GameStarted>().first().player1
    val shipsPlaced get() = gameEvents.filterIsInstance<ShipPlaced>()

    private val pieces: Map<Coordinate, Piece>
        get() {
            return gameEvents
                .flatMap { battleShipEvent ->
                    when (battleShipEvent) {
                        is ShipPlaced -> battleShipEvent.shipCoordinates.map { it to battleShipEvent.ship.part }

                        is FireWasAHit -> listOf(battleShipEvent.coordinate to Hit)
                        is FireWasAMiss -> emptyList()
                        is ShipWasSunk -> battleShipEvent.shipCoordinates.map { it to Sunk }

                        else -> emptyList()
                    }
                }.toMap()
        }

    private fun placedShip(at: Coordinate, target: Player): PlacedShip? {
        return gameEvents.filterIsInstance<ShipPlaced>()
            .filter { it.player == target }
            .groupBy { it.ship }
            .mapValues { (_, v) -> v.flatMap { it.shipCoordinates } }
            .map { (k, v) -> PlacedShip(target, k, v.toSet()) }
            .firstOrNull { at in it.coordinates }
    }

    private data class PlacedShip(val player: Player, val ship: Ship, val coordinates: Set<Coordinate>)
    private fun PlacedShip.wasSunk(): Boolean = gameEvents.filterIsInstance<FireWasAHit>()
        .filter { it.target == this.player }
        .map { it.coordinate }.toSet() == this.coordinates

    fun place(player: Player = "Player 1", ship: Ship, bowCoordinate: Coordinate, placement: Placement): Game {
        val shipCoords: Set<Coordinate> = when (placement) {
            Placement.Horizontally -> bowCoordinate.rangeRight(ship.length)
            Placement.Vertically -> bowCoordinate.rangeDown(ship.length)
        }
        return broadcast(ShipPlaced(id, player, ship, shipCoords))
    }

    fun piece(at: Coordinate): Piece? = pieces[at]

    fun fire(at: Coordinate): Game =
        if (piece(at) != null) hitOrSink(player1, at)
        else broadcast(FireWasAMiss(id, player1, at))

    private fun hitOrSink(target: Player, at: Coordinate): Game {
        broadcast(FireWasAHit(id, this.player1, at))
        val placedShip = placedShip(at, target)
        return if (placedShip != null && placedShip.wasSunk()) broadcast(ShipWasSunk(id, target, placedShip.ship, placedShip.coordinates))
        else this
    }

    private fun broadcast(event: BattleShipEvent): Game {
        eventStream.push(event)
        return this
    }
}

typealias GameId = Id<Game>
typealias Player = String

enum class Placement {
    Horizontally,
    Vertically,
}

enum class Piece {
    CarrierPart,
    BattleshipPart,
    DestroyerPart,
    SubmarinePart,
    PatrolBoatPart,
    Hit,
    Sunk,
}



sealed class Ship (
    val length: Int,
    val part: Piece,
)

data object Carrier : Ship (length = 5, part = CarrierPart)
data object Battleship : Ship (length = 4, part = BattleshipPart)
data object Destroyer : Ship (length = 3, part = DestroyerPart)
data object Submarine : Ship (length = 3, part = SubmarinePart)
data object PatrolBoat : Ship (length = 2, part = PatrolBoatPart)