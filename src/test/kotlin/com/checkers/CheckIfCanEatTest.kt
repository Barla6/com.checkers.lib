package com.checkers

import com.checkers.models.Board
import com.checkers.models.Coordinates
import com.checkers.models.PieceType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CheckIfCanEatTest {

    @Test
    fun checkIfCanEat_eaterIsKingAndCloseToEatenPiece() {
        val board = Board()
        board.getPieceByCoordinates(Coordinates(2, 1))!!.type = PieceType.KING
        board.movePiece(Coordinates(5, 4), Coordinates(4, 3))

        val result = board.checkIfCanEat(
            eaterCoordinates = Coordinates(2, 1),
            eatenCoordinates = Coordinates(4, 3)
        )

        val expected = true

        assertEquals(expected, result)
    }

    @Test
    fun checkIfCanEat_eaterIsKingAndDistantFromEatenPiece() {
        val board = Board()
        board.getPieceByCoordinates(Coordinates(2, 1))!!.type = PieceType.KING
        board.movePiece(Coordinates(5, 4), Coordinates(4, 3))

        val result = board.checkIfCanEat(
            eaterCoordinates = Coordinates(2, 1),
            eatenCoordinates = Coordinates(4, 3)
        )

        val expected = true

        assertEquals(expected, result)
    }

    @Test
    fun checkIfCanEat_eaterIsRegularAndCloseToEatenPiece() {
        val board = Board()
        board.movePiece(Coordinates(5, 4), Coordinates(3, 2))

        val result = board.checkIfCanEat(
            eaterCoordinates = Coordinates(2, 1),
            eatenCoordinates = Coordinates(3, 2)
        )

        val expected = true

        assertEquals(expected, result)
    }

    @Test
    fun checkIfCanEat_eaterIsRegularAndDistantToEatenPiece() {
        val board = Board()
        board.removePiece(Coordinates(6, 5))

        val result = board.checkIfCanEat(
            eaterCoordinates = Coordinates(2, 1),
            eatenCoordinates = Coordinates(5, 4)
        )

        val expected = false

        assertEquals(expected, result)
    }

    @Test
    fun checkIfCanEat_eaterAndEatenAreNotInTheSameDirection() {
        val board = Board()

        val result = board.checkIfCanEat(
            eaterCoordinates = Coordinates(2, 1),
            eatenCoordinates = Coordinates(5, 6)
        )

        val expected = false

        assertEquals(expected, result)
    }

    @Test
    fun checkIfCanEat_eaterIsKingAndNextPlaceIsTaken() {
        val board = Board()

        board.getPieceByCoordinates(Coordinates(2, 1))!!.type = PieceType.KING
        val result = board.checkIfCanEat(
            eaterCoordinates = Coordinates(2, 1),
            eatenCoordinates = Coordinates(5, 4)
        )

        val expected = false

        assertEquals(expected, result)
    }

    @Test
    fun checkIfCanEat_eaterIsRegularAndNextPlaceIsTaken() {
        val board = Board()
        board.movePiece(Coordinates(5, 4), Coordinates(3, 2))
        board.movePiece(Coordinates(6, 5), Coordinates(4, 3))

        val result = board.checkIfCanEat(
            eaterCoordinates = Coordinates(2, 1),
            eatenCoordinates = Coordinates(3, 2)
        )

        val expected = false

        assertEquals(expected, result)
    }
}