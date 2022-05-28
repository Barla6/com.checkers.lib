package com.checkers

import com.checkers.models.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CanEatTest {

    lateinit var friendlyKing: Piece
    lateinit var friendlyPiece: Piece
    lateinit var enemyPiece: Piece
    lateinit var stepSequence: StepSequence
    lateinit var resultBoard: Board

    @BeforeEach
    fun init() {
        friendlyPiece = Piece(Player.PLAYER)
        friendlyKing = Piece(Player.PLAYER, PieceType.KING)
        enemyPiece = Piece(Player.COMPUTER)
            //resultBoard = Mockito.
    }

    @Test
    fun canEat_regularPiece_yes() {
        val board = Board()
        val eatingPiece = friendlyPiece.clone()
        val pieceToEat = enemyPiece.clone()
        board.placePiece(eatingPiece, Coordinates(1, 3))
        board.placePiece(pieceToEat, Coordinates(2, 4))

      //  val result =
    }

    @Test
    fun canEat_regularPiece_nothingToEat_no() {

    }

    @Test
    fun canEat_regularPiece_friendlyPieceToEat_no() {

    }

    @Test
    fun canEat_regularPiece_landingPlaceTaken_no() {

    }

    @Test
    fun canEat_regularPiece_eatingPlaceOutOfBoard_no() {

    }

    @Test
    fun canEat_regularPiece_landingPlaceOutOfBoard_no() {

    }

    @Test
    fun canEat_king_simpleEating_yes() {

    }

    @Test
    fun canEat_king_distantEating_yes() {

    }

    @Test
    fun canEat_king_nothingToEat_no() {

    }

    @Test
    fun canEat_king_friendlyPieceToEat_no() {

    }

    @Test
    fun canEat_king_piecesInTheWay_no() {

    }

    @Test
    fun canEat_king_landingPlaceTaken_no() {

    }

    @Test
    fun canEat_king_eatingPlaceOutOfBoard_no() {

    }

    @Test
    fun canEat_king_landingPlaceOutOfBoard_no() {

    }
}