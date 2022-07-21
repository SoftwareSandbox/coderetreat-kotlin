package be.swsb.coderetreat

internal class PlayingField(private val ships: MutableList<Ship> = mutableListOf()) : InitializablePlayingField, ActionablePlayingField {
    val allShipsSunk: Boolean
        get() = ships.all { it.isSunk }
    override val isComplete
        get() = ships.map{it.type}.containsAll(AllShipClasses)

    override fun place(ship: Ship) {
        require(ship.type !in ships.map { it.type }) { "There can only be one of each class of ship on the playing field at the same time" }
        ships.firstOrNull { it.overlapsWith(ship) }?.let { throw IllegalArgumentException("Trying to place a $ship, but it would overlap with a $it") }
        ships.add(ship)
    }

    override fun fire(target: Position): FiringResult =
        ships.firstOrNull { target in it.positions }
            ?.receiveFire(target)
            ?: FiringResult.Ocean
}
fun startingPlayingField() : InitializablePlayingField = PlayingField()