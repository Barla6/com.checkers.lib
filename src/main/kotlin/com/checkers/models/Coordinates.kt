package com.checkers.models

import com.checkers.Constants
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign

data class Coordinates(val row: Int, val col: Int) {

    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    fun insideBoard(): Boolean {
        return this.row in 0 until Constants.ROWS_NUMBER && this.col in 0 until Constants.COLS_NUMBER
    }

    fun step(stepDirection: StepDirection, numberOfSteps: Int = 1): Coordinates? {

        return (1..numberOfSteps).fold(this) { currentCoordinate, _ ->
            val next = Coordinates(
                currentCoordinate.row + stepDirection.rowDirection,
                currentCoordinate.col + stepDirection.colDirection
            )
            if (!next.insideBoard()) return null
            return next
        }
    }

    fun cloneCoordinate(): Coordinates = Coordinates(row, col)

    fun nextTo(other: Coordinates): Boolean =
        abs(row - other.row) == 1 && abs(col - other.col) == 1

    override fun equals(other: Any?): Boolean {
        return if (other !is Coordinates) false
        else row == other.row && col == other.col
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }

    fun getAllCoordinatesInDirection(direction: StepDirection): List<Coordinates> {
        val endRow = if (direction.rowDirection.sign < 0) 0 else 7
        val endCol = if (direction.colDirection.sign < 0) 0 else 7

        val amountOfSteps = min(
            abs(endRow - this.row),
            abs(endCol - this.col)
        )

        val endCoordinates = this.step(direction, amountOfSteps)!!

        return range(this, endCoordinates)
    }

    companion object {
        fun range(startCoordinates: Coordinates, endCoordinates: Coordinates): List<Coordinates> {
            StepDirection.getDirection(startCoordinates, endCoordinates)
                ?: throw Exception("can't create range from $startCoordinates to $endCoordinates")

            val rowRange = startCoordinates.row..endCoordinates.row
            val colRange = startCoordinates.col..endCoordinates.col

            return rowRange.zip(colRange).toList().map { Coordinates(it) }
        }

        fun countSteps(startCoordinates: Coordinates, endCoordinates: Coordinates): Int {
            StepDirection.getDirection(startCoordinates, endCoordinates)
                ?: throw Exception("can't create range from $startCoordinates to $endCoordinates")

            return abs(endCoordinates.row - startCoordinates.row)
        }
    }
}