package be.swsb.coderetreat

import be.swsb.coderetreat.Direction.*

enum class Direction {
    Horizontal,
    Vertical,
}

enum class FiringResult {
    Hit,
    Ocean,
    Sunk,
}

enum class ShipClass(val size: Int) {
    PatrolBoat(2),
    Submarine(3),
    Destroyer(3),
    Battleship(4),
    Carrier(5);

    override fun toString() = this.name
}
val AllShipClasses = ShipClass.values().toSet()


class DamageTracker(private val maxSustainableDamage: Int, private val damage : MutableSet<Position> = mutableSetOf()) {
    val isFull: Boolean
        get() = damage.size == maxSustainableDamage
    operator fun plusAssign(hit: Position) {
        damage.add(hit)
    }
}
fun noDamage(maxSustainableDamage: Int) = DamageTracker(maxSustainableDamage)

data class Ship(
    val type: ShipClass,
    private val from: Position,
    private val direction: Direction
) {
    private val to = when (direction) {
        Horizontal -> from + at(type.size - 1, 0)
        Vertical -> from + at(0, type.size - 1)
    }
    private val damage = noDamage(type.size)

    val isSunk: Boolean
        get() = damage.isFull
    val positions: List<Position> = from..to

    internal fun receiveFire(target: Position): FiringResult {
        println("$type at $positions receiving fire at $target")
        println("current damage: $damage")
        if (target !in positions) return FiringResult.Ocean
        damage += target
        return if (isSunk) FiringResult.Sunk
        else FiringResult.Hit
    }

    internal fun overlapsWith(other: Ship) : Boolean = positions.any { it in other.positions }

    override fun toString() = "$type at $positions"
}

fun PatrolBoat(from: Position, direction: Direction) = Ship(ShipClass.PatrolBoat, from, direction)
fun Submarine(from: Position, direction: Direction) = Ship(ShipClass.Submarine, from, direction)
fun Destroyer(from: Position, direction: Direction) = Ship(ShipClass.Destroyer, from, direction)
fun Battleship(from: Position, direction: Direction) = Ship(ShipClass.Battleship, from, direction)
fun Carrier(from: Position, direction: Direction) = Ship(ShipClass.Carrier, from, direction)