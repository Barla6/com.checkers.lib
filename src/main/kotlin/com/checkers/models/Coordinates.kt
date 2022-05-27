package com.checkers.models

import com.checkers.Constants
import kotlin.math.abs

data class Coordinates(val row: Int, val col: Int) {

    fun insideBoard(): Boolean {
        return this.row in 0 until Constants.ROWS_NUMBER && this.col in 0 until Constants.COLS_NUMBER
    }

    fun step(stepDirection: StepDirection): Coordinates {
        return Coordinates(
            row + stepDirection.rowDirection,
            col + stepDirection.colDirection
        )
    }

    fun cloneCoordinate(): Coordinates = Coordinates(row, col)

    fun nextTo(other: Coordinates):Boolean =
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
}