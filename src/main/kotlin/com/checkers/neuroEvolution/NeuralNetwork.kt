package com.checkers.neuroEvolution

import kotlin.math.exp

class NeuralNetwork(private val input_nodes: Int,
                    private val hidden_nodes: Int,
                    private val output_nodes: Int) {

    private val weights_input_hidden = Matrix.randomMatrix(hidden_nodes, input_nodes)
    private val weights_hidden_output = Matrix.randomMatrix(output_nodes, hidden_nodes)

    private val biases_hidden = Matrix.randomMatrix(hidden_nodes, 1)
    private val biases_output = Matrix.randomMatrix(output_nodes, 1)

    var winningsNumber = 0

    val DNA = listOf(
        weights_input_hidden.data.flatten(),
        weights_hidden_output.data.flatten(),
        biases_hidden.data.flatten(),
        biases_output.data.flatten()
    )

    // activation function: sigmoid
    private fun activation(x: Double): Double = 1 / (1 + exp(-x))

    fun predict(input: List<Double>): Double? {
        // todo: catch and log
        if (input.size != input_nodes) return null
        val inputMatrix = Matrix.fromList(input)

        // todo: catch and log
        val hidden = inputMatrix.dot(weights_input_hidden) ?: return null
        val hiddenWithBias = hidden.add(biases_hidden)
        val hiddenAfterActivation = hiddenWithBias.map { number -> activation(number) }

        // todo: catch and log
        val output = hiddenAfterActivation.dot(weights_hidden_output) ?: return null
        val outputWithBias = output add biases_output
        val outputAfterActivation = outputWithBias.map { number -> activation(number) }

        return outputAfterActivation.data.first().first()
    }

    fun addWinning() = winningsNumber++
}