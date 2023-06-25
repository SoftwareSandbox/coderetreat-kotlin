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

    private val player1 get() = eventStream.filter { it.gameId == id }.filterIsInstance<GameStarted>().first().player1

    private val pieces: Map<Coordinate, Piece>
        get() {
            return eventStream.filter { it.gameId == id }
                .flatMap { battleShipEvent ->
                    when (battleShipEvent) {
                        is ShipPlaced -> {
                            battleShipEvent.shipCoordinates.map { it to battleShipEvent.ship.part }
                        }
                        is FireWasAHit -> listOf(battleShipEvent.coordinate to Hit)
                        is FireWasAMiss -> emptyList()
                        else -> emptyList()
                    }
                }.toMap()
        }

    fun piece(at: Coordinate): Piece? = pieces[at]

    fun place(player: Player = "Player 1", ship: Ship, bowCoordinate: Coordinate, placement: Placement): Game {
        val shipCoords: Set<Coordinate> = when (placement) {
            Placement.Horizontally -> bowCoordinate.rangeRight(ship.length)
            Placement.Vertically -> bowCoordinate.rangeDown(ship.length)
        }
        return broadcast(ShipPlaced(id, player, ship, shipCoords))
    }

    fun fire(at: Coordinate): Game =
        if (piece(at) != null) broadcast(FireWasAHit(id, player1, at))
        else broadcast(FireWasAMiss(id, player1, at))

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
object Hit : Piece
sealed interface Ship {
    val length: Int
    val part: Piece
}

object Carrier : Ship {
    override val length = 5
    override val part: Piece = CarrierPart
}