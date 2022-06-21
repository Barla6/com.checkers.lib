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
            oppositePlayer = player2
            playerDirection = PlayerDirection.UPWARDS
        }
        player2.apply {
            oppositePlayer = player1
            playerDirection = PlayerDirection.DOWNWARDS
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

    fun printGameDetails() {
        if (isOver) {
            println("GAME OVER")
            println("players: ${player1.name} VS ${player2.name}")
            if (winner != null) println("WINNER: ${this.winner?.name}")
            else println("It's a TIE")
            println("turnsCount: ${this.turnCounter}")
        } else {
            println("game in progress...")
        }
    }
}
