package com.checkers.models

import com.checkers.Constants

class Board(
    private var board: Array<Array<Piece?>> = Array(Constants.ROWS_NUMBER) { Array(Constants.COLS_NUMBER) { null } }
) : Cloneable {

    fun initGameBoard(player1: Player, player2: Player) {
        if (board.all { row -> row.all { piece -> piece == null } }) {
            for (row in 0 until Constants.ROWS_NUMBER) {
                for (col in 0 until Constants.COLS_NUMBER) {

                    if (isSquareBlack(Coordinates(row, col))) {
                        val player = when {
                            player1.playerDirection.startingRows.contains(row) -> player1
                            player2.playerDirection.startingRows.contains(row) -> player2
                            else -> null
                        }

                        if (player != null) {
                            val piece = Piece(player)
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
            print("|")
            row.forEach { piece ->
                val toPrint = when (piece?.player?.playerDirection) {
                    PlayerDirection.DOWNWARDS -> if (piece.type == PieceType.KING) "O|" else "o|"
                    PlayerDirection.UPWARDS -> if (piece.type == PieceType.KING) "X|" else "x|"
                    else -> " |"
                }
                print(toPrint)
            }
        }
        println()
    }

    public override fun clone(): Board {
        val bla = Board(board.map { row ->
            row.map { piece ->
                piece?.clone()
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

    fun executeStep(startCoordinates: Coordinates, endCoordinates: Coordinates): Board {
        val board = clone()

        board.movePiece(startCoordinates, endCoordinates)
        Coordinates.range(startCoordinates, endCoordinates)
            .dropLast(1).forEach { board.removePiece(it) }

        return board
    }

    private fun movePiece(startCoordinates: Coordinates, endCoordinates: Coordinates) {
        val pieceToMove = removePiece(startCoordinates)!!
        placePiece(pieceToMove, endCoordinates)
        pieceToMove.makeKingIfNeeded(endCoordinates)
    }

    private fun removePiece(coordinates: Coordinates): Piece? {
        val removedPiece = getPieceByCoordinates(coordinates)
        placePiece(null, coordinates)
        return removedPiece
    }

    fun placePiece(piece: Piece?, newCoordinates: Coordinates) {
        board[newCoordinates.row][newCoordinates.col] = piece
    }

    fun getPieceByCoordinates(coordinates: Coordinates): Piece? =
        board[coordinates.row][coordinates.col]

    fun getCoordinatesOfPlayer(player: Player): List<Coordinates> =
        board.mapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, piece ->
                if (piece?.player == player) Coordinates(rowIndex, colIndex) else null
            }
        }.flatten()

    fun isRangeEmpty(startCoordinates: Coordinates, endCoordinates: Coordinates): Boolean {
        val coordinatesRange = Coordinates.range(startCoordinates, endCoordinates)
        return (coordinatesRange.drop(1).dropLast(1)).all { isCoordinateEmpty(it) }
    }

    fun isCoordinateEmpty(coordinates: Coordinates): Boolean = getPieceByCoordinates(coordinates) == null

    fun countPiecesOfPlayer(player: Player): Int =
        countOnBoard { piece -> piece?.player == player }

    private fun countRegularPiecesOfPlayer(player: Player): Int =
        countOnBoard { piece -> piece?.player == player && piece.type == PieceType.REGULAR }

    private fun countKingsOfPlayer(player: Player): Int =
        countOnBoard { piece -> piece?.player == player && piece.type == PieceType.KING }

    /**
     * @param - function that defines the condition of the count
     * @return - the number counted
     */
    private fun countOnBoard(condition: (piece: Piece?) -> Boolean): Int =
        board.sumOf { row ->
            row.count { piece -> condition(piece) }
        }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }

    // todo: maybe move to other place
    fun toNeuralNetworkInput(player: Player): List<Double> {
        return board.mapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, piece ->
                if (!isSquareBlack(Coordinates(rowIndex, colIndex))) return@mapIndexedNotNull null
                when (piece?.player) {
                    player -> when (piece.type) {
                        PieceType.REGULAR -> 1.0
                        PieceType.KING -> 2.0
                    }
                    null -> 0.0
                    else -> when (piece.type) {
                        PieceType.REGULAR -> -1.0
                        PieceType.KING -> -2.0
                    }
                }
            }
        }.flatten()
    }
}
