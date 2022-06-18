package com.checkers

import com.checkers.models.Game
import com.checkers.neuroEvolution.Matrix
import com.checkers.models.MovesTree
import com.checkers.models.Player
import com.checkers.models.PlayerDirection
import com.checkers.neuroEvolution.Evolution
import com.checkers.neuroEvolution.NeuralNetwork
import kotlin.random.Random

fun main() {

    val brain = NeuralNetwork(32, 16, 1)
    val game = Game(Player.Computer(PlayerDirection.DOWNWARDS, brain), Player.Human(PlayerDirection.UPWARDS))

    game.board.printBoard()

    game.runGame()

//    val matrix = Matrix.randomMatrix(3, 2)
//    matrix.print()
//
//    val other = Matrix.randomMatrix(2, 3)
//    other.print()
//
//    val result = matrix dot other
//    result!!.print()

//    val brain = NeuralNetwork(32, 16, 1)
//    val input = List(32) {Random.nextDouble(1.0)}
//    brain.predict(input)

//    val matrix = Matrix.randomMatrix(3, 2)
//    matrix.print()
//    val rotated = matrix.rotate()
//    rotated.print()

//    Evolution().draw()
}