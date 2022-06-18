package com.checkers.models

class Game(private val player1: Player, private val player2: Player) {
    var board: Board = Board()
    private var turnCounter = 0

    companion object {
        const val MAX_TURNS = 120
    }

    init {
        if (!(player1.direction oppositeTo player2.direction))
            throw Throwable("can't create game with 2 players in the same direction")
        board.initGameBoard(player1, player2)
    }

    var winner: Player? = null

    private val isOver: Boolean
        get() = winner != null || turnCounter >= MAX_TURNS

    fun getOppositePlayer(player: Player): Player =
        when (player) {
            player1 -> player2
            player2 -> player1
            else -> throw IllegalStateException("????")
        }

    fun runGame() {
        var player = getRandomPlayer()
        while (!isOver) {
            player.playTurn(this)
            turnCounter++
            board.printBoard()
            player = getOppositePlayer(player)
        }
        println("game over")
        println("winner: ${winner?.direction ?: "tie"}")
        println("numberOfTurns: $turnCounter")
    }

    private fun getRandomPlayer() = listOf(player1, player2).random()
}
