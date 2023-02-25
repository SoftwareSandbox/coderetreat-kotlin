package be.swsb.coderetreat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class BattleshipTest {


    @Nested
    inner class Visualisations {
        @Test
        fun `an empty playing field`() {
            val expected = """
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            assertThat(PlayingField().visualize()).isEqualTo(expected)
        }

        @Test
        fun `a playing field with a horizontal carrier in the top left`() {
            val expected = """
                🛬🛬🛬🛬🛬🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Dir.Horizontal)
            assertThat(PlayingField(setOf(carrier)).visualize()).isEqualTo(expected)
        }

        @Test
        fun `a playing field with a vertical carrier in the top left`() {
            val expected = """
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Dir.Vertical)
            assertThat(PlayingField(setOf(carrier)).visualize()).isEqualTo(expected)
        }

        @Test
        fun `a playing field with a combination of all the ships`() {
            val expected = """
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🚤🚤🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🫧🫧🫧
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Dir.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Dir.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Dir.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Dir.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Dir.Horizontal) //🚤
            assertThat(
                PlayingField(
                    setOf(
                        carrier,
                        battleship,
                        destroyer,
                        submarine,
                        patrolBoat
                    )
                ).visualize()
            ).isEqualTo(expected)
        }

        @Test
        fun `a playing field with a ship that was hit`() {
            val expected = """
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🚤🚤🟦🟦🟦🟦
                💥🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🫧🫧🫧
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Dir.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Dir.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Dir.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Dir.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Dir.Horizontal) //🚤
            val playingField = PlayingField(setOf(carrier, battleship, destroyer, submarine, patrolBoat))
            playingField.fire("A3")
            assertThat(playingField.visualize()).isEqualTo(expected)
        }

        @Test
        fun `a playing field with a ship that was sunk`() {
            val expected = """
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🏊‍🏊‍🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🫧🫧🫧
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Dir.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Dir.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Dir.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Dir.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Dir.Horizontal) //🚤
            val playingField = PlayingField(setOf(carrier, battleship, destroyer, submarine, patrolBoat))
            with (playingField) {
                fire("E2")
                fire("F2")
            }
            assertThat(playingField.visualize()).isEqualTo(expected)
        }

        @Test
        fun `a playing field with a ship that was fired at twice on the same position has no extra effect`() {
            val expected = """
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦💥🚤🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🛬🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🫧🫧🫧
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦⛴🟦🟦
                🟦🟦🛳🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Dir.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Dir.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Dir.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Dir.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Dir.Horizontal) //🚤
            val playingField = PlayingField(setOf(carrier, battleship, destroyer, submarine, patrolBoat))
            with (playingField) {
                fire("E2")
                fire("E2")
            }
            assertThat(playingField.visualize()).isEqualTo(expected)
        }

        @Test
        fun `a playing field with all ships sunk`() {
            val expected = """
                🏊‍🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🏊‍🟦🟦🟦🏊‍🏊‍🟦🟦🟦🟦
                🏊‍🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🏊‍🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🏊‍🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦🏊‍🏊‍🏊‍
                🟦🟦🏊‍🟦🟦🟦🟦🏊‍🟦🟦
                🟦🟦🏊‍🟦🟦🟦🟦🏊‍🟦🟦
                🟦🟦🏊‍🟦🟦🟦🟦🏊‍🟦🟦
                🟦🟦🏊‍🟦🟦🟦🟦🟦🟦🟦
                == YOU LOSE 😭 ==
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Dir.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Dir.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Dir.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Dir.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Dir.Horizontal) //🚤
            val playingField = PlayingField(setOf(carrier, battleship, destroyer, submarine, patrolBoat))
            with(playingField) {
                fire("A1")
                fire("A2")
                fire("A3")
                fire("A4")
                fire("A5")

                fire("E2")
                fire("F2")

                fire("H6")
                fire("I6")
                fire("J6")

                fire("H7")
                fire("H8")
                fire("H9")

                fire("C7")
                fire("C8")
                fire("C9")
                fire("C10")
            }
            assertThat(playingField.visualize()).isEqualTo(expected)
        }
    }
}

fun PlayingField.visualize(): String {
    val visualizedPlayingField = (0..9).joinToString("\n") { row ->
        (0..9).joinToString("") { column ->
            val shipAt = shipAt(column, row)
            val square = shipAt?.visualize(column, row) ?: Square.Empty
            square.icon
        }
    }
    return if (allShipsSunk) visualizedPlayingField + "\n== YOU LOSE \uD83D\uDE2D =="
    else visualizedPlayingField
}

fun Ship.visualize(x: Int, y: Int) = when {
    isSunk() -> Square.Sunk
    isDamaged(At(x, y)) -> Square.Damage
    else -> Square.ShipSquare(i)
}

sealed class Square(val icon: String) {
    object Empty : Square("🟦")
    object Damage : Square("💥")
    object Sunk : Square("🏊‍")
    class ShipSquare(icon: String) : Square(icon)
}