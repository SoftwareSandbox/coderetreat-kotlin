package be.swsb.coderetreat.battleship

fun render(game: Game): String {
    return (1..10).joinToString("\n") { y ->
        (1..10).joinToString("") { x ->
            when (game.piece(at(x, y))) {
                CarrierPart -> """🛳️"""
                Hit -> """💥️"""
                null -> """🌊"""
            }
        }
    }
}