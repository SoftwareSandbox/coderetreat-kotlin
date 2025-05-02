# Dittle

Dittle is a dice game where 2 players try to reach their opponents back rank with all their dice on a 7x7 grid.
To move a die, it can be rolled orthogonally 1 step, jump over another die, or perform a combination of the former (roll then jump then jump again).

## Rules

One can roll or jump in any orthogonal direction.  
One can never roll or jump diagonally.  
One can combine a jump after a roll, but not a roll after a jump.  
One may choose to not jump in a combination with a roll.  
The way the dice roll is deterministic.  
When a die jumps, the face remains the same, and it moves 2 spaces.  
When a die rolls, its face changes based on deterministic rules, and it moves 1 space.
A die can only jump in a direction when there is any other die in that direction and there is an empty space next to it.

## Deterministic Dice Definition (DDD)
Here's a flattened, visual representation of a real die:
```
   ⚂
 ⚁ ⚅ ⚄ ⚀
   ⚃
```
Starting dice are always a `⚅` oriented the same way (rolling right would land them on a `⚁`).  
Given the above, flattened representation, these are examples of correct, deterministic rolls.

Rolling right:  
`⚅ -> ⚁`, `⚁ -> ⚀`, `⚀ -> ⚄`, `⚄ -> ⚅`

Rolling down:  
`⚅ -> ⚂`, `⚂ -> ⚀`, `⚀ -> ⚃`, `⚃ -> ⚅`

Rolling up, after having rolled left twice:  
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
Right always starts, here they rolled the 2nd die to the left, revealing a new side: ⚄
```
⚅ . . . . . ⚅
⚅ . . . . ⚄ .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Left's turn, they also rolled the 2nd die but to the right, revealing a new side: ⚁
```
⚅ . . . . . ⚅
. ⚁ . . . ⚄ .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Right continues rolling
```
⚅ . . . . . ⚅
. ⚁ . . ⚀ . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Left continues rolling
```
⚅ . . . . . ⚅
. . ⚀ . ⚀ . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Right rolls, then jumps (when jumping, the die does not roll)
```
⚅ . . . . . ⚅
. ⚁ ⚀ . . . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Left continues rolling
```
⚅ . . . . . ⚅
. ⚁ . ⚄ . . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
Right continues rolling and has reached one of their dice to the opposition's back rank, scoring 6.
```
⚅ . . . . . ⚅
⚅ . . ⚄ . . .
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
⚅ . . . . . ⚅
```
