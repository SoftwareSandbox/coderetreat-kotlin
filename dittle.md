# Dittle

Dittle is a dice game where 2 players try to reach their opponents back rank with all their dice on a 7x7 grid.
To move a die, it can be tilted orthogonally 1 step, jump over another die, or perform a combination of the former (tilt then jump then jump again).

## Rules

One can tilt or jump in any orthogonal direction.  
One can never tilt or jump diagonally.  
One can combine a jump after a tilt, but not a tilt after a jump.  
One may choose to not jump in a combination with a tilt.  
The way the dice tilt is deterministic.  
When a die jumps, the face remains the same, and it moves 2 spaces.  
When a die tilts, its face changes based on deterministic rules, and it moves 1 space.
A die can only jump in a direction when there is any other die in that direction and there is an empty space next to it.

## Deterministic Dice Definition (DDD)
Here's a flattened, visual representation of a real die:
```
   ⚂
 ⚁ ⚅ ⚄ ⚀
   ⚃
```
Starting dice are always a `⚅` oriented the same way (tilting right would land them on a `⚁`).  
Given the above, flattened representation, these are examples of correct, deterministic tilts.

Tilting right:  
`⚅ -> ⚁`, `⚁ -> ⚀`, `⚀ -> ⚄`, `⚄ -> ⚅`

Tilting down:  
`⚅ -> ⚂`, `⚂ -> ⚀`, `⚀ -> ⚃`, `⚃ -> ⚅`

Tilting up, after having tilted left twice:  
`⚅ -> ⚄`, `⚄ -> ⚀`, `⚀ -> ⚃`, `⚃ -> ⚅`, `⚅ -> ⚂`, `⚂ -> ⚀`

## Representation
All the dice values (1-6): `⚀ ⚁ ⚂ ⚃ ⚄ ⚅`
Here's how the game starts:
```
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Right always starts, here they tilted the 2nd die to the left, revealing a new side: ⚄
```
⚅ . . . . . ⚅
⚅ . . . . ⚄ .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Left's turn, they also tilted the 2nd die but to the right, revealing a new side: ⚁
```
⚅ . . . . . ⚅
. ⚁ . . . ⚄ .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Right continues tilting
```
⚅ . . . . . ⚅
. ⚁ . . ⚀ . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Left continues tilting
```
⚅ . . . . . ⚅
. . ⚀ . ⚀ . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Right tilts, then jumps (when jumping, the die does not tilt)
```
⚅ . . . . . ⚅
. ⚁ ⚀ . . . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Left continues tilting
```
⚅ . . . . . ⚅
. ⚁ . ⚄ . . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Right continues tilting and has reached one of their dice to the opposition's back rank, scoring 6.
```
⚅ . . . . . ⚅
⚅ . . ⚄ . . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```

## Alternative playstyles
* What if the dice weren't D6's, but D8's, D10's, D12's or D20's?
* What if the grid wasn't 7x7, but 10x10?
* What if the grid wasn't a square?
