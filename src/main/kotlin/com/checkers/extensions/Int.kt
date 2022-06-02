package com.checkers.extensions

import kotlin.math.sign

infix fun Int.toward(to:Int): IntProgression =
    IntProgression.fromClosedRange(
        this, to,
        step= if ((to-this).sign != 0) (to-this).sign else 1)


