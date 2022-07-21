package be.swsb.coderetreat

import be.swsb.coderetreat.Direction.*
import be.swsb.coderetreat.Player.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class BattleshipTest {
    @Test
    internal fun `a player wins a game of Battleship when they've sunk all of the opponents' ships`() {
        val setup = gameOfBattleship("Tim", "Megan")
        //setup
        val game = setup.asPlayer(Player1) {
            place(PatrolBoat(at(0, 0), Horizontal))
            place(Submarine(at(0, 1), Horizontal))
            place(Destroyer(at(0, 2), Horizontal))
            place(Battleship(at(0, 3), Horizontal))
            place(Carrier(at(0, 4), Horizontal))
        }.asPlayer(Player2) {
            place(PatrolBoat(at(0, 0), Horizontal))
            place(Submarine(at(0, 1), Horizontal))
            place(Destroyer(at(0, 2), Horizontal))
            place(Battleship(at(0, 3), Horizontal))
            place(Carrier(at(0, 4), Horizontal))
        }.startGame()
        game.asPlayer(Player1) {
            listOf(
                at(0, 0), at(1, 0), // patrolboat
                at(0, 1), at(1, 1), at(2, 1), // submarine
                at(0, 2), at(1, 2), at(2, 2), // destroyer
                at(0, 3), at(1, 3), at(2, 3), at(3, 3), // battleship
                at(0, 4), at(1, 4), at(2, 4), at(3, 4), at(4, 4), // cruiser
            ).forEach {
                fire(it)
            }
        }
        assertThat(game.winner).isEqualTo(Player1 to "Tim")
    }
}
