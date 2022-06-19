package com.checkers.neuroEvolution

import com.checkers.models.Board
import com.checkers.models.player.Player
import kotlin.math.exp

class NeuralNetwork(private val input_nodes: Int,
                    private val hidden_nodes: Int,
                    private val output_nodes: Int) {

    private val weights_input_hidden = Matrix.randomMatrix(hidden_nodes, input_nodes)
    private val weights_hidden_output = Matrix.randomMatrix(output_nodes, hidden_nodes)

    private val biases_hidden = Matrix.randomMatrix(hidden_nodes, 1)
    private val biases_output = Matrix.randomMatrix(output_nodes, 1)

    var winningsCount = 0
    fun addWinning() = winningsCount++

    val DNA = listOf(
        weights_input_hidden.data.flatten(),
        weights_hidden_output.data.flatten(),
        biases_hidden.data.flatten(),
        biases_output.data.flatten()
    )

    // activation function: sigmoid
    private fun activation(x: Double): Double = 1 / (1 + exp(-x))

    fun rate(inputBoard:Board, player: Player): Double {
        return this.rate(inputBoard.toNeuralNetworkInput(player))
    }

    private fun rate(input: List<Double>): Double {
        if (input.size != input_nodes) throw Throwable("input size to NN is not ok")
        val inputMatrix = Matrix.fromList(input)

        val hidden = inputMatrix.dot(weights_input_hidden)
        val hiddenWithBias = hidden.add(biases_hidden)
        val hiddenAfterActivation = hiddenWithBias.map { number -> activation(number) }

        val output = hiddenAfterActivation.dot(weights_hidden_output)
        val outputWithBias = output add biases_output
        val outputAfterActivation = outputWithBias.map { number -> activation(number) }

        return outputAfterActivation.data.first().first()
    }
}
