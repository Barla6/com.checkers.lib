package com.checkers

enum class Player(
    val directions: Array<StepDirection>,
    val crowningRow: Int
) {
    COMPUTER(
        directions = arrayOf(StepDirection.DOWN_RIGHT, StepDirection.DOWN_LEFT),
        crowningRow = 7
    ) {
        override val enemy: Player
            get() = PLAYER
    },
    PLAYER(
        directions = arrayOf(StepDirection.UP_LEFT, StepDirection.UP_RIGHT),
        crowningRow = 0
    ) {
        override val enemy: Player
            get() = COMPUTER
    };

    abstract val enemy: Player
}