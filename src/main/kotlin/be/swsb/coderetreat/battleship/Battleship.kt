package be.swsb.coderetreat.battleship

import be.swsb.coderetreat.battleship.common.Id

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
        .count { it.coordinate in this.coordinates } == this.ship.length

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

sealed interface Piece
object CarrierPart : Piece
object BattleshipPart : Piece
object DestroyerPart : Piece
object SubmarinePart : Piece
object PatrolBoatPart : Piece
object Hit : Piece
object Sunk : Piece


sealed interface Ship {
    val length: Int
    val part: Piece
}

object Carrier : Ship {
    override val length = 5
    override val part: Piece = CarrierPart
}

object Battleship : Ship {
    override val length = 4
    override val part: Piece = BattleshipPart
}

object Destroyer : Ship {
    override val length = 3
    override val part: Piece = DestroyerPart
}

object Submarine : Ship {
    override val length = 3
    override val part: Piece = SubmarinePart
}

object PatrolBoat : Ship {
    override val length = 2
    override val part: Piece = PatrolBoatPart
}