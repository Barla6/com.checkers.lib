package com.checkers

import com.checkers.models.*
import com.checkers.models.PlayerDirection
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class GetNextPossibleStepsTest {

    private lateinit var friendlyPlayer: Player
    private lateinit var enemyPlayer: Player
    private lateinit var friendlyPiece: Piece
    private lateinit var enemyPiece: Piece
    private lateinit var friendlyKing: Piece
    private lateinit var stepSequence: StepSequence
    private lateinit var spyStepSequence: StepSequence
    private lateinit var board: Board

    @BeforeEach
    fun init() {
        friendlyPlayer = HumanPlayer("friend").apply {
            playerDirection = PlayerDirection.UPWARDS
        }
        enemyPlayer = HumanPlayer("enemy").apply {
            playerDirection = PlayerDirection.DOWNWARDS
        }
        friendlyPlayer.oppositePlayer = enemyPlayer
        enemyPlayer.oppositePlayer = friendlyPlayer
        friendlyPiece = Piece(friendlyPlayer)
        enemyPiece = Piece(enemyPlayer)
        friendlyKing = Piece(friendlyPlayer, PieceType.KING)
        stepSequence = StepSequence(Board(), listOf())
        spyStepSequence = Mockito.spy(stepSequence)
        board = Board()
    }

    @Test
    fun getNextPossibleSteps_regularPiece_noPossibleMoves() {
        val thisPiece = friendlyPiece
        val blockingPiece = friendlyPiece
        val thisPieceCoordinates = Coordinates(5, 0)

        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(blockingPiece, Coordinates(4, 1))
        board.placePiece(blockingPiece, Coordinates(6, 1))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf<StepSequence>()

        assertEqualLists(expected, result)
    }

    @Test
    fun getNextPossibleSteps_regularPiece_canMove() {
        val thisPiece = friendlyPiece
        val enemyPiece = enemyPiece
        val thisPieceCoordinates = Coordinates(5, 0)

        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(enemyPiece, Coordinates(4, 1))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf(
            StepSequence(Board(), listOf(Coordinates(3, 2)), eaten = true, completed = false),
        )

        assertEqualLists(expected, result)
    }

    @Test
    fun getNextPossibleSteps_king_noPossibleMoves() {
        val thisPiece = friendlyKing
        val blockingPiece = friendlyPiece
        val thisPieceCoordinates = Coordinates(5, 0)

        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(blockingPiece, Coordinates(4, 1))
        board.placePiece(blockingPiece, Coordinates(6, 1))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf<StepSequence>()

        assertEqualLists(expected, result)
    }

    @Test
    fun getNextPossibleSteps_king_canMove() {
        val thisPiece = friendlyKing
        val enemyPiece = enemyPiece
        val thisPieceCoordinates = Coordinates(5, 0)

        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(enemyPiece, Coordinates(3, 2))

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard

        val result = spyStepSequence.getNextPossibleSteps()

        val expected = listOf(
            StepSequence(Board(), listOf(Coordinates(2, 3)), eaten = true, completed = false),
            StepSequence(Board(), listOf(Coordinates(1, 4)), eaten = true, completed = false),
            StepSequence(Board(), listOf(Coordinates(0, 5)), eaten = true, completed = false),
            StepSequence(Board(), listOf(Coordinates(4, 1)), eaten = false, completed = true),
            StepSequence(Board(), listOf(Coordinates(6, 1)), eaten = false, completed = true),
            StepSequence(Board(), listOf(Coordinates(7, 2)), eaten = false, completed = true)
        )

        assertEqualLists(expected, result)
    }
}