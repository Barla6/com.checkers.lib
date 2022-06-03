package com.checkers.utlis

import kotlin.math.sign

infix fun Int.toward(to: Int): IntProgression =
    IntProgression.fromClosedRange(
        this, to,
        step = (to - this).sign
    )


