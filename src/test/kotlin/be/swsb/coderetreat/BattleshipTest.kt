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

            assertThat(visualize(PlayingField())).isEqualTo(expected)
        }

        @Test
        internal fun `a playing field with a horizontal carrier in the top left`() {
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

            val carrier = Carrier(At(0, 0), Direction.Horizontal)
            assertThat(visualize(PlayingField(setOf(carrier)))).isEqualTo(expected)
        }

        @Test
        internal fun `a playing field with a vertical carrier in the top left`() {
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

            val carrier = Carrier(At(0, 0), Direction.Vertical)
            assertThat(visualize(PlayingField(setOf(carrier)))).isEqualTo(expected)
        }

        @Test
        internal fun `a playing field with a combination of all the ships`() {
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

            val carrier = Carrier(At(0, 0), Direction.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Direction.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Direction.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Direction.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Direction.Horizontal) //🚤
            assertThat(visualize(PlayingField(setOf(carrier,battleship,destroyer,submarine,patrolBoat)))).isEqualTo(expected)
        }
    }
}

fun visualize(playingField: PlayingField) : String {
    return (0..9).joinToString("\n") { row ->
        (0..9).joinToString("") { column ->
            val square = playingField.at(column,row) ?: Square.Empty
            square.icon
        }
    }
}

sealed class Square(val icon: String) {
    object Empty: Square("🟦")
}

class PlayingField(private val ships: Set<Ship> = emptySet()) {
    private val positions : Map<At, Ship> = ships.flatMap { ship -> ship.positions.associateWith { ship }.toList() }.toMap()
    fun at(x: Int, y: Int): Ship? = positions[At(x,y)]

}
sealed class Ship(icon: String, size: Int,val at: At, val direction: Direction) : Square(icon) {
    val positions = when(direction) {
        Direction.Horizontal -> at..At(at.x+(size-1), at.y)
        Direction.Vertical -> at..At(at.x, at.y+(size-1))
    }
}
class Carrier(at: At, direction: Direction) : Ship("🛬", 5, at, direction)
class Battleship(at: At, direction: Direction) : Ship("🛳", 4, at, direction)
class Destroyer(at: At, direction: Direction) : Ship("⛴", 3, at, direction)
class Submarine(at: At, direction: Direction) : Ship("🫧", 3, at, direction)
class PatrolBoat(at: At, direction: Direction) : Ship("🚤", 2, at, direction)

data class At(val x: Int, val y: Int){
    operator fun rangeTo(other: At): List<At> {
        return when {
            this.y == other.y -> (this.x..other.x).map { At(it,this.y) }
            this.x == other.x -> (this.y..other.y).map { At(this.x,it) }
            else -> emptyList()
        }
    }

}
enum class Direction {
    Horizontal,
    Vertical
}