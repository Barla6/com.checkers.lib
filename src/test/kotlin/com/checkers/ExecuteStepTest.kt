package com.checkers

import com.checkers.models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ExecuteStepTest {

    private lateinit var friendlyPiece: Piece
    private lateinit var enemyPiece: Piece
    private lateinit var friendlyKing:Piece
    private lateinit var startingBoard: Board

    @BeforeEach
    fun init() {
        friendlyPiece = Piece(Player.Human(PlayerDirection.UPWARDS))
        enemyPiece = Piece(Player.Computer(PlayerDirection.DOWNWARDS))
        friendlyKing = Piece(Player.Human(PlayerDirection.UPWARDS), PieceType.KING)
        startingBoard = Board()
    }

    /**
     * moving piece one step to the crowning row.
     * expectation: the piece will move and become a king
     */
    @Test
    fun executeStep_becomesKing() {
        val movingPiece = friendlyPiece
        val startCoordinate = Coordinates(1, 4)
        val endCoordinate = Coordinates(movingPiece.player.crowningRow, 3)

        startingBoard.placePiece(movingPiece, startCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board()
        expected.placePiece(movingPiece.apply { this.type = PieceType.KING }, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    /**
     * moving piece one step to another place.
     * expectation: the piece will move
     */
    @Test
    fun executeStep_simple() {
        val movingPiece = friendlyPiece
        val startCoordinate = Coordinates(5, 2)
        val endCoordinate = Coordinates(4, 3)

        startingBoard.placePiece(movingPiece, startCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    /**
     * moving piece in order to eat another.
     * expectation: the piece will move two steps and the eaten piece will be removed
     */
    @Test
    fun executeStep_eating() {
        val movingPiece = friendlyPiece
        val pieceToEat = enemyPiece
        val startCoordinate = Coordinates(5, 2)
        val eatCoordinate = Coordinates(4, 3)
        val endCoordinate = Coordinates(3, 4)

        startingBoard.placePiece(movingPiece, startCoordinate)
        startingBoard.placePiece(pieceToEat, eatCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    /**
     * moving piece in order to eat another.
     * expectation: the piece will move two steps and the eaten piece will be removed
     */
    @Test
    fun executeStep_becomesKingWhileEating() {

        val movingPiece = friendlyPiece
        val pieceToEat = enemyPiece
        val startCoordinate = Coordinates(2, 6)
        val pieceToEatCoordinate = Coordinates(1, 5)
        val endCoordinate = Coordinates(movingPiece.player.crowningRow, 4)

        startingBoard.placePiece(movingPiece, startCoordinate)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board()
        val movedPiece = Piece(movingPiece.player, PieceType.KING)
        expected.placePiece(movedPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    @Test
    fun executeStep_kingSimple() {
        val movingPiece = friendlyKing
        val startCoordinate = Coordinates(6, 3)
        val endCoordinate = Coordinates(3, 0)

        startingBoard.placePiece(movingPiece, startCoordinate)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }

    @Test
    fun executeStep_kingEating() {
        val movingPiece = friendlyKing
        val pieceToEat = enemyPiece
        val startCoordinate = Coordinates(6, 3)
        val pieceToEatCoordinates = Coordinates(4, 1)
        val endCoordinate = Coordinates(3, 0)

        startingBoard.placePiece(movingPiece, startCoordinate)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates)

        val resultBoard = startingBoard.executeStep(startCoordinate, endCoordinate)

        val expected = Board()
        expected.placePiece(movingPiece, endCoordinate)

        assertEquals(expected, resultBoard)
    }
}