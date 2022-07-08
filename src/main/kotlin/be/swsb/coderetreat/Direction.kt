package be.swsb.coderetreat

import be.swsb.coderetreat.Direction.*

enum class Direction {
    North,
    East,
    South,
    West,
}

data class Vector(val x: Int, val y: Int) {
    fun inverted() = Vector(-x, -y)
}

fun Direction.asVector() = when (this) {
    North -> Vector(0, 1)
    East -> Vector(1, 0)
    South -> Vector(0, -1)
    West -> Vector(-1, 0)
}

fun Direction.rotateClockwise() = when(this) {
    North -> East
    East -> South
    South -> West
    West -> North
}

fun Direction.rotateCounterClockwise() = when(this) {
    North -> West
    West -> South
    South -> East
    East -> North
}