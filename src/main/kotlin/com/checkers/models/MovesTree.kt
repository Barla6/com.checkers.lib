package com.checkers.models

data class MovesTree(val stepSequence: StepSequence? = null) {
    private var nextSteps: List<MovesTree>? = null

    constructor(player: Player, game: Game, depth: Int = 1) : this() {
        this.nextSteps = getAllPossibleNextMoves(player, game.board, game, depth)
    }

    private fun getAllPossibleNextMoves(player: Player, board: Board, game: Game, depth: Int): List<MovesTree>? {
        if (depth == 0 ||
                board.countPiecesOfPlayer(player) == 0 ||
                board.countPiecesOfPlayer(game.getOppositePlayer(player)!!) == 0) return null
        val nextMoves = board.getCoordinatesOfPlayer(player)
                .map { StepSequence(board.clone(), listOf(it)) }
                .map { it.getPossibleTurnsForPiece() }
                .flatten()
                .map { MovesTree(it) }

        nextMoves.forEach {
            it.nextSteps = getAllPossibleNextMoves(
                    game.getOppositePlayer(player)!!,
                    it.stepSequence!!.resultBoard,
                    game,
                    depth - 1)
        }

        return nextMoves
    }

    /**
     * returns pairs of first leading stepSequence and final board
     */
    fun getLeadingStepsAndFinalBoards(): List<Pair<StepSequence, Board>> {
        return nextSteps?.map { movesTree -> movesTree.getFinalBoards().map { Pair(movesTree.stepSequence!!, it) } }?.flatten()
                ?: listOf<Pair<StepSequence, Board>>()
    }

    private fun getFinalBoards(): List<Board> =
            nextSteps?.map { it.getFinalBoards() }?.flatten()
                    ?: if (stepSequence?.resultBoard == null) listOf()
                    else listOf(stepSequence.resultBoard)
}