package com.checkers

import com.checkers.models.Board
import com.checkers.models.Coordinates
import com.checkers.models.Move
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GetPossibleMovesForPieceTest {

    @Test
    fun getPossibleMovesForPiece_becomesKing() {
        val board = Board()
        board.removePiece(Coordinates(5, 0))
        board.removePiece(Coordinates(5, 2))
        board.removePiece(Coordinates(6, 1))
        board.removePiece(Coordinates(7, 2))
        board.movePiece(Coordinates(2, 1), Coordinates(6, 1))

        val result = board.getPossibleMovesForPiece(Coordinates(6, 1))

        val expected = mutableListOf(
            Move(
                mutableListOf(
                    Coordinates(6, 1),
                    Coordinates(7, 2)
                )
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun getPossibleMovesForPiece_becomesKingWithEatingOnce() {
        val board = Board()
        board.movePiece(Coordinates(2, 1), Coordinates(5, 4))
        board.removePiece(Coordinates(5, 2))
        board.removePiece(Coordinates(7, 2))

        val result = board.getPossibleMovesForPiece(Coordinates(5, 4))

        val expected = mutableListOf(
            Move(
                mutableListOf(
                    Coordinates(5, 4),
                    Coordinates(7, 2)
                )
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun getPossibleMovesForPiece_becomesKingWithEatingTwice() {
        val board = Board()
        board.movePiece(Coordinates(2, 1), Coordinates(5, 4))
        board.removePiece(Coordinates(5, 2))
        board.removePiece(Coordinates(7, 2))
        board.removePiece(Coordinates(5, 0))

        val result = board.getPossibleMovesForPiece(Coordinates(5, 4))

        val expected = mutableListOf(
            Move(
                mutableListOf(
                    Coordinates(5, 4),
                    Coordinates(7, 2)
                )
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun getPossibleMovesForPiece_eatingTwice() {
        val board = Board()
        board.movePiece(Coordinates(5, 2), Coordinates(3, 2))
        board.removePiece(Coordinates(6, 5))

        val result = board.getPossibleMovesForPiece(Coordinates(2, 1))

        val expected = mutableListOf(
            Move(
                mutableListOf(
                    Coordinates(2, 1),
                    Coordinates(3, 0)
                )
            ),
            Move(
                mutableListOf(
                    Coordinates(2, 1),
                    Coordinates(4, 3)
                )
            ),
            Move(
                mutableListOf(
                    Coordinates(2, 1),
                    Coordinates(4, 3),
                    Coordinates(6, 5)
                )
            ),
            Move(
                mutableListOf(
                    Coordinates(2, 1),
                    Coordinates(4, 3),
                    Coordinates(6, 5),
                    Coordinates(4, 7)
                )
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun getPossibleMovesForPiece_eatingOnce() {
        val board = Board()
        board.movePiece(Coordinates(5, 2), Coordinates(3, 2))
        board.removePiece(Coordinates(6, 5))

        val result = board.getPossibleMovesForPiece(Coordinates(2, 3))

        val expected = mutableListOf(
            Move(
                mutableListOf(
                    Coordinates(2, 3),
                    Coordinates(4, 1)
                )
            ),
            Move(
                mutableListOf(
                    Coordinates(2, 3),
                    Coordinates(3, 4)
                )
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun getPossibleMovesForPiece_noSpecialEvents() {
        val board = Board()

        val result = board.getPossibleMovesForPiece(Coordinates(2, 1))

        val expected = mutableListOf(
            Move(
                mutableListOf(
                    Coordinates(2, 1),
                    Coordinates(3, 0)
                )
            ),
            Move(
                mutableListOf(
                    Coordinates(2, 1),
                    Coordinates(3, 2)
                )
            )
        )

        assertEquals(expected, result)
    }
}