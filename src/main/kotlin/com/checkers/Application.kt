package com.checkers

import com.checkers.models.Game
import com.checkers.models.Player
import com.checkers.models.PlayerDirection

fun main() {

    val game = Game(Player.Computer(PlayerDirection.DOWNWARDS), Player.Computer(PlayerDirection.UPWARDS))

    game.board.printBoard()
}