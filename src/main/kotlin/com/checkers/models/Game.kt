package com.checkers.models

class Game(private val player1: Player, private val player2: Player) {
    var board: Board = Board()

    init {
        if (!(player1.direction oppositeTo player2.direction))
            throw Throwable("can't create game with 2 players in the same direction")
        board.initGameBoard(player1, player2)
    }
}
