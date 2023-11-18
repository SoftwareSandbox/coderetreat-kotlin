package be.swsb.coderetreat.battleship.logic

import be.swsb.coderetreat.battleship.logic.Placement.Horizontally
import be.swsb.coderetreat.battleship.logic.Placement.Vertically
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class BattleshipTest {

    @Test
    fun `Battleship field without ships on it`() {
        val playerField = """
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()

        val actual = Game.startNewGame()
            .render()

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Placing a Carrier horizontally in the top left corner`() {
        val playerField = """
            🛳🛳🛳🛳🛳🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val actual = Game.startNewGame()
            .place(ship = Carrier, bowCoordinate = at(1, 1), placement = Horizontally)
            .render()

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Placing a Carrier vertically in the top left corner`() {
        val playerField = """
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val actual = Game.startNewGame()
            .place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically)
            .render()

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Firing and hitting a Carrier in the top left corner`() {
        val playerField = """
            💥🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val actual = Game.startNewGame()
            .place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically)
            .fire(at = at(1, 1))
            .render()

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Firing and missing a Carrier in the top right corner`() {
        val playerField = """
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val actual = Game.startNewGame()
            .place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically)
            .fire(at(10, 1))
            .render()

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Firing and sinking a Carrier in the top left corner`() {
        val playerField = """
            🏊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🏊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🏊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🏊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🏊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val actual = Game.startNewGame()
            .place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically)
            .fire(at = at(1, 1))
            .fire(at = at(1, 2))
            .fire(at = at(1, 3))
            .fire(at = at(1, 4))
            .fire(at = at(1, 5))
            .render()

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Firing at the same spot of a Carrier 5 times does not sink the Carrier`() {
        val playerField = """
            💥🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val actual = Game.startNewGame()
            .place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically)
            .fire(at = at(1, 1))
            .fire(at = at(1, 1))
            .fire(at = at(1, 1))
            .fire(at = at(1, 1))
            .fire(at = at(1, 1))
            .render()

        assertThat(actual).isEqualTo(playerField)
    }
}