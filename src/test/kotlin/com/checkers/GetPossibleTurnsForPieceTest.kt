package com.checkers

import com.checkers.models.*
import com.checkers.models.PlayerDirection
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetPossibleTurnsForPieceTest {

    private lateinit var friendlyPlayer: Player
    private lateinit var enemyPlayer: Player
    private lateinit var friendlyKing: Piece
    private lateinit var friendlyPiece: Piece
    private lateinit var enemyPiece: Piece
    private lateinit var startingBoard: Board

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
        startingBoard = Board()
    }

    @Test
    suspend fun getPossibleTurnsForPiece_piece_noPossibleTurns() {
        val thisPiece = friendlyPiece
        val blockingPiece = friendlyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingCoordinates = listOf(Coordinates(6, 2), Coordinates(6, 4))

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(blockingPiece, blockingCoordinates[0])
        startingBoard.placePiece(blockingPiece, blockingCoordinates[1])

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf<StepSequence>()

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_piece_oneStepPossible() {
        val thisPiece = friendlyPiece
        val blockingPiece = friendlyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingCoordinates = listOf(Coordinates(6, 2), Coordinates(5, 5))

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(blockingPiece, blockingCoordinates[0])
        startingBoard.placePiece(blockingPiece, blockingCoordinates[1])

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf(
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(6, 4)),
                        eaten = false,
                        completed = true
                )
        )

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_piece_oneEating() {
        val thisPiece = friendlyPiece
        val blockingPiece = friendlyPiece
        val pieceToEat = enemyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingPieceCoordinates = Coordinates(6, 2)
        val pieceToEatCoordinates = Coordinates(6, 4)
        val landingCoordinates = Coordinates(5, 5)

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates)
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates)

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf(
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, landingCoordinates),
                        eaten = true,
                        completed = true
                )
        )

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_piece_moreThanOneEating() {
        val thisPiece = friendlyPiece
        val blockingPiece = friendlyPiece
        val pieceToEat = enemyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingPieceCoordinates = Coordinates(6, 2)
        val pieceToEatCoordinates = listOf(Coordinates(6, 4), Coordinates(4, 4))
        val landingCoordinates = listOf(Coordinates(5, 5), Coordinates(3, 3))

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates[0])
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates[1])

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf(
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, landingCoordinates[0], landingCoordinates[1]),
                        eaten = true,
                        completed = true
                )
        )

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_king_noPossibleMoves() {
        val thisPiece = friendlyKing
        val blockingPiece = friendlyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingPieceCoordinates = listOf(Coordinates(6, 2), Coordinates(6, 4))

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates[0])
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates[1])

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf<StepSequence>()

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_king_oneStep() {
        val thisPiece = friendlyKing
        val blockingPiece = friendlyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingPieceCoordinates = listOf(Coordinates(6, 4), Coordinates(5, 1))

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates[0])
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates[1])

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf(
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(6, 2)),
                        eaten = false,
                        completed = true
                )
        )

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_king_oneDistantStep() {
        val thisPiece = friendlyKing
        val blockingPiece = friendlyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingPieceCoordinates = Coordinates(6, 4)

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates)

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf(
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(6, 2)),
                        eaten = false,
                        completed = true
                ),
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(5, 1)),
                        eaten = false,
                        completed = true
                ),
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(4, 0)),
                        eaten = false,
                        completed = true
                )
        )

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_king_oneEating() {
        val thisPiece = friendlyKing
        val blockingPiece = friendlyPiece
        val pieceToEat = enemyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingPieceCoordinates = Coordinates(6, 4)
        val pieceToEatCoordinates = Coordinates(5, 1)

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates)

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf(
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(6, 2)),
                        eaten = false,
                        completed = true
                ),
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(4, 0)),
                        eaten = true,
                        completed = true
                )
        )

        assertEqualLists(expected, result)
    }

    @Test
    suspend fun getPossibleTurnsForPiece_king_moreThanOneEating() {
        val thisPiece = friendlyKing
        val blockingPiece = friendlyPiece
        val pieceToEat = enemyPiece
        val startingCoordinates = Coordinates(7, 3)
        val blockingPieceCoordinates = Coordinates(6, 4)
        val pieceToEatCoordinates = listOf(Coordinates(5, 1), Coordinates(3, 1))

        startingBoard.placePiece(thisPiece, startingCoordinates)
        startingBoard.placePiece(blockingPiece, blockingPieceCoordinates)
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates[0])
        startingBoard.placePiece(pieceToEat, pieceToEatCoordinates[1])

        val stepSequence = StepSequence(startingBoard, listOf(startingCoordinates))
        val result = stepSequence.getPossibleTurnsForPieceAsync()

        val expected = listOf(
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(6, 2)),
                        eaten = false,
                        completed = true
                ),
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(4, 0), Coordinates(2, 2)),
                        eaten = true,
                        completed = true
                ),
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(4, 0), Coordinates(1, 3)),
                        eaten = true,
                        completed = true
                ),
                StepSequence(
                        startingBoard,
                        listOf(startingCoordinates, Coordinates(4, 0), Coordinates(0, 4)),
                        eaten = true,
                        completed = true
                )
        )

        assertEqualLists(expected, result)
    }
}