package com.checkers

import com.checkers.models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CanEatTest {

    private lateinit var friendlyKing: Piece
    private lateinit var friendlyPiece: Piece
    private lateinit var enemyPiece: Piece
    private lateinit var stepSequence: StepSequence
    private lateinit var spyStepSequence: StepSequence

    @BeforeEach
    fun init() {
        friendlyPiece = Piece(Player.PLAYER)
        friendlyKing = Piece(Player.PLAYER, PieceType.KING)
        enemyPiece = Piece(Player.COMPUTER)
        stepSequence = StepSequence(Board(), listOf())
        spyStepSequence = Mockito.spy(stepSequence)
    }

    /**
     * check if regular piece can eat enemy piece next to it.
     * EXPECTATION: yes
     */
    @Test
    fun canEat_regularPiece_yes() {
        val eatingPiece = friendlyPiece.clone()
        val pieceToEat = enemyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 3)
        val pieceToEatCoordinates = Coordinates(2, 4)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT)

        assertTrue(result)
    }

    /**
     * check if regular piece can eat when there is no other piece.
     * EXPECTATION: no
     */
    @Test
    fun canEat_regularPiece_nothingToEat_no() {
        val eatingPiece = friendlyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 3)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT)

        assertFalse(result)
    }

    /**
     * check if regular piece can eat friendly piece next to it.
     * EXPECTATION: no
     */
    @Test
    fun canEat_regularPiece_friendlyPieceToEat_no() {
        val eatingPiece = friendlyPiece.clone()
        val pieceToEat = friendlyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 3)
        val pieceToEatCoordinates = Coordinates(2, 4)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT)

        assertFalse(result)
    }

    /**
     * check if regular piece can eat enemy piece next to it, when the landing place is taken.
     * EXPECTATION: no
     */
    @Test
    fun canEat_regularPiece_landingPlaceTaken_no() {
        val eatingPiece = friendlyPiece.clone()
        val pieceToEat = enemyPiece.clone()
        val pieceInLandingPlace = friendlyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 3)
        val pieceToEatCoordinates = Coordinates(2, 4)
        val landingPlace = Coordinates(3, 5)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)
        board.placePiece(pieceInLandingPlace, landingPlace)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT)

        assertFalse(result)
    }

    /**
     * check if regular piece can eat in direction that leads outside the board.
     * EXPECTATION: no
     */
    @Test
    fun canEat_regularPiece_eatingPlaceOutOfBoard_no() {
        val eatingPiece = friendlyPiece.clone()
        val eatingPieceCoordinates = Coordinates(0, 2)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.UP_RIGHT)

        assertFalse(result)
    }

    /**
     * check if regular piece can eat enemy piece next to it, when the landing place is outside the board.
     * EXPECTATION: no
     */
    @Test
    fun canEat_regularPiece_landingPlaceOutOfBoard_no() {
        val eatingPiece = friendlyPiece.clone()
        val pieceToEat = enemyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)
        val pieceToEatCoordinates = Coordinates(0,1)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.UP_LEFT)

        assertFalse(result)
    }

    /**
     * check if king piece can eat enemy piece next to it.
     * EXPECTATION: yes
     */
    @Test
    fun canEat_king_simpleEating_yes() {
        val eatingPiece = friendlyKing.clone()
        val pieceToEat = enemyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)
        val pieceToEatCoordinates = Coordinates(2,3)
        val landingPlace = Coordinates(3,4)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT, landingPlace)

        assertTrue(result)
    }

    /**
     * check if king piece can eat enemy piece from distant.
     * EXPECTATION: yes
     */
    @Test
    fun canEat_king_distantEating_yes() {
        val eatingPiece = friendlyKing.clone()
        val pieceToEat = enemyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)
        val pieceToEatCoordinates = Coordinates(4,5)
        val landingPlace = Coordinates(6, 7)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT, landingPlace)

        assertTrue(result)
    }

    /**
     * check if king piece can eat when there are no other pieces.
     * EXPECTATION: no
     */
    @Test
    fun canEat_king_nothingToEat_no() {
        val eatingPiece = friendlyKing.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT)

        assertFalse(result)
    }

    /**
     * check if king piece can eat friendly piece.
     * EXPECTATION: no
     */
    @Test
    fun canEat_king_friendlyPieceToEat_no() {
        val eatingPiece = friendlyKing.clone()
        val pieceToEat = friendlyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)
        val pieceToEatCoordinates = Coordinates(2,3)
        val landingPlace = Coordinates(4, 5)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT, landingPlace)

        assertFalse(result)
    }

    /**
     * check if king piece can eat enemy piece when there is a piece in the way.
     * EXPECTATION: no
     */
    @Test
    fun canEat_king_piecesInTheWay_no() {
        val eatingPiece = friendlyKing.clone()
        val pieceToEat = enemyPiece.clone()
        val pieceInTheWay = friendlyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)
        val pieceInTheWayCoordinates = Coordinates(3, 4)
        val pieceToEatCoordinates = Coordinates(4,5)
        val landingPlace = Coordinates(5, 6)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)
        board.placePiece(pieceInTheWay, pieceInTheWayCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT, landingPlace)

        assertFalse(result)
    }

    /**
     * check if king piece can eat enemy piece when there is a piece in the way.
     * EXPECTATION: no
     */
    @Test
    fun canEat_king_landingPlaceTaken_no() {
        val eatingPiece = friendlyKing.clone()
        val pieceToEat = enemyPiece.clone()
        val pieceInLandingPlace = friendlyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)
        val pieceToEatCoordinates = Coordinates(3,4)
        val landingCoordinates = Coordinates(4, 5)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)
        board.placePiece(pieceInLandingPlace, landingCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.DOWN_RIGHT, landingCoordinates)

        assertFalse(result)
    }

    /**
     * check if king piece can eat in direction that leads outside the board.
     * EXPECTATION: no
     */
    @Test
    fun canEat_king_eatingPlaceOutOfBoard_no() {
        val eatingPiece = friendlyKing.clone()
        val eatingPieceCoordinates = Coordinates(0, 1)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.UP_LEFT)

        assertFalse(result)
    }

    /**
     * check if king piece can eat enemy piece when the landing place is outside the board.
     * EXPECTATION: no
     */
    @Test
    fun canEat_king_landingPlaceOutOfBoard_no() {
        val eatingPiece = friendlyKing.clone()
        val pieceToEat = enemyPiece.clone()
        val eatingPieceCoordinates = Coordinates(1, 2)
        val pieceToEatCoordinates = Coordinates(0, 1)

        val board = Board()
        board.placePiece(eatingPiece, eatingPieceCoordinates)
        board.placePiece(pieceToEat, pieceToEatCoordinates)

        Mockito.doReturn(eatingPieceCoordinates).`when`(spyStepSequence).currentCoordinates()
        Mockito.doReturn(board).`when`(spyStepSequence).resultBoard()

        val result = spyStepSequence.canEat(StepDirection.UP_LEFT)

        assertFalse(result)
    }
}