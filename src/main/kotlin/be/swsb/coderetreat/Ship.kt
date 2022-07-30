package be.swsb.coderetreat

sealed class Ship(val icon: String, private val size: Int, private val at: At, private val direction: Direction) {
    private val damages = mutableSetOf<At>()

    fun damage(at: At) {
        damages += at
    }

    fun isDamaged(at: At) = at in damages
    fun isSunk() = damages.size == size

    val positions
        get() = when (direction) {
            Direction.Horizontal -> at..At(at.x + (size - 1), at.y)
            Direction.Vertical -> at..At(at.x, at.y + (size - 1))
        }
}

class Carrier(at: At, direction: Direction) : Ship("🛬", 5, at, direction)
class Battleship(at: At, direction: Direction) : Ship("🛳", 4, at, direction)
class Destroyer(at: At, direction: Direction) : Ship("⛴", 3, at, direction)
class Submarine(at: At, direction: Direction) : Ship("🫧", 3, at, direction)
class PatrolBoat(at: At, direction: Direction) : Ship("🚤", 2, at, direction)

enum class Direction {
    Horizontal,
    Vertical
}