package com.checkers.neuroEvolution

import kotlin.random.Random

class Matrix() {
    private var rows: Int = 0
    private var cols: Int = 0
    private var data: List<List<Double>> = listOf()

    companion object {
        fun randomMatrix(rows: Int = 1, cols: Int = 1) =
            Matrix(cols, rows).apply {
                fillRandomData()
            }

        fun fromList(list: List<Double>): Matrix =
            Matrix(cols = list.size).apply {
                data = data.map { row -> row.mapIndexed { index, _ -> list[index] } }
            }
    }

    constructor(rows: Int = 1, cols: Int = 1) : this() {
        this.cols = cols
        this.rows = rows
        data = List(rows) { List(cols) { 0.0 } }
    }

    fun fillRandomData() {
        data = data.map { row ->
            row.map { Random.nextDouble(1.0) }
        }
    }

    // rotates matrix 90 degrees to the right
    private fun rotate(): Matrix {
        return Matrix(this.cols, this.rows).apply {
            data = data.mapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, _ ->
                    this@Matrix.data[colIndex][rowIndex]
                }
            }.map { row ->
                row.asReversed()
            }
        }
    }

    /**
     * mathematical operations on matrix
     **/

    infix fun dot(other: Matrix): Matrix? {
        // todo: throw instead of null
        if (this.cols != other.rows) return null

        val rotatedOther = other.rotate()

        return Matrix(this.rows, other.cols).apply {
            data = data.mapIndexed {rowIndex, row ->
                row.mapIndexed {colIndex, number ->
                    this@Matrix.data[rowIndex].zip(rotatedOther.data[colIndex]).sumOf { it.first * it.second }
                }
            }
        }
    }

    infix fun add(other: Matrix): Matrix =
        copy().apply {
            data = data.mapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, number ->
                    number + other.data[rowIndex][colIndex]
                }
            }
        }

    private fun copy(): Matrix =
        Matrix(this.rows, this.cols).apply {
            data = data.mapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, _ ->
                    this@Matrix.data[rowIndex][colIndex]
                }
            }
        }

    fun map(func: (x: Double) -> Double): Matrix =
        copy().apply { data.map { row -> row.map { number -> func(number) } }.also { data = it } }


    fun print() {
        println("[")
        data.forEach { row ->
            print("[")
            row.forEach { number ->
                if (row.last() == number) println("$number],")
                else print("$number, ")
            }
        }
        println("]")
    }
}
