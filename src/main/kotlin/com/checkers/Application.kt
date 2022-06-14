package com.checkers

import com.checkers.models.Game
import com.checkers.models.MovesTree
import com.checkers.models.Player
import com.checkers.models.PlayerDirection

fun main() {

    val game = Game(Player.Computer(PlayerDirection.DOWNWARDS), Player.Human(PlayerDirection.UPWARDS))

    game.board.printBoard()

    game.startGame()
}