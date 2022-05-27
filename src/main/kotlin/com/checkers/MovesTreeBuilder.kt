package com.checkers

import com.checkers.models.*

object MovesTreeBuilder {


    fun buildMovesTree(board: Board, depth: Int): MovesTree {

        val movesTree = MovesTree(MoveWithResultBoard(null, board))
        buildMovesTree(movesTree, depth, Player.COMPUTER)
        return movesTree
    }

    /**
     * @param - movesTree (to add children to)
     * @param - depth left
     * @param - current player
     * PURPOSE: recursive function that builds a moves tree of all the possible moves
     * of each player according to depth of turns ahead
     */
    private fun buildMovesTree(movesTree: MovesTree, depth: Int, currentPlayer: Player) {
        if (depth == 0) return

        val currentBoard = movesTree.currentMove.resultBoard

        val possibleMovesForPlayer = getPossibleMovesForPlayer(currentBoard, currentPlayer)

        val nextMovesOnMovesTree = possibleMovesForPlayer.map { move ->
            MovesTree(MoveWithResultBoard(move, currentBoard.makeMoveInNewBoard(move)))
        }.toMutableList()

        movesTree.nextPossibleMoves = nextMovesOnMovesTree

        movesTree.nextPossibleMoves.forEach {
            buildMovesTree(it, depth - 1, currentPlayer.enemy)
        }
    }

    /**
     * @param - currentBoard
     * @param - player to look for all his possible moves
     * @return - mutableList of Move
     */
    private fun getPossibleMovesForPlayer(board: Board, player: Player): MutableList<Move> {

        val possibleMoves = mutableListOf<Move>()
        val allPiecesOfPlayer = board.getCoordinatesOfPlayer(player)

        allPiecesOfPlayer.forEach { coordinate ->
            possibleMoves.addAll(getPossibleMovesForPiece(board, coordinate))
        }
        return possibleMoves
    }

    /**
     * @param - currentBoard
     * @param - coordinates of a piece
     * @return - mutableList of Move
     */
    fun getPossibleMovesForPiece(currentBoard: Board, coordinates: Coordinates): List<Move> {

        val currentPiece = currentBoard.getPieceByCoordinates(coordinates)!!

        // get possible directions for piece
        val directions = currentPiece.getDirections()

        return getPossibleMovesInDirections(
            Move(mutableListOf(coordinates)), //TODO better DS
            currentBoard,
            directions
        )
    }

    /**
     * @param - current move, that contains all the former steps on this turn
     * @param - current board
     * @param - array of all possible direction to check for possible steps
     * @return - mutable list of Move (of all the possible moves from all directions)
     */
    private fun getPossibleMovesInDirections(
        currentMove: Move,
        currentBoard: Board,
        possibleDirections: Array<StepDirection>
    ): List<Move> {
        return possibleDirections.map { direction ->
            getPossibleMovesInDirection(
                currentMove.cloneMove(),
                currentBoard.cloneBoard(),
                direction
            )
        }.flatten()
    }


    /**
     * @param - current move
     * @param - current board
     * @param - step direction
     * according to the current move's last coordinate, knows which piece is playing now
     * @return - mutable list of Move
     */
    private fun getPossibleMovesInDirection(
        currentMove: Move,
        currentBoard: Board,
        stepDirection: StepDirection
    ): List<Move> {

        val list = mutableListOf<Move>()
        val currentCoordinates = currentMove.getLastPlace()
        val currentPiece = currentBoard.getPieceByMove(currentMove)

        val nextCoordinates = currentCoordinates.step(stepDirection).apply {
            if (!this.insideBoard()) return emptyList()
        }

        // if the next coordinate in the direction is outside the board
        // if (!nextCoordinates.outOfBoard()) return emptyList()

        val pieceInNextCoordinates = currentBoard.getPieceByCoordinates(nextCoordinates)

        if (pieceInNextCoordinates != null && pieceInNextCoordinates.player == currentPiece.player) {
            return emptyList()
        }


        // if the next place is empty
        if (pieceInNextCoordinates == null) {

            handleRegularStep(
                currentPiece,
                currentBoard,
                currentMove,
                currentCoordinates,
                nextCoordinates,
                stepDirection,
                list
            )
            // if the next place is taken by the enemy
        }
        else if (pieceInNextCoordinates.player == currentPiece.player.enemy) {
            val nextNextCoordinates = nextCoordinates.step(stepDirection)
            // and the place after it is empty
            if (nextNextCoordinates.insideBoard() &&
                currentBoard.getPieceByCoordinates(nextNextCoordinates) == null
            ) {
                handleEating(
                    currentPiece,
                    currentBoard,
                    currentMove,
                    currentCoordinates,
                    nextNextCoordinates,
                    stepDirection,
                    list
                )
            }
        }
        return list
    }

    private fun handleRegularStep(
        currentPiece: Piece,
        currentBoard: Board,
        currentMove: Move,
        currentCoordinates: Coordinates,
        nextCoordinates: Coordinates,
        stepDirection: StepDirection,
        possibleMoves: MutableList<Move>
    ) {
        when (currentPiece.type) {
            PieceType.KING -> handleRegularStepWithKing(
                currentBoard,
                currentMove,
                currentCoordinates,
                nextCoordinates,
                stepDirection,
                possibleMoves
            )
            PieceType.REGULAR -> handleRegularStepWithRegularPiece(
                currentMove,
                nextCoordinates,
                possibleMoves
            )
        }
    }

    private fun handleRegularStepWithKing(
        currentBoard: Board,
        currentMove: Move,
        currentCoordinates: Coordinates,
        nextCoordinates: Coordinates,
        stepDirection: StepDirection,
        possibleMoves: MutableList<Move>
    ) {
        // add the step to current move
        currentMove.addStep(nextCoordinates)
        // create the new board
        val currentBoard = currentBoard.makeStepInNewBoard(currentCoordinates, nextCoordinates)
        // only if the king has not eaten yet this move is possible
        if (!currentMove.hasEaten()) possibleMoves.add(currentMove)
        // check for possible moves in the same direction
        possibleMoves.addAll(
            getPossibleMovesInDirections(
                currentMove,
                currentBoard,
                arrayOf(stepDirection)
            )
        )
    }

    private fun handleRegularStepWithRegularPiece(
        currentMove: Move,
        nextCoordinates: Coordinates,
        possibleMoves: MutableList<Move>
    ) {
        if (currentMove.steps.size == 1) {
            // add the step to current move
            currentMove.addStep(nextCoordinates)
            // add the move to the possible moves
            possibleMoves.add(currentMove)
        }
    }

    private fun handleEating(
        currentPiece: Piece,
        currentBoard: Board,
        currentMove: Move,
        currentCoordinates: Coordinates,
        nextNextCoordinates: Coordinates,
        stepDirection: StepDirection,
        possibleMoves: MutableList<Move>
    ) {

        val pieceTypeBeforeStep = currentPiece.type

        // add the step to the current move
        currentMove.addStep(nextNextCoordinates)
        // add the move to the possible moves
        possibleMoves.add(currentMove)
        // create the new board
        val currentBoard = currentBoard.makeStepInNewBoard(currentCoordinates, nextNextCoordinates)

        val pieceTypeAfterStep = currentBoard.getPieceByCoordinates(nextNextCoordinates)!!.type

        // only if the piece has not become king then it is possible to continue with the move
        if (pieceTypeBeforeStep == pieceTypeAfterStep) {

            // get possible moves for all directions except going back in the opposite direction
            val nextDirectionsList = StepDirection.values().toMutableList()
            val oppositeDirection = stepDirection.getOppositeDirection()
            nextDirectionsList.removeAt(nextDirectionsList.indexOf(oppositeDirection))

            possibleMoves.addAll(
                getPossibleMovesInDirections(
                    currentMove,
                    currentBoard,
                    nextDirectionsList.toTypedArray()
                )
            )
        }
    }
}
