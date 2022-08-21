package com.checkers.models

import com.checkers.neuroEvolution.NeuralNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.checkers.utlis.asyncMapIndexed

class AIPlayer(val brain: NeuralNetwork) : Player() {
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        name = brain.name
    }

    suspend fun playTurn(board: Board): Board? {
        val movesTree = MovesTree.create(this, board, 3)
        val leadingStepsAndFinalBoards = movesTree.getLeadingStepsAndFinalBoards()
        if (leadingStepsAndFinalBoards.isEmpty()) return null
        val bestBoard = pickBoardAsync(leadingStepsAndFinalBoards.map { it.finalBoard })

        return leadingStepsAndFinalBoards.findLast { it.finalBoard == bestBoard }!!.leadingStep.resultBoard
    }

    private suspend fun pickBoardAsync(boards: List<Board>): Board {
        val bestBoard = boards
            .asyncMapIndexed(scope) {index, board -> index to brain.rate(board, this@AIPlayer)}
            .maxByOrNull { it.second }
        return boards[bestBoard!!.first]
    }
}
