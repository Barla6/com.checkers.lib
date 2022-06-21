package com.checkers.neuroEvolution

import com.checkers.models.Board
import com.checkers.models.Player
import com.checkers.utlis.initOnce
import kotlin.math.exp

class NeuralNetwork(
    val input_nodes: Int,
    val hidden_nodes: Int,
    val output_nodes: Int
) {

    var name: String by initOnce()

    private var weights_input_hidden: Matrix by initOnce()
    private var weights_hidden_output: Matrix by initOnce()

    private var biases_hidden: Matrix by initOnce()
    private var biases_output: Matrix by initOnce()

    var winningsCount = 0
    fun addWinning() = winningsCount++

    var gamesCounter = 0

    val fitness: Double?
        get() {
            return if (gamesCounter == 0) null
            else winningsCount / gamesCounter.toDouble()
        }

    val DNA: DNA
        get() = listOf(
        weights_input_hidden.data.flatten(),
        weights_hidden_output.data.flatten(),
        biases_hidden.data.flatten(),
        biases_output.data.flatten()
    )

    companion object {
        fun randomNeuralNetwork(input_nodes: Int, hidden_nodes: Int, output_nodes: Int): NeuralNetwork =
            NeuralNetwork(input_nodes, hidden_nodes, output_nodes).apply {
                weights_input_hidden = Matrix.randomMatrix(input_nodes, hidden_nodes)
                weights_hidden_output = Matrix.randomMatrix(hidden_nodes, output_nodes)
                biases_hidden = Matrix.randomMatrix(cols = hidden_nodes)
                biases_output = Matrix.randomMatrix(cols = output_nodes)
            }

        fun fromDNA(input_nodes: Int, hidden_nodes: Int, output_nodes: Int, dna: DNA): NeuralNetwork =
            NeuralNetwork(input_nodes, hidden_nodes, output_nodes).apply {
                weights_input_hidden = Matrix.fromList(input_nodes, hidden_nodes, dna[0])
                weights_hidden_output = Matrix.fromList(hidden_nodes, output_nodes, dna[1])
                biases_hidden = Matrix.fromList(cols = hidden_nodes, list = dna[2])
                biases_output = Matrix.fromList(cols = output_nodes, list = dna[3])
            }
    }

    // activation function: sigmoid
    private fun activation(x: Double): Double = 1 / (1 + exp(-x))

    fun rate(inputBoard: Board, player: Player): Double {
        return this.rate(inputBoard.toNeuralNetworkInput(player))
    }

    fun rate(input: List<Double>): Double {
        if (input.size != input_nodes) throw Throwable("input size to NN is not ok")
        val inputMatrix = Matrix.fromList(cols = input_nodes, list = input)

        val hidden = inputMatrix.dot(weights_input_hidden)
        val hiddenWithBias = hidden.add(biases_hidden)
        val hiddenAfterActivation = hiddenWithBias.map { number -> activation(number) }

        val output = hiddenAfterActivation.dot(weights_hidden_output)
        val outputWithBias = output add biases_output
        val outputAfterActivation = outputWithBias.map { number -> activation(number) }

        return outputAfterActivation.data.first().first()
    }
}
