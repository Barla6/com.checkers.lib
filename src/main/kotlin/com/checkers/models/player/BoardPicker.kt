package com.checkers.models.player

import com.checkers.models.Board
import com.checkers.neuroEvolution.NeuralNetwork

sealed interface BoardPicker {
    fun pick(boards: List<Board>, player: Player): Board
}

class AIPicker(val brain: NeuralNetwork): BoardPicker {
    override fun pick(boards: List<Board>, player: Player): Board {
        require(boards.isNotEmpty())
        val bestBoard = boards
            .mapIndexed { a, b -> a to brain.rate(b, player) }
            .maxByOrNull { it.second }
        return boards[bestBoard!!.first]
    }
}