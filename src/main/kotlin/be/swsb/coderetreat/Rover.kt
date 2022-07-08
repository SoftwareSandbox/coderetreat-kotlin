package be.swsb.coderetreat

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result

typealias Scanner = (Position) -> Obstacle?

data class Rover(
    private val position: Position = at(0, 0),
    private val facing: Direction = Direction.North,
    private val scanner: Scanner = { null },
) {
    // DO NOT CHANGE THIS CONTRACT!
    fun receive(commandsAsString: String): Rover {
        return tryExecutingCommands(commandsAsString)
    }

    // You probably want a Result<Rover, SomeFailure> here
    private fun tryExecutingCommands(commandsAsString: String): Rover =
        commandsAsString.split(",")
            .map { commandString ->
                when (commandString.lowercase()) {
                    "f" -> forwards
                    "b" -> backwards
                    "r" -> turnRight
                    "l" -> turnLeft
                    else -> identity
                }
            }.fold(this) { mutatedRover, cmd -> mutatedRover.cmd() }

    private val forwards: Command = { copy(position = position + facing.asVector()) }
    private val backwards: Command = { copy(position = position + facing.asVector().inverted()) }
    private val turnRight: Command = { copy(facing = facing.rotateClockwise()) }
    private val turnLeft: Command = { copy(facing = facing.rotateCounterClockwise()) }
    private val identity: Command = { this }
}
typealias Command = Rover.() -> Rover

sealed class Obstacle(private val message: String) {
    override fun toString() = message
}

object Crater : Obstacle("Crater")
object Martian : Obstacle("Martian")