package com.checkers.models

import com.checkers.Constants
import kotlin.random.Random

class Board(
    private var board: Array<Array<Piece?>> = Array(Constants.ROWS_NUMBER) { Array(Constants.COLS_NUMBER) { null } }
) {

    // initial board for game
    init {
        if (board.all { row -> row.all { piece -> piece == null } }) {
            for (row in 0 until Constants.ROWS_NUMBER) {
                for (col in 0 until Constants.COLS_NUMBER) {

                    if (isSquareBlack(Coordinates(row, col))) {
                        val color = when (row) {
                            0, 1, 2 -> Player.COMPUTER
                            5, 6, 7 -> Player.PLAYER
                            else -> null
                        }

                        if (color != null) {
                            val piece = Piece(color)
                            board[row][col] = piece
                        }
                    }
                }
            }
        }
    }

    fun printBoard() {
        board.forEach { row ->
            println()
            row.forEach { piece ->
                val toPrint = when (piece?.player) {
                    Player.COMPUTER -> if (piece.type == PieceType.KING) "[⬜]" else "[▫]"
                    Player.PLAYER -> if (piece.type == PieceType.KING) "[⬛]" else "[▪]"
                    else -> "[ ]"
                }
                print(toPrint)
            }
        }
        println()
    }

    fun cloneBoard(): Board {
        val bla = Board(board.map { row ->
            row.map { piece ->
                piece?.clonePiece()
            }.toTypedArray()
        }.toTypedArray())
        return bla
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is Board) false
        else board.all { row ->
            row.all { piece ->
                piece == other.board[board.indexOf(row)][row.indexOf(piece)]
            }
        }
    }

    private fun isSquareBlack(coordinates: Coordinates) =
        coordinates.col % 2 == (coordinates.row + 1) % 2

    /**
     * @param - move to make
     * iterates all the steps of the move and play them including eating pieces
     * @return - new Board after the move
     */
    fun makeMoveInNewBoard(move: Move): Board {
        var newBoard = cloneBoard()
        val iterator = move.steps.listIterator()

        for (step in move.steps) {
            if (iterator.hasNext() && iterator.hasPrevious()) {
                val startCoordinates = move.steps[iterator.previousIndex()]
                val endCoordinates = move.steps[iterator.nextIndex()]
                newBoard = newBoard.makeStepInNewBoard(startCoordinates, endCoordinates)
            }
            iterator.next()
        }
        return newBoard
    }

    /**
     * @param - start coordinated and end coordinates
     * moves the piece in the start coordinates to the end coordinates
     * and removes piece in between
     * @return - new Board after the step
     */
    fun makeStepInNewBoard(startCoordinates: Coordinates, endCoordinates: Coordinates): Board {

        val newBoard = cloneBoard()
        newBoard.movePiece(startCoordinates, endCoordinates)

        val stepDirection = StepDirection.getDirection(startCoordinates, endCoordinates)!!

        var currentCoordinate = startCoordinates.step(stepDirection)

        while (currentCoordinate != endCoordinates) {
            if (newBoard.getPieceByCoordinates(currentCoordinate) != null)
                newBoard.removePiece(currentCoordinate)
            currentCoordinate = currentCoordinate.step(stepDirection)
        }

        return newBoard
    }

    fun movePiece(startCoordinates: Coordinates, endCoordinates: Coordinates) {
        val pieceToMove = removePiece(startCoordinates)
        placePiece(pieceToMove, endCoordinates)
        pieceToMove.makeKingIfNeeded(endCoordinates)
    }

    fun removePiece(coordinates: Coordinates): Piece {
        val removedPiece = getPieceByCoordinates(coordinates)
        placePiece(null, coordinates)
        return removedPiece!!
    }

    fun placePiece(piece: Piece?, newCoordinates: Coordinates) {
        board[newCoordinates.row][newCoordinates.col] = piece
    }

    fun getPieceByCoordinates(coordinates: Coordinates): Piece? =
        board[coordinates.row][coordinates.col]

    private fun getCoordinatesOfPlayer(player: Player): MutableList<Coordinates> {
        val coordinates = mutableListOf<Coordinates>()
        board.forEach { row ->
            row.forEach { piece ->
                if (piece?.player == player)
                    coordinates.add(Coordinates(board.indexOf(row), row.indexOf(piece)))
            }
        }
        return coordinates
    }

    private fun countPiecesOfPlayer(player: Player): Int =
        countOnBoard { piece -> piece?.player == player }

    private fun countRegularPiecesOfPlayer(player: Player): Int =
        countOnBoard { piece -> piece?.player == player && piece.type == PieceType.REGULAR }

    private fun countKingsOfPlayer(player: Player): Int =
        countOnBoard { piece -> piece?.player == player && piece.type == PieceType.KING }

    /**
     * @param - function that defines the condition of the count
     * @return - the number counted
     */
    private fun countOnBoard(condition: (piece: Piece?) -> Boolean): Int {
        var counter = 0
        board.forEach { row ->
            row.forEach { piece ->
                if (condition(piece)) {
                    counter++
                }
            }
        }
        return counter
    }

    /*********************************************************************************************
     *******************************  LOGIC FOR MINIMAX ALGORITHM  *******************************
     *********************************************************************************************/

    fun getPossibleMovesForPiece(coordinates: Coordinates): MutableList<Move> {

        val possibleMoves = mutableListOf<Move>()
        val currentPiece = getPieceByCoordinates(coordinates) ?: return possibleMoves

        // get possible directions for piece
        val directions = currentPiece.getDirections()

        possibleMoves.addAll(
            getPossibleMovesInDirections(
                Move(mutableListOf(coordinates)),
                this,
                directions
            )
        )

        return possibleMoves
    }

    // TODO: remove the currentBoard from the parameters and call the function on the cloned board
    private fun getPossibleMovesInDirection(
        currentMove: Move,
        currentBoard: Board,
        stepDirection: StepDirection
    ): MutableList<Move> {

        val possibleMoves = mutableListOf<Move>()
        var currentBoard = currentBoard
        val currentCoordinates = currentMove.getLastPlace()
        val nextCoordinates = currentCoordinates.step(stepDirection)
        if (!nextCoordinates.isValid()) return possibleMoves
        val currentPiece = currentBoard.getPieceByCoordinates(currentCoordinates)

        val pieceInNextCoordinates = currentBoard.getPieceByCoordinates(nextCoordinates)

        // if the next place is empty
        if (pieceInNextCoordinates == null) {

            // if the current piece is king
            if (currentPiece!!.type == PieceType.KING) {
                currentMove.addStep(nextCoordinates)
                currentBoard = currentBoard.makeStepInNewBoard(currentCoordinates, nextCoordinates)

                // only if the king has not eaten yet this move is possible
                if (!currentMove.hasEaten()) {
                    possibleMoves.add(currentMove)
                }

                possibleMoves.addAll(
                    getPossibleMovesInDirections(
                        currentMove,
                        currentBoard,
                        arrayOf(stepDirection)
                    )
                )
            }
            // if the current piece is regular then if it is the first step of the move this move is possible
            if (currentPiece.type == PieceType.REGULAR &&
                currentMove.steps.size == 1
            ) {
                currentMove.addStep(nextCoordinates)
                possibleMoves.add(currentMove)
            }
            // if the next place is taken by the enemy
        } else if (pieceInNextCoordinates.player != currentPiece!!.player) {
            val nextNextCoordinates = nextCoordinates.step(stepDirection)
            // and the place after it is empty
            if (nextNextCoordinates.isValid() &&
                currentBoard.getPieceByCoordinates(nextNextCoordinates) == null
            ) {
                val pieceTypeBeforeStep = currentPiece.type

                currentMove.addStep(nextNextCoordinates)
                possibleMoves.add(currentMove)
                currentBoard = currentBoard.makeStepInNewBoard(currentCoordinates, nextNextCoordinates)

                // get possible moves for all directions except going back in the opposite direction
                val nextDirectionsList = StepDirection.values().toMutableList()
                val oppositeDirection = stepDirection.getOppositeDirection()
                nextDirectionsList.removeAt(nextDirectionsList.indexOf(oppositeDirection))

                val pieceTypeAfterStep = currentBoard.getPieceByCoordinates(nextNextCoordinates)!!.type

                if (pieceTypeBeforeStep == pieceTypeAfterStep) {
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

        return possibleMoves
    }

    private fun getPossibleMovesInDirections(
        currentMove: Move,
        currentBoard: Board,
        stepDirections: Array<StepDirection>
    ): MutableList<Move> {

        val possibleMoves = mutableListOf<Move>()

        for (direction in stepDirections) {
            possibleMoves.addAll(
                getPossibleMovesInDirection(
                    currentMove.cloneMove(),
                    currentBoard.cloneBoard(),
                    direction
                )
            )
        }

        return possibleMoves
    }

    fun getPossibleMovesForPlayer(player: Player): MutableList<Move> {

        val possibleMoves = mutableListOf<Move>()
        val allPiecesOfPlayer = getCoordinatesOfPlayer(player)

        allPiecesOfPlayer.forEach { coordinate ->
            possibleMoves.addAll(getPossibleMovesForPiece(coordinate))
        }
        return possibleMoves
    }

    fun makeTurn(player: Player): MoveWithResultBoard {
        val possibleMoves = getPossibleMovesForPlayer(player)

        val possibleMovesWithResultBoards = possibleMoves.map { move ->
            MoveWithResultBoard(move, this.makeMoveInNewBoard(move))
        }

        val resultBoards = possibleMovesWithResultBoards.map { it.resultBoard }

        val indexOfBestBoard = findTheIndexOfBestBoard(resultBoards, player)

        return possibleMovesWithResultBoards[indexOfBestBoard]
    }

    fun countPiecesInDanger(player: Player): Int {
        var counter = 0

        val enemyCoordinates = getCoordinatesOfPlayer(player.enemy)
        val playerCoordinates = getCoordinatesOfPlayer(player)

        playerCoordinates.forEach { playerCoordinate ->
            enemyCoordinates.forEach { enemyCoordinate ->
                if (checkIfCanEat(enemyCoordinate, playerCoordinate)) counter++
            }
        }
        return counter
    }

    fun checkIfCanEat(eaterCoordinates: Coordinates, eatenCoordinates: Coordinates): Boolean {
        val eaterPiece = getPieceByCoordinates(eaterCoordinates)
        val eaterDirections = eaterPiece!!.getDirections()

        if (eaterPiece.type == PieceType.REGULAR) {
            eaterDirections.forEach { stepDirection ->
                val placeToEat = eaterCoordinates.step(stepDirection)
                if (placeToEat.isValid() && placeToEat == eatenCoordinates) {
                    val placeAfterEaten = placeToEat.step(stepDirection)
                    if (placeAfterEaten.isValid() && getPieceByCoordinates(placeAfterEaten) == null)
                        return true
                }
            }
        } else {
            eaterDirections.forEach { stepDirection ->
                var nextPlace = eaterCoordinates.step(stepDirection)
                while (nextPlace.isValid()) {
                    val pieceInNextPlace = getPieceByCoordinates(nextPlace)
                    if (pieceInNextPlace?.player == eaterPiece.player) break
                    if (pieceInNextPlace?.player == eaterPiece.player.enemy && nextPlace == eatenCoordinates) {
                        val placeAfterEaten = nextPlace.step(stepDirection)
                        if (placeAfterEaten.isValid() && getPieceByCoordinates(placeAfterEaten) == null)
                            return true
                    }
                    nextPlace = nextPlace.step(stepDirection)
                }
            }
        }
        return false
    }

    // TODO: think of a better way to calculate - only basic calculation for now
    private fun calculatePointsForBoard(player: Player): Int {
        val enemy = player.enemy

        return countRegularPiecesOfPlayer(player) +
                2 * countKingsOfPlayer(player) +
                countPiecesInDanger(enemy) -
                countRegularPiecesOfPlayer(enemy) -
                2 * countKingsOfPlayer(enemy) -
                countPiecesInDanger(player)
    }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }

    companion object {

        fun findTheIndexOfBestBoard(boards: List<Board>, player: Player): Int {

            val indexesOfBestBoards = mutableListOf<Int>()
            var bestPoints = Int.MIN_VALUE

            boards.forEachIndexed { index, board ->
                val pointsForNewBoard = board.calculatePointsForBoard(player)
                if (pointsForNewBoard == bestPoints) {
                    indexesOfBestBoards.add(index)
                } else if (pointsForNewBoard > bestPoints) {
                    indexesOfBestBoards.clear()
                    indexesOfBestBoards.add(index)
                    bestPoints = pointsForNewBoard
                }
            }
            return Random.nextInt(indexesOfBestBoards.size)
        }
    }
}
