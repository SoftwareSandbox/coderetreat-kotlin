package be.swsb.coderetreat.battleship

data class Coordinate(private val x: Int, private val y: Int) {
    fun rangeRight(length: Int): Set<Coordinate> =
        (x..x + (length - 1)).map { Coordinate(it, y) }.toSet()

    fun rangeDown(length: Int): Set<Coordinate> =
        (y..y + (length - 1)).map { Coordinate(x, it) }.toSet()

    override fun toString() = "(x=$x,y=$y)"
}

fun at(x: Int, y: Int) = Coordinate(x, y)