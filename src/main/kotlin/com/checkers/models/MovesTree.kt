package com.checkers.models

import com.checkers.models.player.Player

data class MovesTree(val stepSequence: StepSequence? = null) {
    private var nextSteps: List<MovesTree>? = null

    constructor(startPlayer: Player,board: Board, depth: Int = 1) : this() {
        nextSteps = getAllPossibleNextMoves(startPlayer, board, depth)
    }

    private fun getAllPossibleNextMoves(player: Player, board: Board, depth: Int): List<MovesTree>? {
        if (depth == 0 ||
                board.countPiecesOfPlayer(player) == 0 ||
                board.countPiecesOfPlayer(player.oppositePlayer) == 0) return null

        return board.getCoordinatesOfPlayer(player)
                .map { StepSequence(board.clone(), listOf(it)) }
                .flatMap { it.getPossibleTurnsForPiece() }
                .map(::MovesTree)
                .onEach {
                    it.nextSteps = getAllPossibleNextMoves(
                        player.oppositePlayer,
                        it.stepSequence!!.resultBoard,
                        depth - 1)
                }
    }

    /**
     * returns pairs of first leading stepSequence and final board
     */
    fun getLeadingStepsAndFinalBoards(): List<Pair<StepSequence, Board>> {
        return nextSteps?.map { movesTree -> movesTree.getFinalBoards().map { Pair(movesTree.stepSequence!!, it) } }?.flatten()
                ?: listOf()
    }

    private fun getFinalBoards(): List<Board> =
            nextSteps?.map { it.getFinalBoards() }?.flatten()
                    ?: if (stepSequence?.resultBoard == null) listOf()
                    else listOf(stepSequence.resultBoard)
}