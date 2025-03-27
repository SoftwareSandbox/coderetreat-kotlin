Reference implementation by Joep Schuurkes: https://smallsheds.garden/counterstring/

Joep also inspired this kata at SocratesFR 2025. You can find his repository with multiple different language starters at https://codeberg.org/joeposaurus/counterstring-codekata

Counterstrings are strings that tell you how long they are. For example a counterstring with length 9 looks like this: *3*5*7*9*. Each number tells you the position of the asterisk (*) following the number.

Examples:

* counterstring(1)  = *
* counterstring(2)  = 2*
* counterstring(3)  = *3*
* counterstring(4)  = 2*4*
* counterstring(9)  = *3*5*7*9*
* counterstring(10) = *3*5*7*10*

Applying "baby steps" or the "TDD as if you meant it" constraints works wonders for this code kata.