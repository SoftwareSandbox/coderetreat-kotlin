package be.swsb.coderetreat

class PlayingField(private val ships: Set<Ship> = emptySet()) {
    val allShipsSunk
        get() = !ships.isEmpty() && ships.all(Ship::isSunk)

    private val positions: Map<At, Ship>
        get() = ships.flatMap { ship -> ship.positions.associateWith { ship }.toList() }.toMap()

    fun shipAt(x: Int, y: Int): Ship? = shipAt(At(x, y))
    private fun shipAt(at: At): Ship? = positions[at]
    fun fire(target: String) {
        val at = At(target)
        shipAt(at)?.damage(at)
    }
}