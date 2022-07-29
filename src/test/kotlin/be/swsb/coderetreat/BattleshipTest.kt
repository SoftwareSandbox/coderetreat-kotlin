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
            assertThat(PlayingField(setOf(carrier)).visualize()).isEqualTo(expected)
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
            assertThat(PlayingField(setOf(carrier)).visualize()).isEqualTo(expected)
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
            assertThat(PlayingField(setOf(carrier, battleship, destroyer, submarine, patrolBoat)).visualize()).isEqualTo(expected)
        }

        @Test
        internal fun `a playing field with a ship that was hit`() {
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

            val carrier = Carrier(At(0, 0), Direction.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Direction.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Direction.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Direction.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Direction.Horizontal) //🚤
            val playingField = PlayingField(setOf(carrier, battleship, destroyer, submarine, patrolBoat))
            playingField.fire("A3")
            assertThat(playingField.visualize()).isEqualTo(expected)
        }

        @Test
        internal fun `a playing field with all ships sunk`() {
            val expected = """
                💥🟦🟦🟦🟦🟦🟦🟦🟦🟦
                💥🟦🟦🟦💥💥🟦🟦🟦🟦
                💥🟦🟦🟦🟦🟦🟦🟦🟦🟦
                💥🟦🟦🟦🟦🟦🟦🟦🟦🟦
                💥🟦🟦🟦🟦🟦🟦🟦🟦🟦
                🟦🟦🟦🟦🟦🟦🟦💥💥💥
                🟦🟦💥🟦🟦🟦🟦💥🟦🟦
                🟦🟦💥🟦🟦🟦🟦💥🟦🟦
                🟦🟦💥🟦🟦🟦🟦💥🟦🟦
                🟦🟦💥🟦🟦🟦🟦🟦🟦🟦
            """.trimIndent()

            val carrier = Carrier(At(0, 0), Direction.Vertical) // 🛬
            val battleship = Battleship(At(2, 6), Direction.Vertical) // 🛳
            val destroyer = Destroyer(At(7, 6), Direction.Vertical) // ⛴
            val submarine = Submarine(At(7, 5), Direction.Horizontal) // 🫧
            val patrolBoat = PatrolBoat(At(4, 1), Direction.Horizontal) //🚤
            val playingField = PlayingField(setOf(carrier, battleship, destroyer, submarine, patrolBoat))
            with(playingField){
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
    return (0..9).joinToString("\n") { row ->
        (0..9).joinToString("") { column ->
            val shipAt = shipAt(column, row)
            val square = shipAt?.visualize(column, row) ?: Square.Empty
            square.icon
        }
    }
}

fun Ship.visualize(x: Int, y: Int)  = if (isDamaged(At(x,y))) Square.Damage else Square.ShipSquare(icon)

sealed class Square(val icon: String) {
    object Empty: Square("🟦")
    object Damage: Square("💥")
    class ShipSquare(icon: String) : Square(icon)
}

class PlayingField(private val ships: Set<Ship> = emptySet()) {
    private val positions : Map<At, Ship>
     get() = ships.flatMap { ship -> ship.positions.associateWith { ship }.toList() }.toMap()
    fun shipAt(x: Int, y: Int): Ship? = shipAt(At(x,y))
    private fun shipAt(at:At): Ship? = positions[at]
    fun fire(target: String) {
        val at = At(target)
        shipAt(at)?.damage(at)
    }

}
sealed class Ship(val icon: String, private val size: Int, private val at: At, private val direction: Direction) {
    private val damages = mutableListOf<At>()

    fun damage(at: At) {
        damages += at
    }

    fun isDamaged(at: At): Boolean = at in damages

    val positions
        get() = when(direction) {
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
    companion object {
        operator fun invoke(string: String) : At {
            val col = string.first()
            val row = string.drop(1)
            val x = ('A'..'J').zip(0..9).toMap()[col] ?: error("Column $col out of bounds")
            return At(x, row.toInt()-1)
        }
    }
}
enum class Direction {
    Horizontal,
    Vertical
}