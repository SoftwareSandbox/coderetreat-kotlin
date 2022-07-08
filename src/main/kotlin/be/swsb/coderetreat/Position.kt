package be.swsb.coderetreat

data class Position(private val x: Int, private val y: Int) {
    infix operator fun plus(vector: Vector): Position = Position(x = x + vector.x, y = y + vector.y)
    override fun toString() = "($x,$y)"
}

fun at(x: Int, y: Int) = Position(x, y)