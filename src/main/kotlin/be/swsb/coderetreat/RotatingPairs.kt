package be.swsb.coderetreat

fun List<String>.pairUp() = zipWithNext().windowed(1, 2).flatten()
fun List<Ensemble>.rotatePairs() = plus(this.first()).zipWithNext { pair1, pair2 -> pair1.first to pair2.second }
fun List<Ensemble>.rotatePairsOtherDirection() = reversed().plus(this.last()).zipWithNext { pair1, pair2 -> pair1.first to pair2.second }.reversed()

fun List<String>.pairUpToDesks(): List<Desk> = pairUp().mapIndexed { idx, pair -> Desk(idx + 1, pair) }
fun List<Desk>.rotateFirst(): List<Desk> = map(Desk::ensemble).rotatePairs().mapIndexed { idx, pair -> Desk(codebase = idx + 1, ensemble = pair) }
fun List<Desk>.rotateEven(): List<Desk> = map(Desk::ensemble).map { it.switch() }.rotatePairsOtherDirection().mapIndexed { idx, pair -> Desk(codebase = idx + 1, ensemble = pair) }
fun List<Desk>.rotateOdd(): List<Desk> = map(Desk::ensemble).map { it.switch() }.rotatePairs().mapIndexed { idx, pair -> Desk(codebase = idx + 1, ensemble = pair) }

typealias Ensemble = Pair<String, String>

fun Ensemble.switch(): Ensemble = second to first

typealias Codebase = Int


data class Desk(val codebase: Codebase, val ensemble: Ensemble) {
    override fun toString() = "Desk$codebase(${ensemble.first},${ensemble.second})"
}

fun List<Desk>.asString(): String = map { it.toString() }.joinToString("\n") { it }
