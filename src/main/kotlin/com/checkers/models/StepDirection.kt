package com.checkers.models

import kotlin.math.abs
import kotlin.math.sign

enum class StepDirection(private val direction: Pair<Int, Int>) {
    DOWN_RIGHT(Pair(1, 1)),
    DOWN_LEFT(Pair(1, -1)),
    UP_RIGHT(Pair(-1, 1)),
    UP_LEFT(Pair(-1, -1));

    val rowDirection: Int
        get() = this.direction.first

    val colDirection: Int
        get() = this.direction.second

    companion object {
        fun getDirection(startCoordinates: Coordinates, endCoordinates: Coordinates): StepDirection? {
            val rowDirection = endCoordinates.row - startCoordinates.row
            val colDirection = endCoordinates.col - startCoordinates.col

            if (abs(rowDirection) != abs(colDirection)) return null

            return values().find { stepDirection ->
                stepDirection.rowDirection == rowDirection.sign &&
                        stepDirection.colDirection == colDirection.sign
            }
        }
    }
}