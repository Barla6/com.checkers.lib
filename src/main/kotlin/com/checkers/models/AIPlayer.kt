package com.checkers.models

import com.checkers.neuroEvolution.NeuralNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class AIPlayer(val brain: NeuralNetwork) : Player() {
    val scope = CoroutineScope(Dispatchers.Default)

    init {
        name = brain.name
    }

    suspend fun playTurn(board: Board): Board? {
        val movesTree = MovesTree(this, board, 3)
        val leadingStepsAndFinalBoards = movesTree.getLeadingStepsAndFinalBoards()
        if (leadingStepsAndFinalBoards.isEmpty()) return null
        val bestBoard = pickBoardAsync(leadingStepsAndFinalBoards.map { it.finalBoard })

        return leadingStepsAndFinalBoards.findLast { it.finalBoard == bestBoard }!!.leadingStep.resultBoard
    }

    private suspend fun pickBoardAsync(boards: List<Board>): Board {
        val bestBoard = boards
            .mapIndexed() { index, board -> scope.async { index to brain.rate(board, this@AIPlayer) } }
            .awaitAll()
            .maxByOrNull { it.second }
        return boards[bestBoard!!.first]
    }
}
