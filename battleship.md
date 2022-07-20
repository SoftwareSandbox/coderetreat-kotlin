# Battleship Kata

You can find more info on the game of [Battleship here](https://en.wikipedia.org/wiki/Battleship_(game)).

## Rules
We'll start with the grid, it's 10x10.

Ships can only be placed on spaces inside the grid.

Ships can only be placed orthogonally (not diagonally).

There are 5 types of ships.
1 Carrier (which has a size of 5).
1 Battleship (size of 4)
1 Destroyer (size of 3)
1 Submarine (also size of 3)
1 Patrol Boat (size of 2)

You can only put one of each on the grid.

There's a "receive shot" function that takes a coordinate. This returns either HIT, OCEAN or SUNK.

HIT is when one of your ships is hit.
OCEAN is when none of your ships are hit.
SUNK is when a ship has sustained an amount of hits equal to its size (a patrol boat is sunk when it got hit twice)