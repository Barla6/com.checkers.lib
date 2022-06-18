package com.checkers.models

import com.checkers.neuroEvolution.NeuralNetwork
import java.util.*

sealed interface Player {

    val direction: PlayerDirection

    val directions
        get() = direction.directions

    val crowningRow
        get() = direction.crowningRow

    val startingRows
        get() = direction.startingRows

    fun playTurn(game: Game)

    class Computer(
        override val direction: PlayerDirection,
        public val brain: NeuralNetwork
    ) : Player {
        override fun playTurn(game: Game) {
            // todo: temporary implementation
            val movesTree = MovesTree(this, game, 3)
            val possibleSteps = movesTree.getLeadingStepsAndFinalBoards()

            if (possibleSteps.isEmpty()) {
                game.winner = game.getOppositePlayer(this)
                return
            }
            val chosenIndex = pickBoard(possibleSteps.map { it.second })
            game.board = possibleSteps[chosenIndex].first.resultBoard
        }

        private fun pickBoard(boards: List<Board>): Int {
            val rates = boards.map { brain.predict(it.toNeuralNetworkInput(this))!! }
            return rates.fold(0) { indexOfBest, rate ->
                if (rate > rates[indexOfBest]) rates.indexOf(rate) else indexOfBest
            }
        }
    }

    class Human(override val direction: PlayerDirection) : Player {
        override fun playTurn(game: Game) {
            // todo: temporary implementation
            val movesTree = MovesTree(this, game, 1)
            val possibleSteps = movesTree.getLeadingStepsAndFinalBoards()

            if (possibleSteps.isEmpty()) {
                game.winner = game.getOppositePlayer(this)
                return
            }
            val chosenIndex = pickBoard(possibleSteps.map { it.first })
            game.board = possibleSteps[chosenIndex].first.resultBoard
        }

        private fun pickBoard(possibleSteps: List<StepSequence>): Int {
            println("insert the number of the option you choose:")
            possibleSteps.forEachIndexed { index, stepSequence -> println("${index + 1}) ${stepSequence.stringStepTrace()}") }
            val read = Scanner(System.`in`)
            return read.nextInt() - 1
        }
    }
}