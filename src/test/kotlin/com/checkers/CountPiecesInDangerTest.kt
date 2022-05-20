package com.checkers

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CountPiecesInDangerTest {

    @Test
    fun countPiecesInDanger_withPiecesInDanger() {
        val board = Board()
        board.movePiece(Coordinates(5, 2), Coordinates(3, 2))
        board.removePiece(Coordinates(6, 5))

        val result = board.countPiecesInDanger(Player.PLAYER)

        val expected = 4

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun countPiecesInDanger_noPiecesInDanger() {
        val board = Board()

        val result = board.countPiecesInDanger(Player.PLAYER)

        val expected = 0

        Assertions.assertEquals(expected, result)
    }
}