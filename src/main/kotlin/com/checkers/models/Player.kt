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

    fun pickBoard(boardsAmount: Int): Int {
        return (0 until boardsAmount).random()
    }

    fun playTurn(game: Game) {
        val movesTree = MovesTree(this, game, 3)
        val possibleSteps = movesTree.getLeadingStepsAndFinalBoards()
        if (possibleSteps.isEmpty()) {
            game.isOver = true
            game.winner = game.getOppositePlayer(this)
            return
        }
        val chosenIndex = pickBoard(possibleSteps.size)
        game.board = possibleSteps.get(chosenIndex).first.resultBoard
    }

    class Computer(override val direction: PlayerDirection) : Player {
        override fun playTurn(game: Game) {
            super.playTurn(game)
        }
    }

    class Human(override val direction: PlayerDirection) : Player {
        override fun playTurn(game: Game) {
            val possibleSteps = game.board.getCoordinatesOfPlayer(this)
                    .map { StepSequence(game.board, listOf(it)) }
                    .map { it.getPossibleTurnsForPiece() }.flatten()
            if (possibleSteps.isEmpty()) {
                game.isOver = true
                game.winner = game.getOppositePlayer(this)
                return
            }
            println("insert the number of the option you choose:")
            possibleSteps.forEachIndexed { index, stepSequence -> println("${index+1}) ${stepSequence.stringStepTrace()}") }
            val read = Scanner(System.`in`)
            val chosenIndex = read.nextInt()-1
            game.board = possibleSteps.get(chosenIndex).resultBoard
        }
    }
}