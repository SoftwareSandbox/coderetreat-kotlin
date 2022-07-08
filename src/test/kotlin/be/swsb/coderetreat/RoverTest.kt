package be.swsb.coderetreat

import be.swsb.coderetreat.Direction.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class RoverTest {

    @Test
    fun `A Rover can receive commands and reacts accordingly to them`() {
        val initialRover = Rover(at(0, 0), facing = North)

        val rover = initialRover.receive("f,f,f,r,b,b,l,l,f")

        assertThat(rover).isEqualTo(Rover(at(-3, 3), facing = West))
    }

    @Test
    fun `A Rover shall ignore commands after it encountered an obstacle`() {
        val craterPosition = at(0, 4)
        val positionBeforeHittingTheCrater = craterPosition + Vector(0, -1)
        val knownObstacles = mapOf(craterPosition to Crater, at(-3, -3) to Martian)

        val premonitionScanner: Scanner = { pos -> knownObstacles[pos] }
        val defaultRover = Rover(position = at(0, 0), facing = North, scanner = premonitionScanner)

        val rover = defaultRover.receive("f,f,f,f,b,f,l,r")

        assertThat(rover).isEqualTo(defaultRover.copy(position = positionBeforeHittingTheCrater))
    }
}
