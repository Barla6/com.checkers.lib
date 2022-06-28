package com.checkers.models

typealias LeadingStepAndFinalBoard = Pair<StepSequence, Board>

val LeadingStepAndFinalBoard.leadingStep: StepSequence
    get() = first

val LeadingStepAndFinalBoard.finalBoard: Board
    get() = second