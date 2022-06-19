package com.checkers.models.player

import com.checkers.utlis.initOnce

class Player(val type: PlayerType, val playerDirection: PlayerDirection) {

    var oppositePlayer: Player by initOnce()

    fun playTurn() {

    }
}