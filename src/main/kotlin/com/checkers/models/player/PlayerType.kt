package com.checkers.models.player

import com.checkers.models.Board
import com.checkers.models.MovesTree

sealed interface PlayerType {
}

class Human : PlayerType {
}

class Computer(val picker: BoardPicker) : PlayerType {

    fun playTurn(board: Board, player: Player): Board? {
        val movesTree = MovesTree(player, board, 3)
        val leadingStepsAndFinalBoards = movesTree.getLeadingStepsAndFinalBoards()
        if (leadingStepsAndFinalBoards.isEmpty()) return null
        val bestBoard = picker.pick(leadingStepsAndFinalBoards.map { it.second }, player)
        return leadingStepsAndFinalBoards.findLast { it.second == bestBoard }!!.first.resultBoard
    }
}
