package be.swsb.coderetreat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt

data class Complex(val real: Float, val imaginary: Float) {
    val absoluteValue: Float = sqrt(real * real + imaginary * imaginary)

    operator fun plus(other: Complex) =
        Complex(real + other.real, imaginary + other.imaginary)

    operator fun times(other: Complex) =
        Complex(real * other.real - imaginary * other.imaginary, real * other.imaginary + imaginary * other.real)

    fun isInMandelbrotSet(iterations: Int = 100) = generateMandelbrotSequence()
        .take(iterations)
        .none { it.absoluteValue > 2 }

    fun nextMandelbrot(start: Complex) = this * this + start

    fun generateMandelbrotSequence() = sequence {
        val start = this@Complex
        var prev = start
        while (true) {
            yield(prev)
            prev = prev.nextMandelbrot(start)
        }
    }

}

private const val i = 1000

class MandelbrotTest {

    @Test
    fun mandelbrot() {
        val actual = Complex(1f, 0f).generateMandelbrotSequence().take(4).toList()
        assertThat(actual).containsExactly(
            Complex(1f, 0f),
            Complex(2f, 0f),
            Complex(5f, 0f),
            Complex(26f, 0f)
        )
    }

    @Test
    fun `Minus one is part of the mandelbrot set`() {
        val actual: Boolean = Complex(-1f, 0f).isInMandelbrotSet()
        assertThat(actual).isTrue()
    }

    @Test
    fun `One is not part of the mandelbrot set`() {
        val actual: Boolean = Complex(1f, 0f).isInMandelbrotSet()
        assertThat(actual).isFalse()
    }

    @Test
    fun `length of 1,1 must be square root of 1 squared plus 1 squared`() {
        val actual: Float = Complex(1.0f, 1.0f).absoluteValue
        assertThat(actual).isEqualTo(sqrt(2.0f))
    }

    @Test
    fun `next for 0 returns complex`() {
        val actual = Complex(1f, 1f).generateMandelbrotSequence().first()
        assertThat(actual).isEqualTo(Complex(1f, 1f))
    }

    @Test
    fun `next for 1 returns square of the previous next plus the start`() {
        val actual = Complex(1.0f, 0f).generateMandelbrotSequence().drop(1).first()
        assertThat(actual).isEqualTo(Complex(2.0f, 0f))
    }

    @Nested
    inner class ComplexTests {
        @Test
        fun `Complex numbers can be summed`() {
            val actual = Complex(0f, 1f) + Complex(1f, 3f)
            assertThat(actual).isEqualTo(Complex(1f, 4f))
        }

        @Test
        fun `Complex numbers can be multiplied`() {
            val actual = Complex(1f, 2f) * Complex(3f, 4f)
            assertThat(actual).isEqualTo(Complex(-5f, 10f))
        }
    }



    @Test
    fun generateBmp() {
        val size = Dimension(1000, 1000)
        val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
        (0 until size.width).forEach { x ->
            (0 until size.height).forEach { y ->
                val color = if(Point(x,y).toComplexNumber().isInMandelbrotSet(1)) Color.BLACK else Color.WHITE
                img.setRGB(x, y, color.rgb)
            }
        }
        ImageIO.write(img, "BMP", File("test.bmp"))
    }

    @ParameterizedTest
    @MethodSource("mappingData")
    fun mappingFn(actual: Point, expected: Complex) {
        assertThat(actual.toComplexNumber()).isEqualTo(expected)
    }

    fun Point.toComplexNumber() =
        Complex((x - 500) / 250f, (500 - y) / 250f)

    companion object {
        @JvmStatic
        fun mappingData() = listOf(
            arguments(Point(0, 0), Complex(-2f, 2f)),
            arguments(Point(1000, 1000), Complex(2f, -2f)),
        )
    }
}

data class Point(val x: Int, val y: Int)