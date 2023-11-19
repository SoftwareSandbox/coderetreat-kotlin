package be.swsb.coderetreat.battleship.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import be.swsb.coderetreat.battleship.logic.*
import be.swsb.coderetreat.battleship.logic.Piece.*
import be.swsb.coderetreat.battleship.ui.GameState.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Battleship",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        val events = mutableStateListOf<BattleShipEvent>()
        val game = Game.startNewGame(EventStream(events))
        MainContent(game)
    }
}

@Composable
@Preview
private fun preview() {
    val events = mutableStateListOf<BattleShipEvent>()
    val game = Game.startNewGame(EventStream(events))
    MainContent(game)
}

val allShips = listOf(Carrier, Battleship, Destroyer, Submarine, PatrolBoat)

@Composable
fun MainContent(game: Game) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = CenterHorizontally,
        ) {
            var placement by remember { mutableStateOf(Placement.Horizontally) }
            var ship: Ship by remember { mutableStateOf(Carrier) }
            val shipsToPlace = game.remainingShipsToPlace.toMutableStateList()
            var state by remember { mutableStateOf(PlacingShips) }
            val shipPlacement = { x: Int, y: Int ->
                {
                    game.place(ship = ship, bowCoordinate = Coordinate(x, y), placement = placement)
                    shipsToPlace.remove(ship)
                    shipsToPlace.firstOrNull()?.let { ship = it }
                    if (shipsToPlace.isEmpty()) {
                        state = Firing
                    }
                }
            }
            if (state == PlacingShips) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Text("Place your $ship now.")
                    Button(
                        modifier = Modifier.padding(3.dp),
                        onClick = { placement = placement.toggle() },
                    ) { Text("$placement") }
                }
            } else {
                Text("Fire away!")
            }
            Field(game, state, shipPlacement)
        }
    }
}

private val Game.remainingShipsToPlace get() = (allShips - shipsPlaced.map { it.ship }.toSet())

private fun Placement.toggle() = if (this == Placement.Horizontally) Placement.Vertically else Placement.Horizontally

@Composable
private fun Field(game: Game, state: GameState, shipPlacement: (x: Int, y: Int) -> () -> Unit) {
    fun coordinateHandler(state: GameState, game: Game, x: Int, y: Int): () -> Unit {
        return when (state) {
            PlacingShips -> shipPlacement(x, y)
            Firing -> {
                { game.fire(at(x, y)) }
            }

            GameOver -> {
                { }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = CenterHorizontally,
    ) {
        (1..10).map { y ->
            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                (1..10).map { x ->
                    val displayText = game.piece(at(x, y)).renderAsEmoticon()
                    IconButton(
                        modifier = Modifier.size(40.dp),
                        onClick = coordinateHandler(state, game, x, y),
                    ) { Text(displayText) }
                }
            }
        }
    }
}


private fun Piece?.renderAsEmoticon() = when (this) {
    Hit -> """💥"""
    Sunk -> """🏊"""
    CarrierPart -> """🛳"""
    BattleshipPart -> """⛴️"""
    DestroyerPart -> """🚢"""
    SubmarinePart -> """🤿"""
    PatrolBoatPart -> """🛥"""
    null -> """🌊"""
}

enum class GameState {
    PlacingShips,
    Firing,
    GameOver,
}
