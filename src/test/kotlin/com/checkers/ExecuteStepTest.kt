package com.checkers

import com.checkers.models.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ExecuteStepTest {

    /**
     * moving piece one step to the crowning row.
     * expectation: the piece will move and become a king
     */
    @Test
    fun executeStep_becomesKing() {
        val movingPiece = Piece(Player.PLAYER)
        val startCoordinate = Coordinates(1, 4)
        val endCoordinate = Coordinates(movingPiece.player.crowningRow, 3)

        val startingBoard = Board.emptyBoard()
        startingBoard.placePiece(movingPiece, startCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board.emptyBoard()
        expected.placePiece(movingPiece.apply { this.type = PieceType.KING }, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    /**
     * moving piece one step to another place.
     * expectation: the piece will move
     */
    @Test
    fun executeStep_simple() {
        val movingPiece = Piece(Player.PLAYER)
        val startCoordinate = Coordinates(5, 2)
        val endCoordinate = Coordinates(4, 3)

        val startingBoard = Board.emptyBoard()
        startingBoard.placePiece(movingPiece, startCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board.emptyBoard()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    /**
     * moving piece in order to eat another.
     * expectation: the piece will move two steps and the eaten piece will be removed
     */
    @Test
    fun executeStep_eating() {
        val movingPiece = Piece(Player.PLAYER)
        val pieceToEat = Piece(Player.COMPUTER)
        val startCoordinate = Coordinates(5, 2)
        val eatCoordinate = Coordinates(4, 3)
        val endCoordinate = Coordinates(3, 4)

        val startingBoard = Board.emptyBoard()
        startingBoard.placePiece(movingPiece, startCoordinate)
        startingBoard.placePiece(pieceToEat, eatCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board.emptyBoard()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    /**
     * moving piece in order to eat another.
     * expectation: the piece will move two steps and the eaten piece will be removed
     */
    @Test
    fun executeStep_becomesKingWhileEating() {

        val movingPiece = Piece(Player.PLAYER)
        val pieceToEat = Piece(Player.COMPUTER)
        val startCoordinate = Coordinates(2, 6)
        val pieceToEatCoordinate = Coordinates(1, 5)
        val endCoordinate = Coordinates(movingPiece.player.crowningRow, 4)

        val startingBoard = Board.emptyBoard()
        startingBoard.placePiece(movingPiece, startCoordinate)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board.emptyBoard()
        val movedPiece = Piece(movingPiece.player, PieceType.KING)
        expected.placePiece(movedPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    @Test
    fun executeStep_kingSimple() {
        val movingPiece = Piece(Player.PLAYER, PieceType.KING)
        val startCoordinate = Coordinates(6, 3)
        val endCoordinate = Coordinates(3, 0)

        val startingBoard = Board.emptyBoard()
        startingBoard.placePiece(movingPiece, startCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board.emptyBoard()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    @Test
    fun executeStep_kingEating() {
        val movingPiece = Piece(Player.PLAYER, PieceType.KING)
        val startCoordinate = Coordinates(6, 3)
        val pieceToEat = Piece(Player.COMPUTER)
        val pieceToEatCoordinates = Coordinates(4, 1)
        val endCoordinate = Coordinates(3, 0)

        val startingBoard = Board.emptyBoard()
        startingBoard.placePiece(movingPiece, startCoordinate)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board.emptyBoard()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }
}