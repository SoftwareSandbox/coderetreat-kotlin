package be.swsb.coderetreat.battleship

fun Game.render() =
    (1..10).joinToString("\n") { y ->
        (1..10).joinToString("") { x ->
            when (this.piece(at(x, y))) {
                Hit -> """💥"""
                Sunk -> """🏊"""
                CarrierPart -> """🛳"""
                BattleshipPart -> """⛴️"""
                DestroyerPart -> """🚢"""
                SubmarinePart -> """🛥"""
                PatrolBoatPart -> """🚤"""
                null -> """🌊"""
            }
        }
    }
