package com.checkers.models.player

import com.checkers.models.Board
import com.checkers.utlis.initOnce

class Player(val type: PlayerType, val playerDirection: PlayerDirection) {

    var oppositePlayer: Player by initOnce()

    val name: String?
        get() = ((type as? Computer)?.picker as? AIPicker)?.brain?.name

    fun playTurn(board: Board): Board? {
        return when (type) {
            is Computer -> type.playTurn(board, this)
            is Human -> null
        }
    }
}