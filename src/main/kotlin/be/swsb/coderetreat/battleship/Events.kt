package be.swsb.coderetreat.battleship

import be.swsb.coderetreat.battleship.common.Id
import java.time.LocalDateTime

typealias EventId = Id<BattleShipEvent>

sealed class BattleShipEvent(
    private val id: EventId = EventId(),
    private val at: LocalDateTime = LocalDateTime.now(),
    val gameId: GameId,
)

data class GameStarted(private val _gameId: GameId, val player1: Player, val player2: Player) :
    BattleShipEvent(gameId = _gameId)

data class ShipPlaced(private val _gameId: GameId, val player: Player, val ship: Ship, val shipCoordinates: Set<Coordinate>) :
    BattleShipEvent(gameId = _gameId)
data class AllShipsWerePlaced(private val _gameId: GameId, val player: Player) : BattleShipEvent(gameId = _gameId)

data class FireWasAHit(private val _gameId: GameId, val target: Player, val coordinate: Coordinate) : BattleShipEvent(gameId = _gameId)
data class FireWasAMiss(private val _gameId: GameId, val target: Player, val coordinate: Coordinate) : BattleShipEvent(gameId = _gameId)
data class ShipWasSunk(private val _gameId: GameId, val player: Player, val ship: Ship, val shipCoordinates: Set<Coordinate>) :
    BattleShipEvent(gameId = _gameId)
data class AllShipsHaveSunk(private val _gameId: GameId, val player: Player) :
    BattleShipEvent(gameId = _gameId)

data class PlayerHasWon(private val _gameId: GameId, val player: Player) :
    BattleShipEvent(gameId = _gameId)

class EventStream(private val _events: MutableList<BattleShipEvent> = mutableListOf()) : List<BattleShipEvent> by _events {
    fun push(event: BattleShipEvent) = event.also { _events += event }
}