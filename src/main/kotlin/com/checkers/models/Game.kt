package com.checkers.models

import com.checkers.models.player.Player
import com.checkers.models.player.PlayerDirection
import com.checkers.models.player.PlayerType

class Game(playerType1: PlayerType, playerType2: PlayerType) {

    var board: Board = Board()
    var turnCounter = 0
    val player1: Player
    val player2: Player
    var winner: Player? = null

    val isOver: Boolean
        get() = winner != null || turnCounter >= MAX_TURNS


    companion object {
        const val MAX_TURNS = 120
    }

    init {
        player1 = Player(playerType1, PlayerDirection.UPWARDS)
        player2 = Player(playerType2, PlayerDirection.DOWNWARDS)
        player1.oppositePlayer = player2
        player2.oppositePlayer = player1
        board.initGameBoard(player1, player2)
    }

    fun getRandomPlayer() = listOf(player1, player2).random()

    fun playTurn(stepSequence: StepSequence, humanPlayer: Player) {
        if (stepSequence.startingBoard != board) throw Throwable("step sequence is not valid")

        board = stepSequence.resultBoard
        board.printBoard()
        turnCounter++

        winner = checkForWinner()

        if (isOver) return

        val computerPlayer = humanPlayer.oppositePlayer
        val turnResult = computerPlayer.playTurn(board)
        if (turnResult == null) {
            winner = humanPlayer
            return
        }
        board = turnResult
        board.printBoard()
        turnCounter++
        winner = checkForWinner()
        if (isOver) return

        if (MovesTree(humanPlayer, board, 1).nextSteps!!.isEmpty()) winner = computerPlayer
    }

    private fun checkForWinner(): Player? {
        return when {
            board.countPiecesOfPlayer(player1) == 0 -> player2
            board.countPiecesOfPlayer(player2) == 0 -> player1
            else -> null
        }
    }
}
