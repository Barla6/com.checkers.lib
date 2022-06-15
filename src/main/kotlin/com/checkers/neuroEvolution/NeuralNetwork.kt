package com.checkers.neuroEvolution

import kotlin.math.exp

class NeuralNetwork {

    companion object {
        const val INPUT_NODES = 32
        const val HIDDEN_NODES = 16
        const val OUTPUT_NODES = 1
    }

    private val weights_input_hidden = Matrix.randomMatrix(HIDDEN_NODES, INPUT_NODES)
    private val weights_hidden_output = Matrix.randomMatrix(OUTPUT_NODES, HIDDEN_NODES)

    private val biases_hidden = Matrix.randomMatrix(HIDDEN_NODES, 1)
    private val biases_output = Matrix.randomMatrix(OUTPUT_NODES, 1)

    // activation function: sigmoid
    private fun activation(x: Double): Double = 1 / (1 + exp(-x))

    fun predict(input: List<Double>) {
        // todo: catch and log
        if (input.size != INPUT_NODES) return
        val inputMatrix = Matrix.fromList(input)

        // todo: catch and log
        val hidden = inputMatrix.dot(weights_input_hidden) ?: return
        val hiddenWithBias = hidden.add(biases_hidden)
        val hiddenAfterActivation = hiddenWithBias.map { number -> activation(number) }

        // todo: catch and log
        val output = hiddenAfterActivation.dot(weights_hidden_output) ?: return
        val outputWithBias = output add biases_output
        val outputAfterActivation = outputWithBias.map { number -> activation(number) }

        return outputAfterActivation.print()
    }
}