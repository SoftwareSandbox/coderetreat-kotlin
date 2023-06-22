package be.swsb.coderetreat

import be.swsb.coderetreat.battleship.*
import be.swsb.coderetreat.battleship.Placement.Horizontally
import be.swsb.coderetreat.battleship.Placement.Vertically
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

        val actual = render(Game.startNewGame())

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Placing a Carrier horizontally in the top left corner`() {
        val playerField = """
            🛳️🛳️🛳️🛳️🛳️🌊🌊🌊🌊🌊
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
        val game = Game.startNewGame().place(ship = Carrier, bowCoordinate = at(1, 1), placement = Horizontally)
        val actual = render(game)

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Placing a Carrier vertically in the top left corner`() {
        val playerField = """
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val game = Game.startNewGame().place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically)
        val actual = render(game)

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Firing and hitting a Carrier in the top left corner`() {
        val playerField = """
            💥️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val game = Game.startNewGame().place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically).fire(at = at(
            1,
            1
        ))
        val actual = render(game)

        assertThat(actual).isEqualTo(playerField)
    }

    @Test
    fun `Firing and missing a Carrier in the top right corner`() {
        val playerField = """
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🛳️🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
            🌊🌊🌊🌊🌊🌊🌊🌊🌊🌊
        """.trimIndent()
        val game = Game.startNewGame().place(ship = Carrier, bowCoordinate = at(1, 1), placement = Vertically).fire(at = at(
            10,
            1
        ))
        val actual = render(game)

        assertThat(actual).isEqualTo(playerField)
    }
}
