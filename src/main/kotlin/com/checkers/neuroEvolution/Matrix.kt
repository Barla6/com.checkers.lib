package com.checkers.neuroEvolution

import kotlin.random.Random

class Matrix() {
    val rows: Int = 0
    val cols: Int = 0
    var data: List<List<Double>> = listOf()

    companion object {
        fun randomMatrix(cols: Int, rows: Int = 1) =
            Matrix(cols, rows).apply { fillRandomData() }

        fun fromList(list: List<Double>): Matrix =
            Matrix(list.size).apply {
                data = data.map { row -> row.mapIndexed { index, d -> list[index] } }
            }
    }

    constructor(cols: Int, rows: Int = 1) : this() {
        data = List(rows) { List(cols) { 0.0 } }
    }

    fun fillRandomData() =
        data.map { row -> row.map { col -> Random.nextDouble(1.0)} }.also { data = it }


    fun rotate(): Matrix {
        return Matrix(this.rows, this.cols).apply {

        }
    }

    // mathematical operations on matrix

    infix fun dot(other: Matrix): Matrix? {
        if (this.cols != other.rows) return null

        return Matrix(this.rows, other.cols).apply {

        }
    }
}
