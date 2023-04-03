package be.swsb.coderetreat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class HelloTest {

    @Test
    fun `greet - should return World`() {
        assertThat(Hello().greet()).isEqualTo("World!")
    }

    @Test
    fun `sayGreeting - without mock should return World`() {
        val helloMock = HelloService()
        assertThat(helloMock.sayGreeting()).isEqualTo("World!")
    }

    @Test
    fun `sayGreeting - with mock should return a mocked greeting`() {
        val helloMock : HelloService = mock()
        whenever(helloMock.sayGreeting()) doReturn("Snarf!")
        assertThat(helloMock.sayGreeting()).isEqualTo("Snarf!")
    }
}
