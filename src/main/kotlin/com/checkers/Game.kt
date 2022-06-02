package com.checkers

import com.checkers.models.*

class Game {
    var board: Board = Board()

//    fun makeTurn(): Pair<Move, Board> {
//
//        return Pair(Move(), board)
//    }

    private fun getFinalBoards(movesTree: MovesTree, startMove: Move? = null): MutableList<MoveWithResultBoard> {

        if (movesTree.nextPossibleMoves.size == 0) {
            return mutableListOf(MoveWithResultBoard(startMove!!, movesTree.currentMove!!.resultBoard))
        }
        val finalBoards = mutableListOf<MoveWithResultBoard>()

        movesTree.nextPossibleMoves.forEach { nextMove ->
            val startMove = startMove ?: nextMove.currentMove?.move
            finalBoards.addAll(getFinalBoards(nextMove, startMove))
        }
        return finalBoards
    }
}
