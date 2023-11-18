package be.swsb.coderetreat.battleship.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import be.swsb.coderetreat.battleship.logic.EventStream
import be.swsb.coderetreat.battleship.logic.Game
import be.swsb.coderetreat.battleship.logic.Piece.*
import be.swsb.coderetreat.battleship.logic.at

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Battleship",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        MainContent()
    }
}

@Composable
@Preview
fun MainContent() {
    val eventStream = EventStream()
    val game = Game.startNewGame(eventStream)
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            (1..10).map { y ->
                Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                    (1..10).map { x ->
                        val displayText = when (game.piece(at(x, y))) {
                            Hit -> """💥"""
                            Sunk -> """🏊"""
                            CarrierPart -> """🛳"""
                            BattleshipPart -> """⛴️"""
                            DestroyerPart -> """🚢"""
                            SubmarinePart -> """🤿"""
                            PatrolBoatPart -> """🛥"""
                            null -> """🌊"""
                        }
                        Text(text = displayText)
                    }
                }
            }
        }
    }
}
