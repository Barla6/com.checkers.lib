package com.checkers.neuroEvolution

import com.checkers.models.AIPlayer
import com.checkers.models.Board
import com.checkers.models.Game
import com.checkers.models.Player
import com.checkers.utlis.initOnce
import kotlin.math.exp

class NeuralNetwork(
    val input_nodes: Int,
    val hidden_nodes: Int,
    val output_nodes: Int
) {

    var name: String by initOnce()

    private var weightsInputHidden: Matrix by initOnce()
    private var weightsHiddenOutput: Matrix by initOnce()

    private var biasesHidden: Matrix by initOnce()
    private var biasesOutput: Matrix by initOnce()

    var fitness: Double = 0.0

    fun updateFitness(game: Game) {
        val thisPlayer by lazy { listOf(game.player1, game.player2).first { (it as? AIPlayer)?.brain == this } }
        val otherPlayer by lazy { listOf(game.player1, game.player2).first { (it as? AIPlayer)?.brain != this } }
        val thisPlayerPiecesLeft by lazy { game.board.countPiecesOfPlayer(thisPlayer).toDouble() }
        val otherPlayerPiecesLeft by lazy { game.board.countPiecesOfPlayer(otherPlayer).toDouble() }

        when (game.winner as? AIPlayer) {
            null ->
                fitness = fitness.minus(otherPlayerPiecesLeft / 12).plus(thisPlayerPiecesLeft)
            thisPlayer ->
                fitness = fitness.plus(1 - otherPlayerPiecesLeft / 12)
        }
    }

    val dna: DNA
        get() = listOf(
            weightsInputHidden.data.flatten(),
            weightsHiddenOutput.data.flatten(),
            biasesHidden.data.flatten(),
            biasesOutput.data.flatten()
        )

    companion object {
        fun randomNeuralNetwork(input_nodes: Int, hidden_nodes: Int, output_nodes: Int): NeuralNetwork =
            NeuralNetwork(input_nodes, hidden_nodes, output_nodes).apply {
                weightsInputHidden = Matrix.randomMatrix(input_nodes, hidden_nodes)
                weightsHiddenOutput = Matrix.randomMatrix(hidden_nodes, output_nodes)
                biasesHidden = Matrix.randomMatrix(cols = hidden_nodes)
                biasesOutput = Matrix.randomMatrix(cols = output_nodes)
            }

        fun fromDNA(input_nodes: Int, hidden_nodes: Int, output_nodes: Int, dna: DNA): NeuralNetwork =
            NeuralNetwork(input_nodes, hidden_nodes, output_nodes).apply {
                weightsInputHidden = Matrix.fromList(input_nodes, hidden_nodes, dna[0])
                weightsHiddenOutput = Matrix.fromList(hidden_nodes, output_nodes, dna[1])
                biasesHidden = Matrix.fromList(cols = hidden_nodes, list = dna[2])
                biasesOutput = Matrix.fromList(cols = output_nodes, list = dna[3])
            }
    }

    // activation function: sigmoid
    private fun activation(x: Double): Double = 1 / (1 + exp(-x))

    fun rate(inputBoard: Board, player: Player): Double {
        return this.rate(inputBoard.toNeuralNetworkInput(player))
    }

    private fun rate(input: List<Double>): Double {
        if (input.size != input_nodes) throw Throwable("input size to NN is not ok")
        val inputMatrix = Matrix.fromList(cols = input_nodes, list = input)

        val hidden = inputMatrix.dot(weightsInputHidden)
        val hiddenWithBias = hidden.add(biasesHidden)
        val hiddenAfterActivation = hiddenWithBias.map { number -> activation(number) }

        val output = hiddenAfterActivation.dot(weightsHiddenOutput)
        val outputWithBias = output add biasesOutput
        val outputAfterActivation = outputWithBias.map { number -> activation(number) }

        return outputAfterActivation.data.first().first()
    }
}
