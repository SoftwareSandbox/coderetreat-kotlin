package be.swsb.coderetreat

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito


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
        val helloMock = mock<HelloService>()
        whenever(helloMock.sayGreeting()) doReturn("Snarf!")
        assertThat(helloMock.sayGreeting()).isEqualTo("Snarf!")
    }
}
