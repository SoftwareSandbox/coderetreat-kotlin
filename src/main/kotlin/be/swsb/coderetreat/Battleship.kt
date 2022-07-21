package be.swsb.coderetreat

import be.swsb.coderetreat.Player.*

interface InitializablePlayingField {
    val isComplete: Boolean
    fun place(ship: Ship)
}

interface ActionablePlayingField {
    fun fire(target: Position): FiringResult
}

enum class Player {
    Player1, Player2
}

val Player.opponent: Player
    get() = if (this == Player1) Player2 else Player1

internal data class PlayerState(val name: String, val playingField: PlayingField = PlayingField()) {
    val setupComplete
        get() = playingField.isComplete
    private var winner = false
    fun markAsWon() {
        winner = true
    }
    val hasWon
        get() = winner
}

class BattleshipGameSetup(player1Name: String, player2Name: String) {
    private val players = mapOf(Player1 to PlayerState(player1Name), Player2 to PlayerState(player2Name))

    fun asPlayer(player: Player, placement: InitializablePlayingField.() -> Unit): BattleshipGameSetup {
        val playerState = players.getValue(player)
        require(!playerState.setupComplete) { "Setup has already been completed for $player" }
        playerState.playingField.placement()
        return this
    }

    internal fun startGame() = BattleshipGame(players)
}

fun gameOfBattleship(player1Name: String, player2Name: String) = BattleshipGameSetup(player1Name, player2Name)

internal class BattleshipGame(private val players: Map<Player, PlayerState>) {
    val winner: Pair<Player, String>?
        get() = players.firstNotNullOfOrNull { (player, state) ->
            if (state.hasWon) {
                player to state.name
            } else {
                null
            }
        }

    fun <T> asPlayer(player: Player, action: ActionablePlayingField.() -> T): T {
        val opponentsPlayingField = players.getValue(player.opponent).playingField
        val result = opponentsPlayingField.action()
        if (opponentsPlayingField.allShipsSunk) {
            players.getValue(player).markAsWon()
        }
        return result
    }
}