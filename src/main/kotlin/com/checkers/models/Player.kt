package com.checkers.models

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

    class Computer(override val direction: PlayerDirection) : Player {
        override fun playTurn(game: Game) {
            // todo: temporary implementation
            val movesTree = MovesTree(this, game, 3)
            val possibleSteps = movesTree.getLeadingStepsAndFinalBoards()

            if (possibleSteps.isEmpty()) {
                game.winner = game.getOppositePlayer(this)
                return
            }
            val chosenIndex = pickBoard(possibleSteps.size)
            game.board = possibleSteps.get(chosenIndex).first.resultBoard
        }

        fun pickBoard(boardsAmount: Int): Int {
            return (0 until boardsAmount).random()
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
            game.board = possibleSteps.get(chosenIndex).first.resultBoard
        }

        fun pickBoard(possibleSteps: List<StepSequence>): Int {
            println("insert the number of the option you choose:")
            possibleSteps.forEachIndexed { index, stepSequence -> println("${index+1}) ${stepSequence.stringStepTrace()}") }
            val read = Scanner(System.`in`)
            return read.nextInt()-1
        }
    }
}