package be.swsb.coderetreat

import be.swsb.coderetreat.Direction.*
import be.swsb.coderetreat.FiringResult.*
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlayingFieldTest {
    @Test
    internal fun `a ship can be hit on any of the positions it holds`() {
        val playingFieldWithAllShips = {
            playingFieldWith(
                PatrolBoat(at(0, 0), Horizontal),
                Submarine(at(0, 1), Horizontal),
                Destroyer(at(0, 2), Horizontal),
                Battleship(at(0, 3), Horizontal),
                Carrier(at(0, 4), Horizontal),
            )
        }
        val expectedHits = listOf(
            at(0, 0), at(1, 0), // patrolboat
            at(0, 1), at(1, 1), at(2, 1), // submarine
            at(0, 2), at(1, 2), at(2, 2), // destroyer
            at(0, 3), at(1, 3), at(2, 3), at(3, 3), // battleship
            at(0, 4), at(1, 4), at(2, 4), at(3, 4), at(4, 4), // cruiser
        )
        expectedHits.forEach { expectedHit ->
            assertThat(playingFieldWithAllShips().fire(expectedHit))
                .describedAs("Expected hit at $expectedHit")
                .isEqualTo(Hit)
        }

        val expectedMisses = (at(2, 0)..at(9, 0)) +
                (at(3, 1)..at(9, 1)) +
                (at(3, 2)..at(9, 2)) +
                (at(4, 3)..at(9, 3)) +
                (at(5, 4)..at(9, 4)) +
                (6..9).flatMap { row -> at(0, row)..at(9, row) }
        expectedMisses.forEach { expectedMiss ->
            assertThat(playingFieldWithAllShips().fire(expectedMiss))
                .describedAs("Expected hit at $expectedMiss")
                .isEqualTo(Ocean)
        }
    }

    @Test
    internal fun `a ship is sunk on the playing field when all of its positions were hit`() {
        val playingField = playingFieldWith(
            PatrolBoat(at(0, 0), Horizontal), // at(0, 0), at(1, 0)
            Carrier(at(0, 4), Horizontal),    // at(0, 4), at(1, 4), at(2, 4), at(3, 4), at(4, 4)
        )

        assertThat(playingField.fire(at(0, 0))).isEqualTo(Hit)
        assertThat(playingField.fire(at(0, 4))).isEqualTo(Hit)
        assertThat(playingField.fire(at(1, 0))).isEqualTo(Sunk)
        assertThat(playingField.fire(at(1, 4))).isEqualTo(Hit)
        assertThat(playingField.fire(at(4, 4))).isEqualTo(Hit)
        assertThat(playingField.fire(at(3, 4))).isEqualTo(Hit)
        assertThat(playingField.fire(at(2, 4))).isEqualTo(Sunk)
    }

    @Test
    internal fun `can only put one of each of the ships on a playingfield`() {
        val playingField = startingPlayingField()
        playingField.place(PatrolBoat(at(0, 0), Horizontal))
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { playingField.place(PatrolBoat(at(0, 1), Horizontal)) }
            .withMessage("There can only be one of each class of ship on the playing field at the same time")
    }

    @Test
    internal fun `ships should not overlap on a playingfield`() {
        val playingField = startingPlayingField()
        playingField.place(PatrolBoat(at(3, 3), Horizontal))
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { playingField.place(Carrier(at(4, 1), Vertical)) }
            .withMessage("Trying to place a Carrier at [(4,1), (4,2), (4,3), (4,4), (4,5)], but it would overlap with a PatrolBoat at [(3,3), (4,3)]")
    }

    internal fun playingFieldWith(vararg ships: Ship): PlayingField = PlayingField(mutableListOf(*ships))
}