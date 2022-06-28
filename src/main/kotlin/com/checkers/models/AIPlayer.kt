package com.checkers.models

import com.checkers.neuroEvolution.NeuralNetwork

class AIPlayer(val brain: NeuralNetwork): Player() {

    init {
        name = brain.name
    }

    fun playTurn(board: Board): Board? {
        val movesTree = MovesTree(this, board, 3)
        val leadingStepsAndFinalBoards = movesTree.getLeadingStepsAndFinalBoards()
        if (leadingStepsAndFinalBoards.isEmpty()) return null
        val bestBoard = pickBoard(leadingStepsAndFinalBoards.map { it.finalBoard })

        return leadingStepsAndFinalBoards.findLast { it.finalBoard == bestBoard }!!.leadingStep.resultBoard
    }

    private fun pickBoard(boards: List<Board>): Board {
        // todo: run async all the rates
        val bestBoard = boards
            .mapIndexed { a, b -> a to brain.rate(b, this) }
            .maxByOrNull { it.second }
        return boards[bestBoard!!.first]
    }
}