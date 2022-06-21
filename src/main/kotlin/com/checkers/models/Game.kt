package com.checkers.models

class Game(val player1: Player, val player2: Player) {

    var board: Board = Board()
    var turnCounter = 0
    var winner: Player? = null

    val isOver: Boolean
        get() = winner != null || turnCounter >= MAX_TURNS


    companion object {
        const val MAX_TURNS = 120
    }

    init {
        player1.apply {
            this.oppositePlayer = player2
            this.playerDirection = PlayerDirection.UPWARDS
        }
        player2.apply {
            this.oppositePlayer = player1
            this.playerDirection = PlayerDirection.DOWNWARDS
        }
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

        val computerPlayer = humanPlayer.oppositePlayer as AIPlayer
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
