package com.checkers

import com.checkers.models.*

class Game {
    var board: Board = Board()

    fun makeTurn(): Pair<Move, Board> {
        val movesTree = createMovesTree(3)

        val firstMovesAndFinalBoards = getFinalBoards(movesTree)

        val finalBoards = firstMovesAndFinalBoards.map { it.resultBoard }

        val indexOfBestFinalBoard = Board.findTheIndexOfBestBoard(finalBoards, Player.COMPUTER)

        val bestMove = firstMovesAndFinalBoards[indexOfBestFinalBoard].move
        val resultBoard = board.makeMoveInNewBoard(bestMove)

        board = resultBoard
        return Pair(bestMove, resultBoard)
    }

    private fun createMovesTree(
        depth: Int, movesTree: MovesTree = MovesTree(null)
    )
            : MovesTree {

        if (depth == 1) {
            setNextMoves(movesTree, Player.COMPUTER)
            return movesTree
        }

        setNextMoves(movesTree, Player.COMPUTER)

        movesTree.nextPossibleMoves.forEach { nextMove ->
            setNextMoves(nextMove, Player.PLAYER)
            createMovesTree(depth - 1, nextMove.nextPossibleMoves[0])
        }

        return movesTree
    }

    private fun setNextMoves(movesTree: MovesTree, player: Player) {

        val currentBoard = movesTree.currentMove?.resultBoard ?: this.board

        if (player == Player.COMPUTER) {
            val possibleMoves = currentBoard.getPossibleMovesForPlayer(player)
            possibleMoves.forEach { move ->
                val resultBoard = currentBoard.makeMoveInNewBoard(move)
                movesTree.nextPossibleMoves.add(MovesTree(MoveWithResultBoard(move, resultBoard)))
            }
        } else {
            val moveWithResultBoard = currentBoard.makeTurn(player)
            movesTree.nextPossibleMoves.add(MovesTree(moveWithResultBoard))
        }
    }

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
