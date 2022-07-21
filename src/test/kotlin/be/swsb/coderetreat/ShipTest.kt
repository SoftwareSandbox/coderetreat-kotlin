package be.swsb.coderetreat

import be.swsb.coderetreat.Direction.Horizontal
import be.swsb.coderetreat.Direction.Vertical
import be.swsb.coderetreat.FiringResult.Hit
import be.swsb.coderetreat.FiringResult.Sunk
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

class ShipTest {

    @Test
    internal fun `a ship cannot be placed horizontally outside of the grid`() {
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.PatrolBoat, at(9, 0), Horizontal) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Submarine, at(8, 0), Horizontal) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Destroyer, at(8, 0), Horizontal) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Battleship, at(7, 0), Horizontal) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Carrier, at(6, 0), Horizontal) }
    }

    @Test
    internal fun `a ship cannot be placed vertically outside of the grid`() {
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.PatrolBoat, at(0, 9), Vertical) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Submarine, at(0, 8), Vertical) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Destroyer, at(0, 8), Vertical) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Battleship, at(0, 7), Vertical) }
        assertThatExceptionOfType(IllegalStateException::class.java)
            .isThrownBy { Ship(ShipClass.Carrier, at(0, 6), Vertical) }
    }

    @Test
    internal fun `a ship is sunk when all of its positions were hit`() {
        val patrolBoat = PatrolBoat(at(0, 0), Vertical)
        assertThat(patrolBoat.receiveFire(at(0, 0))).isEqualTo(Hit)
        assertThat(patrolBoat.receiveFire(at(0, 1))).isEqualTo(Sunk)

        val carrier = Carrier(at(0, 4), Horizontal)
        assertThat(carrier.receiveFire(at(0, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(1, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(4, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(3, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(2, 4))).isEqualTo(Sunk)
    }

    @Test
    internal fun `hitting a ship twice on the same position only registers as damage once`() {
        val patrolBoat = PatrolBoat(at(0, 0), Vertical)
        assertThat(patrolBoat.receiveFire(at(0, 0))).isEqualTo(Hit)
        assertThat(patrolBoat.receiveFire(at(0, 0))).isEqualTo(Hit)
        assertThat(patrolBoat.receiveFire(at(0, 1))).isEqualTo(Sunk)

        val carrier = Carrier(at(0, 4), Horizontal)
        assertThat(carrier.receiveFire(at(0, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(1, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(4, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(3, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(4, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(3, 4))).isEqualTo(Hit)
        assertThat(carrier.receiveFire(at(2, 4))).isEqualTo(Sunk)
    }
}