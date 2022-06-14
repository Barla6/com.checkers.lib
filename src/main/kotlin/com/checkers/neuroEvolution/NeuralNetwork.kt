package com.checkers.neuroEvolution

import javax.print.DocFlavor
import kotlin.math.exp

class NeuralNetwork {

    companion object {
        const val INPUT_NODES = 32
        const val HIDDEN_NODES = 16
        const val OUTPUT_NODES = 1
    }

    val weights_input_hidden = Matrix.randomMatrix(HIDDEN_NODES, INPUT_NODES)
    val weights_hidden_output = Matrix.randomMatrix(OUTPUT_NODES, HIDDEN_NODES)

    val biases_hidden = Matrix.randomMatrix(HIDDEN_NODES)
    val biases_output = Matrix.randomMatrix(OUTPUT_NODES)

    // activation function: sigmoid
    fun activation(x: Double) = 1 / (1 + exp(-x))

    fun predict(input: List<Double>) {
        val input_matrix = Matrix.fromList(input)

    }
}