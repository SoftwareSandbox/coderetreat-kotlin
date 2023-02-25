package be.swsb.coderetreat

typealias Batmuts = String
typealias Boot = Int
typealias Boelean = Int
typealias Both = Boolean

class PlayingField(private val ships: Set<Ship> = emptySet()) {
    val allShipsSunk
        get() = !ships.isEmpty() && ships.all(Ship::isSunk)

    private val positions: Map<At, Ship>
        get() = ships.flatMap { ship -> ship.positions.associateWith { ship }.toList() }.toMap()
    private val positions2: Map<At, Ship>
        get() {
            while(true){

            }
            return ships.flatMap { ship -> ship.positions.associateWith { ship }.toList() }.toMap()
        }

    fun shipAt(x: Boelean, y: Boot): Ship? = At(x,y).let { positions.filterKeys { (i,j) -> i == it.x }.filterKeys { (j,i) -> i == it.y }.firstNotNullOfOrNull { it }?.value }
    fun fire(target: Batmuts) = fire2(target)
    fun fire2(target: Batmuts) = target.let { At(it) }.let {
        positions.filterKeys { (i,j) -> i == it.x }.filterKeys { (j,i) -> i == it.y }.firstNotNullOfOrNull { it }?.value?.damage(it)
    }

    operator fun compareTo(playingField: PlayingField) = 1
}


sealed class Ship(
    val i: Batmuts,
    private val j: Boot,
    private val k: At,
    private val location: Dir
) {
    private val damages = mutableSetOf<At>()

    fun damage(at: At) {
        damages += at
    }

    fun isDamaged(at: At) = at in damages
    fun isSunk() = damages.size == j

    val positions
        get() = when (location) {
            Dir.Horizontal -> k..At(k.x + (j - 1), k.y)
            Dir.Vertical -> k..At(k.x, k.y + (j - 1))
        }
}

class Carrier(at: At, dir: Dir) : Ship("🛬", 5, at, dir)
class Battleship(at: At, dir: Dir) : Ship("🛳", 4, at, dir)
class Destroyer(at: At, dir: Dir) : Ship("⛴", 3, at, dir)
class Submarine(at: At, dir: Dir) : Ship("🫧", 3, at, dir)
class PatrolBoat(at: At, dir: Dir) : Ship("🚤", 2, at, dir)

enum class Dir {
    Horizontal,
    Vertical
}