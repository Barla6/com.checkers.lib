package com.checkers.neuroEvolution

import kotlin.math.exp

class NeuralNetwork(private val input_nodes: Int,
                    private val hidden_nodes: Int,
                    private val output_nodes: Int) {

    private val weights_input_hidden = Matrix.randomMatrix(hidden_nodes, input_nodes)
    private val weights_hidden_output = Matrix.randomMatrix(output_nodes, hidden_nodes)

    private val biases_hidden = Matrix.randomMatrix(hidden_nodes, 1)
    private val biases_output = Matrix.randomMatrix(output_nodes, 1)

    // activation function: sigmoid
    private fun activation(x: Double): Double = 1 / (1 + exp(-x))

    fun predict(input: List<Double>) {
        // todo: catch and log
        if (input.size != input_nodes) return
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