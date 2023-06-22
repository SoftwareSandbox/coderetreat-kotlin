package be.swsb.coderetreat.battleship

import be.swsb.coderetreat.battleship.common.Id

class Game
private constructor(
    private val id: GameId,
    private val eventStream: EventStream,
    private val player1: Player,
    private val player2: Player,
    private val pieces: Map<Coordinate, Piece>,
) {
    companion object {
        fun startNewGame(eventStream: EventStream? = null, player1: Player? = null, player2: Player? = null): Game {
            val gameId = GameId()
            val _eventStream = eventStream ?: EventStream()
            val _player1 = player1 ?: "Player 1"
            val _player2 = player2 ?: "Player 2"
            return Game(gameId, _eventStream, _player1, _player2, emptyMap())
                .also { it.broadcast(GameStarted(gameId, _player1, _player2)) }
        }
    }

    private fun updatePieces(pieces: Map<Coordinate, Piece>) = Game(id, eventStream, player1, player2, pieces)

    fun piece(at: Coordinate): Piece? = pieces[at]

    fun place(player: Player = "Player 1", ship: Ship, bowCoordinate: Coordinate, placement: Placement): Game {
        val shipCoords: Set<Coordinate> = when (placement) {
            Placement.Horizontally -> bowCoordinate.rangeRight(ship.length)
            Placement.Vertically -> bowCoordinate.rangeDown(ship.length)
        }.also { coordinates -> broadcast(ShipPlaced(id, player, ship, coordinates)) }
        return updatePieces(pieces + shipCoords.associateWith { ship.part })
    }


    fun fire(player: Player = "Player 1", at: Coordinate): Game =
        if (pieces[at] != null) updatePieces(pieces + (at to Hit).also { broadcast(FireWasAHit(id, player, at)) })
        else this.also { broadcast(FireWasAMiss(id, player, at)) }

    private fun broadcast(event: BattleShipEvent) = eventStream.push(event)
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