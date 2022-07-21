package be.swsb.coderetreat

data class Position(private val x: Int, private val y: Int) {
    init {
        check(x in 0..9 && y in 0..9) { "Position ($x,$y) would be out of bounds." }
    }

    operator fun plus(other: Position) = copy(x = x + other.x, y = y + other.y)
    operator fun rangeTo(to: Position): List<Position> {
        return if (this.x == to.x) {
            (this.y..to.y).map { y -> Position(this.x, y) }
        } else if (this.y == to.y) {
            (this.x..to.x).map { x -> Position(x, this.y) }
        } else {
            throw IllegalArgumentException("Cannot rangeTo diagonally")
        }
    }

    override fun toString() = "($x,$y)"
}
fun at(x: Int, y: Int) = Position(x, y)