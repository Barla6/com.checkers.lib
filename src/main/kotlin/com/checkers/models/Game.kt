package com.checkers.models

import com.sun.org.apache.xpath.internal.operations.Bool
import kotlin.random.Random

class Game(val player1: Player, val player2: Player) {
    var board: Board = Board()

    init {
        if (!(player1.direction oppositeTo player2.direction))
            throw Throwable("can't create game with 2 players in the same direction")
        board.initGameBoard(player1, player2)
    }

    var winner: Player? = null

    val isOver: Boolean
        get() = winner != null

    fun getOppositePlayer(player: Player): Player? =
        when(player) {
            player1 -> player2
            player2 -> player1
            else -> null
        }

    fun startGame() {
        var numberOfTurns = 0
        var player = getRandomStartingPlayer()
        while(!isOver) {
            player.playTurn(this)
            numberOfTurns++
            board.printBoard()
            player = getOppositePlayer(player)!!
        }
        println("game over")
        println("winner: ${winner!!.direction}")
        println("numberOfTurns: $numberOfTurns")
    }

    fun getRandomStartingPlayer() = if ((0..2).random() % 2 == 0) player1 else player2
}
