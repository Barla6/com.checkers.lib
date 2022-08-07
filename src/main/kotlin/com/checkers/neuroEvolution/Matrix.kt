package com.checkers.neuroEvolution

import com.checkers.utlis.initOnce
import java.io.Serializable
import kotlin.random.Random

class Matrix(private val rows: Int = 1, private val cols: Int = 1) : Serializable {
    var data: List<List<Double>> by initOnce()

    companion object {
        fun randomMatrix(rows: Int = 1, cols: Int = 1, min: Double = 0.0, max: Double = 1.0) =
            Matrix(rows, cols).apply { fillRandomData(min, max) }

        fun fromList(rows: Int = 1, cols: Int = 1, list: List<Double>): Matrix {
            if (rows*cols != list.size) throw Throwable("can't create matrix from list")
            return Matrix(rows, cols, list)
        }

    }

    private constructor(rows: Int = 1, cols: Int = 1, list: List<Double>): this(rows, cols) {
        data = (0 until rows).map { rowIndex ->
            (0 until cols).map { colIndex ->
                list[rowIndex*cols + colIndex]
            }
        }
    }

    private fun fillRandomData(min: Double = 0.0, max: Double = 1.0) {
        data = List(rows) { List(cols) { 0.0 } }.map { row ->
            row.map { Random.nextDouble(min, max) }
        }
    }

    /**
     * mathematical operations on matrix
     **/

    infix fun dot(other: Matrix): Matrix {
        if (this.cols != other.rows) throw Throwable("can't perform dot operation on matrices, cols of one has to be equals rows of other")

        val rotatedOther = other.rotate()

        return Matrix(this.rows, other.cols).apply {
            data = (0 until rows).map { rowIndex ->
                (0 until cols).map { colIndex ->
                    this@Matrix.data[rowIndex].zip(rotatedOther.data[colIndex]).sumOf { it.first * it.second }
                }
            }
        }
    }

    infix fun add(other: Matrix): Matrix =
        Matrix(this.rows, this.cols).apply {
            data = (0 until rows).map { rowIndex ->
                (0 until cols).map { colIndex ->
                    this@Matrix.data[rowIndex][colIndex] + other.data[rowIndex][colIndex]
                }
            }
        }


    /**
     * non-mathematical operations on matrix
     **/

    // rotates matrix 90 degrees to the right
    private fun rotate(): Matrix {
        return Matrix(this.cols, this.rows).apply {
            data = (0 until rows).map{ rowIndex ->
                (0 until cols).map{ colIndex ->
                    this@Matrix.data[colIndex][rowIndex]
                }
            }.map { row ->
                row.asReversed()
            }
        }
    }

    private fun copy(): Matrix = map {x -> x}

    fun map(func: (x: Double) -> Double): Matrix =
        Matrix(rows, cols).apply {
            this@Matrix.data.map { row -> row.map { number -> func(number) }
            }.also { data = it } }

    override fun toString() =
         "[\n" +
        data.fold("") { initial, row ->
            "$initial[" + row.fold("") { initial2, number ->
                initial2 + if (row.last() == number) "$number],\n"
                else "$number, "
            }
        } + "]"
}
