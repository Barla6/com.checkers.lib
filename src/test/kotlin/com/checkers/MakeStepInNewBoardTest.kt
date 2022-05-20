package com.checkers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MakeStepInNewBoardTest {

    @Test
    fun makeStepInNewBoard_becomesKing() {
        val board = Board()
        board.removePiece(Coordinates(5, 0))
        board.removePiece(Coordinates(5, 2))
        board.removePiece(Coordinates(6, 1))
        board.removePiece(Coordinates(7, 2))
        board.movePiece(Coordinates(2, 1), Coordinates(6, 1))

        val result = board.makeStepInNewBoard(Coordinates(6, 1), Coordinates(7, 2))

        val expected = board.cloneBoard()
        expected.placePiece(expected.removePiece(Coordinates(6, 1)), Coordinates(7, 2))
        expected.getPieceByCoordinates(Coordinates(7, 2))!!.type = PieceType.KING

        assertEquals(expected, result)
    }

    @Test
    fun makeStepInNewBoard_becomesKingWithEating() {
        val board = Board()
        board.removePiece(Coordinates(5, 0))
        board.removePiece(Coordinates(7, 2))
        board.movePiece(Coordinates(2, 1), Coordinates(5, 0))

        val result = board.makeStepInNewBoard(Coordinates(5, 0), Coordinates(7, 2))

        val expected = board.cloneBoard()
        expected.placePiece(expected.removePiece(Coordinates(5, 0)), Coordinates(7, 2))
        expected.removePiece(Coordinates(6, 1))
        expected.getPieceByCoordinates(Coordinates(7, 2))!!.type = PieceType.KING

        assertEquals(expected, result)
    }

    @Test
    fun makeStepInNewBoard_eating() {
        val board = Board()
        board.movePiece(Coordinates(5, 2), Coordinates(3, 2))

        val result = board.makeStepInNewBoard(Coordinates(2, 3), Coordinates(4, 1))

        val expected = board.cloneBoard()
        expected.placePiece(expected.removePiece(Coordinates(2, 3)), Coordinates(4, 1))
        expected.removePiece(Coordinates(3, 2))

        assertEquals(expected, result)
    }

    @Test
    fun makeStepInNewBoard_regularStep() {
        val board = Board()

        val result = board.makeStepInNewBoard(Coordinates(2, 1), Coordinates(3, 2))

        val expected = Board()
        expected.placePiece(expected.removePiece(Coordinates(2, 1)), Coordinates(3, 2))

        assertEquals(expected, result)
    }
}