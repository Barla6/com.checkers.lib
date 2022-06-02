package com.checkers

import com.checkers.models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GetNextPossibleStepsTest {

    private lateinit var friendlyPiece: Piece
    private lateinit var enemyPiece: Piece
    private lateinit var friendlyKing: Piece
    private lateinit var stepSequence: StepSequence
    private lateinit var spyStepSequence: StepSequence

    @BeforeEach
    fun init() {
        friendlyPiece = Piece(Player.PLAYER)
        enemyPiece = Piece(Player.COMPUTER)
        friendlyKing = Piece(Player.PLAYER, PieceType.KING)
        stepSequence = StepSequence(Board(), listOf())
        spyStepSequence = Mockito.spy(stepSequence)
    }

    @Test
    fun getNextPossibleSteps_regularPiece_noPossibleMoves() {
        val thisPiece = friendlyPiece.clone()
        val blockingPiece = friendlyPiece.clone()
        val thisPieceCoordinates = Coordinates(5, 0)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(blockingPiece, Coordinates(4, 1))
        board.placePiece(blockingPiece, Coordinates(6, 1))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf<StepSequence>()

        assertEquals(expected.size, result.size)
        assertTrue(expected.containsAll(result))
        assertTrue(result.containsAll(expected))
    }

    @Test
    fun getNextPossibleSteps_regularPiece_canMove() {
        val thisPiece = friendlyPiece.clone()
        val enemyPiece = enemyPiece.clone()
        val thisPieceCoordinates = Coordinates(5, 0)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(enemyPiece, Coordinates(4, 1))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf(
            StepSequence(Board(), listOf(Coordinates(3, 2)), eaten = true, completed = false),
        )

        assertEquals(expected.size, result.size)
        assertTrue(expected.containsAll(result))
        assertTrue(result.containsAll(expected))
    }

    @Test
    fun getNextPossibleSteps_king_noPossibleMoves() {
        val thisPiece = friendlyKing.clone()
        val blockingPiece = friendlyPiece.clone()
        val thisPieceCoordinates = Coordinates(5, 0)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(blockingPiece, Coordinates(4, 1))
        board.placePiece(blockingPiece, Coordinates(6, 1))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf<StepSequence>()

        assertEquals(expected.size, result.size)
        assertTrue(expected.containsAll(result))
        assertTrue(result.containsAll(expected))
    }

    @Test
    fun getNextPossibleSteps_king_canMove() {
        val thisPiece = friendlyKing.clone()
        val enemyPiece = enemyPiece.clone()
        val thisPieceCoordinates = Coordinates(5, 0)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(enemyPiece, Coordinates(3, 2))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf(
            StepSequence(Board(), listOf(Coordinates(2, 3)), eaten = true, completed = false),
            StepSequence(Board(), listOf(Coordinates(1, 4)), eaten = true, completed = false),
            StepSequence(Board(), listOf(Coordinates(0, 5)), eaten = true, completed = false),
            StepSequence(Board(), listOf(Coordinates(4, 1)), eaten = false, completed = true),
            StepSequence(Board(), listOf(Coordinates(6, 1)), eaten = false, completed = true),
            StepSequence(Board(), listOf(Coordinates(7, 2)), eaten = false, completed = true)
        )

        assertEquals(expected.size, result.size)
        assertTrue(expected.containsAll(result))
        assertTrue(result.containsAll(expected))
    }
}