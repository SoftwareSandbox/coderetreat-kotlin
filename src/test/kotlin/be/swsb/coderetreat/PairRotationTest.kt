package be.swsb.coderetreat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PairRotationTest {

    @Test
    internal fun `Rotating with 6 People`() {
        val people = listOf("Silvio", "Olivier", "Florent", "Jeremie", "Jelle", "Agnes")

        people.pairUp()
            .containsExactly(
                "Silvio" and "Olivier",
                "Florent" and "Jeremie",
                "Jelle" and "Agnes",
            )
            .rotatePairs()
            .containsExactly(
                "Silvio" and "Jeremie",
                "Florent" and "Agnes",
                "Jelle" and "Olivier"
            )
            .rotatePairs()
            .containsExactly(
                "Silvio" and "Agnes",
                "Florent" and "Olivier",
                "Jelle" and "Jeremie"
            )
            .rotatePairs()
            .containsExactly(
                "Silvio" and "Olivier",
                "Florent" and "Jeremie",
                "Jelle" and "Agnes"
            )
    }

    @Test
    internal fun `Rotating with 6 People, including desks`() {
        val people = listOf("Silvio", "Olivier", "Florent", "Jeremie", "Jelle", "Agnes")

        people.pairUpToDesks()
            .containsExactly(
                Codebase(1).with { "Silvio" and "Olivier" },
                Codebase(2).with { "Florent" and "Jeremie" },
                Codebase(3).with { "Jelle" and "Agnes" },
            )
            .rotateFirst()
            .containsExactly(
                Codebase(1).with { "Silvio" and "Jeremie" },
                Codebase(2).with { "Florent" and "Agnes" },
                Codebase(3).with { "Jelle" and "Olivier" },
            )
            .rotateEven()
            .containsExactly(
                Codebase(1).with { "Jeremie" and "Jelle" },
                Codebase(2).with { "Agnes" and "Silvio" },
                Codebase(3).with { "Olivier" and "Florent" },
            )
            .rotateOdd()
            .containsExactly(
                Codebase(1).with { "Jelle" and "Agnes" },
                Codebase(2).with { "Silvio" and "Olivier" },
                Codebase(3).with { "Florent" and "Jeremie" },
            )
            .rotateEven()
            .containsExactly(
                Codebase(1).with { "Agnes" and "Florent" },
                Codebase(2).with { "Olivier" and "Jelle" },
                Codebase(3).with { "Jeremie" and "Silvio" },
            )
            .rotateOdd()
            .containsExactly(
                Codebase(1).with { "Florent" and "Olivier" },
                Codebase(2).with { "Jelle" and "Jeremie" },
                Codebase(3).with { "Silvio" and "Agnes" },
            )
            .rotateEven()
            .containsExactly(
                Codebase(1).with { "Olivier" and "Silvio" },
                Codebase(2).with { "Jeremie" and "Florent" },
                Codebase(3).with { "Agnes" and "Jelle" },
            )
            .rotateOdd()
            .containsExactly(
                Codebase(1).with { "Silvio" and "Jeremie" },
                Codebase(2).with { "Florent" and "Agnes" },
                Codebase(3).with { "Jelle" and "Olivier" },
            )
    }
}

private fun List<Ensemble>.containsExactly(vararg pairs: Ensemble) = also {
    assertThat(it).containsExactlyElementsOf(pairs.toList())
}

private fun List<Desk>.containsExactly(vararg desks: Desk) = also {
    assertThat(it).containsExactlyElementsOf(desks.toList())
}
private infix fun String.and(other: String): Ensemble = this to other

private fun Codebase(id: Codebase) = DeskBuilder(id)
private class DeskBuilder(private val id: Codebase) {
    fun with(block: () -> Ensemble) = Desk(codebase = id, ensemble = block())
}
