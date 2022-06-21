package com.checkers

import com.checkers.models.AIPlayer
import com.checkers.models.Game
import com.checkers.models.HumanPlayer
import com.checkers.neuroEvolution.Evolution
import com.checkers.neuroEvolution.Matrix
import com.checkers.neuroEvolution.NeuralNetwork
import kotlin.random.Random

fun main() {

//    val brain1 = NeuralNetwork.randomNeuralNetwork(32, 16, 1)
//    val game = Game(AIPlayer(brain1), HumanPlayer("Bob"))
//
//    game.board.printBoard()
//
//    GameRunner.runGame(game)
//
//    val matrix = Matrix.randomMatrix(3, 2)
//    matrix.print()
//
//    val other = Matrix.randomMatrix(2, 3)
//    other.print()
//
//    val result = matrix dot other
//    result.print()
//
//    val brain2 = NeuralNetwork(32, 16, 1)
//    val input = List(32) { Random.nextDouble(1.0)}
//    val rate = brain2.rate(input)
//    println(rate)
//
//    Evolution().draw(5)
}