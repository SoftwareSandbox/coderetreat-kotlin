package be.swsb.coderetreat

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    internal fun `position rangeTo`() {
        assertThat(at(0, 0)..at(0, 1)).containsExactly(at(0, 0), at(0, 1))
        assertThat(at(0, 0)..at(0, 5))
            .containsExactly(at(0, 0), at(0, 1), at(0, 2), at(0, 3), at(0, 4), at(0, 5))
        assertThat(at(0, 0)..at(1, 0)).containsExactly(at(0, 0), at(1, 0))
        assertThat(at(0, 0)..at(5, 0))
            .containsExactly(at(0, 0), at(1, 0), at(2, 0), at(3, 0), at(4, 0), at(5, 0))
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { at(0, 1)..at(1, 0) }
    }

}