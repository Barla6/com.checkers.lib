package com.checkers

import com.checkers.models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CanStepTest {

    private lateinit var friendlyKing: Piece
    private lateinit var friendlyPiece: Piece
    private lateinit var enemyPiece: Piece
    private lateinit var stepSequence: StepSequence
    private lateinit var spyStepSequence : StepSequence

    @BeforeEach
    fun init() {
        friendlyPiece = Piece(Player.PLAYER)
        friendlyKing = Piece(Player.PLAYER, PieceType.KING)
        enemyPiece = Piece(Player.COMPUTER)
        stepSequence = StepSequence(Board(), listOf())
        spyStepSequence = Mockito.spy(stepSequence)
    }

    /**
     * check if regular piece can step to empty place.
     * EXPECTATION: yes
     */
    @Test
    fun canStep_regularPiece_yes() {
        val thisPiece = friendlyPiece.clone()
        val thisPieceCoordinates = Coordinates(1, 3)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT)

        assertTrue(result)
    }

    /**
     * check if regular piece can step to place taken by enemy piece.
     * EXPECTATION: no
     */
    @Test
    fun canStep_regularPiece_placeIsTakenByEnemy_no() {
        val thisPiece = friendlyPiece.clone()
        val enemyPiece = enemyPiece.clone()
        val thisPieceCoordinates = Coordinates(1, 3)
        val targetCoordinates = Coordinates(2, 4)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(enemyPiece, targetCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT)

        assertFalse(result)
    }

    /**
     * check if regular piece can step to place taken by friendly piece.
     * EXPECTATION: no
     */
    @Test
    fun canStep_regularPiece_placeIsTakenByFriend_no() {
        val thisPiece = friendlyPiece.clone()
        val friendlyPiece = friendlyPiece.clone()
        val thisPieceCoordinates = Coordinates(1, 3)
        val targetCoordinates = Coordinates(2, 4)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(friendlyPiece, targetCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT)

        assertFalse(result)
    }

    /**
     * check if regular piece can step in direction that leads outside the board.
     * EXPECTATION: no
     */
    @Test
    fun canStep_regularPiece_placeIsOutOfBoard_no() {
        val thisPiece = friendlyPiece.clone()
        val thisPieceCoordinates = Coordinates(0, 2)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.UP_RIGHT)

        assertFalse(result)
    }

    /**
     * check if king piece can step to empty place next to it.
     * EXPECTATION: yes
     */
    @Test
    fun canStep_king_yes() {
        val thisPiece = friendlyKing.clone()
        val thisPieceCoordinates = Coordinates(0, 2)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT)

        assertTrue(result)
    }

    /**
     * check if king piece can step to empty place distant from it.
     * EXPECTATION: yes
     */
    @Test
    fun canStep_king_distantStep_yes() {
        val thisPiece = friendlyKing.clone()
        val thisPieceCoordinates = Coordinates(0, 2)
        val targetCoordinates = Coordinates(3, 5)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT, targetCoordinates)

        assertTrue(result)
    }

    /**
     * check if king piece can step to place taken by enemy piece.
     * EXPECTATION: no
     */
    @Test
    fun canStep_king_placeIsTakenByEnemy_no() {
        val thisPiece = friendlyKing.clone()
        val enemyPiece = enemyPiece.clone()
        val thisPieceCoordinates = Coordinates(0, 2)
        val targetCoordinates = Coordinates(3, 5)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(enemyPiece, targetCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT, targetCoordinates)

        assertFalse(result)
    }

    /**
     * check if king piece can step to place taken by friendly piece.
     * EXPECTATION: no
     */
    @Test
    fun canStep_king_placeIsTakenByFriend_no() {
        val thisPiece = friendlyKing.clone()
        val friendlyPiece = friendlyPiece.clone()
        val thisPieceCoordinates = Coordinates(0, 2)
        val targetCoordinates = Coordinates(3, 5)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(friendlyPiece, targetCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT, targetCoordinates)

        assertFalse(result)
    }

    /**
     * check if king piece can step to empty place when there are pieces in the way.
     * EXPECTATION: no
     */
    @Test
    fun canStep_king_piecesInTheWay_no() {
        val thisPiece = friendlyKing.clone()
        val pieceInTheWay = friendlyPiece.clone()
        val thisPieceCoordinates = Coordinates(0, 2)
        val targetCoordinates = Coordinates(3, 5)
        val pieceInTheWayCoordinates = Coordinates(2, 4)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)
        board.placePiece(pieceInTheWay, pieceInTheWayCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.DOWN_RIGHT, targetCoordinates)

        assertFalse(result)
    }

    /**
     * check if king piece can step in direction that leads outside the board.
     * EXPECTATION: no
     */
    @Test
    fun canStep_king_placeIsOutOfBoard_no() {
        val thisPiece = friendlyKing.clone()
        val thisPieceCoordinates = Coordinates(0, 2)

        val board = Board()
        board.placePiece(thisPiece, thisPieceCoordinates)

        Mockito.doReturn(thisPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canStep(StepDirection.UP_RIGHT)

        assertFalse(result)
    }
}