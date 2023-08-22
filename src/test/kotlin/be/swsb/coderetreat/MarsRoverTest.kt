package be.swsb.coderetreat

import be.swsb.coderetreat.Kommando.*
import be.swsb.coderetreat.Retning.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MarsRoverTest {

    @Test
    fun `En Rover kjenner â rulle pâ Mars`() {
        MarsRover(at(10,10))
            .mottaKommandoer(Fremover, Fremover).also { assertThat(it).isEqualTo(MarsRover(at(10, 12), Nord)) }
            .mottaKommandoer(TaTilHøyre).also { assertThat(it).isEqualTo(MarsRover(at(10, 12), Øst)) }
            .mottaKommandoer(TaTilHøyre).also { assertThat(it).isEqualTo(MarsRover(at(10, 12), SØr)) }
            .mottaKommandoer(TaTilHøyre).also { assertThat(it).isEqualTo(MarsRover(at(10, 12), Vest)) }
            .mottaKommandoer(TaTilVenstre).also { assertThat(it).isEqualTo(MarsRover(at(10, 12), SØr)) }
            .mottaKommandoer(TaTilVenstre).also { assertThat(it).isEqualTo(MarsRover(at(10, 12), Øst)) }
            .mottaKommandoer(Bakover, Bakover).also { assertThat(it).isEqualTo(MarsRover(at(8, 12), Øst)) }
    }

    @Test
    fun `En Rover kan wrap segselv om Planeten in alle retninger, bâde med Fremover og Bakover`() {
        MarsRover(planet = Mânen)
            .mottaKommandoer(Fremover, Fremover, Fremover, Fremover)
            .also { assertThat(it).isEqualTo(MarsRover(at(0,0), Nord, Mânen)) }
            .mottaKommandoer(TaTilVenstre, Fremover)
            .also { assertThat(it).isEqualTo(MarsRover(at(3,0), Vest, Mânen)) }
            .mottaKommandoer(Bakover)
            .also { assertThat(it).isEqualTo(MarsRover(at(0,0), Vest, Mânen)) }
    }

    @Test
    fun `En posisjon kan bli normalisert`() {
        val maxDimensjoner = at(4, 4)
        assertThat(at(4, 4) % maxDimensjoner).isEqualTo(Posisjon(0,0))
        assertThat(at(3, 4) % maxDimensjoner).isEqualTo(Posisjon(3,0))
        assertThat(at(4, 2) % maxDimensjoner).isEqualTo(Posisjon(0,2))
        assertThat(at(6, 2) % maxDimensjoner).isEqualTo(Posisjon(2,2))
        assertThat(at(-1, 0) % maxDimensjoner).isEqualTo(Posisjon(3,0))
        assertThat(at(2, -1) % maxDimensjoner).isEqualTo(Posisjon(2,3))
    }
}

abstract class Planet(
    private val maxX: Int,
    private val maxY: Int
) {
    fun normaliser(posisjon: Posisjon): Posisjon = posisjon % at(x= maxX, y=maxY)
}

data object Mânen: Planet(4,4)
data object Mars: Planet(400,400)

data class MarsRover(
    private val posisjon: Posisjon = Posisjon(),
    private val retning: Retning = Nord,
    private val planet: Planet = Mars,
) {
    
    fun mottaKommandoer(vararg kommandoer: Kommando): MarsRover =
        kommandoer.fold(this) { acc, kommando ->
            when (kommando) {
                Fremover -> acc.bevegFremover()
                Bakover -> acc.bevegBakover()
                TaTilHøyre -> acc.taTilHøyre()
                TaTilVenstre -> acc.taTilVenstre()
            }
        }
    
    private fun taTilHøyre() = copy(retning = retning.medKlokka())
    private fun taTilVenstre() = copy(retning = retning.motKlokka())
    private fun bevegFremover() = copy(posisjon = planet.normaliser(posisjon + retning.vector))
    private fun bevegBakover() = copy(posisjon = planet.normaliser(posisjon - retning.vector))
}

enum class Kommando {
    Fremover,
    Bakover,
    TaTilHøyre,
    TaTilVenstre,
}

enum class Retning {
    Nord,
    Øst,
    SØr,
    Vest,;

    fun medKlokka() = when (this) {
        Nord -> Øst
        Øst -> SØr
        SØr -> Vest
        Vest -> Nord
    }
    fun motKlokka() = when (this) {
        Nord -> Vest
        Vest -> SØr
        SØr -> Øst
        Øst -> Nord
    }

    val vector: Posisjon get() = when(this) {
        Nord -> Posisjon(0,1)
        Øst -> Posisjon(1,0)
        SØr -> Posisjon(0,-1)
        Vest -> Posisjon(-1,0)
    }

}

data class Posisjon(private val x: Int = 0, private val y: Int = 0) {
    operator fun plus(andre: Posisjon) = Posisjon(x + andre.x, y + andre.y)
    operator fun minus(andre: Posisjon) = Posisjon(x - andre.x, y - andre.y)
    operator fun rem(andre: Posisjon): Posisjon {
        val nyX = if (x.isNegative()) x + andre.x else x % andre.x
        val nyY = if (y.isNegative()) y + andre.y else y % andre.y
        return Posisjon(nyX, nyY)
    }
    private fun Int.isNegative() = this < 0
}

fun at(x: Int, y: Int) = Posisjon(x, y)