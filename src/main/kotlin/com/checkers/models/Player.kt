package com.checkers.models

import kotlin.random.Random

sealed interface Player {

    val direction: PlayerDirection

    val directions
        get() = direction.directions

    val crowningRow
        get() = direction.crowningRow

    val startingRows
        get() = direction.startingRows

    fun pickBoard(stepSequences: List<StepSequence>): StepSequence {
        return stepSequences.get(Random(stepSequences.size).nextInt())
    }

    class Computer(override val direction: PlayerDirection) : Player {
        override fun pickBoard(stepSequences: List<StepSequence>): StepSequence {
            return super.pickBoard(stepSequences)
        }
    }

    class Human(override val direction: PlayerDirection) : Player {
        override fun pickBoard(stepSequences: List<StepSequence>): StepSequence {
            return super.pickBoard(stepSequences)
        }
    }
}