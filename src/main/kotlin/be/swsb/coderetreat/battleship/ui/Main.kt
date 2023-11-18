package be.swsb.coderetreat.battleship.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import be.swsb.coderetreat.battleship.logic.*
import be.swsb.coderetreat.battleship.logic.Piece.*

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
    val initialGame = Game.startNewGame(eventStream)
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = CenterHorizontally,
        ) {
            var game by rememberSaveable { mutableStateOf(initialGame) }
            var lastShipPlaced by remember { mutableStateOf("") }
            val onClick = {
                val ship = Carrier
                game = game.place(ship = ship, bowCoordinate = Coordinate(1, 1), placement = Placement.Horizontally)
                lastShipPlaced = "$ship placed!"
            }
            Button(
                modifier = Modifier.padding(3.dp),
                onClick = onClick,
            ) { Text("Place ship") }
            Text(lastShipPlaced)
            Field(game)
        }
    }
}

@Composable
private fun Field(game: Game) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = CenterHorizontally,
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
