package com.checkers.models

import com.checkers.models.player.Player
import com.checkers.models.player.PlayerDirection
import com.checkers.models.player.PlayerType

class Game(playerType1: PlayerType, playerType2: PlayerType) {

    var board: Board = Board()
    private var turnCounter = 0
    private val player1: Player
    private val player2: Player
    var winner: Player? = null

    private val isOver: Boolean
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

    private fun getRandomPlayer() = listOf(player1, player2).random()

    fun runGame() {
        var player = getRandomPlayer()
        while (!isOver) {
            player.playTurn()
            turnCounter++
            board.printBoard()
            player = player.oppositePlayer
        }
        println("game over")
        println("winner: ${winner?.playerDirection ?: "tie"}")
        println("numberOfTurns: $turnCounter")
    }
}
