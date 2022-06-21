package com.checkers.models

enum class PlayerDirection(
    val directions: Array<StepDirection>,
    val crowningRow: Int,
    val startingRows: List<Int>
) {
    DOWNWARDS(
            directions = arrayOf(StepDirection.DOWN_RIGHT, StepDirection.DOWN_LEFT),
            crowningRow = 7,
            startingRows = listOf(0, 1, 2)
    ) {
        override fun oppositeTo(playerDirection: PlayerDirection) = playerDirection == UPWARDS
    },
    UPWARDS(
            directions = arrayOf(StepDirection.UP_LEFT, StepDirection.UP_RIGHT),
            crowningRow = 0,
            startingRows = listOf(5, 6, 7)
    ) {
        override fun oppositeTo(playerDirection: PlayerDirection) = playerDirection == DOWNWARDS
    };

    abstract infix fun oppositeTo(playerDirection: PlayerDirection): Boolean
}


