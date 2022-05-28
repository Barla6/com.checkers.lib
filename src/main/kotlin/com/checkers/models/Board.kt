package com.checkers.models

import com.checkers.Constants

class Board(
    private var board: Array<Array<Piece?>> = Array(Constants.ROWS_NUMBER) { Array(Constants.COLS_NUMBER) { null } }
) : Cloneable {

    fun initGameBoard() {
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

    // initial board for game
    init {
        println("luv u!")
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

    fun movePiece(startCoordinates: Coordinates, endCoordinates: Coordinates) {
        val pieceToMove = removePiece(startCoordinates)!!
        placePiece(pieceToMove, endCoordinates)
        pieceToMove.makeKingIfNeeded(endCoordinates)
    }

    fun removePiece(coordinates: Coordinates): Piece? {
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
        board.map { row ->
            row.mapNotNull { piece ->
                if (piece?.player == player) Coordinates(board.indexOf(row), row.indexOf(piece)) else null
            }
        }.flatten()

    fun isRangeEmpty(startCoordinates: Coordinates, endCoordinates: Coordinates): Boolean {
        val coordinatesRange = Coordinates.range(startCoordinates, endCoordinates)
        return coordinatesRange.all { this.getPieceByCoordinates(it) == null }
    }

    fun getPieceByMove(move: Move): Piece {
        return getPieceByCoordinates(move.getLastPlace())!!
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
    private fun countOnBoard(condition: (piece: Piece?) -> Boolean): Int =
        board.sumOf { row ->
            row.count { piece -> condition(piece) }
        }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }

    companion object {
        fun emptyBoard(): Board {
            val emptyBoard = Board()
            emptyBoard.board = Array(Constants.ROWS_NUMBER) { Array(Constants.COLS_NUMBER) { null } }
            return emptyBoard
        }
    }
}
