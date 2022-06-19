package com.checkers

import com.checkers.models.Game
import com.checkers.models.player.AIPicker
import com.checkers.models.player.Computer
import com.checkers.models.player.Human
import com.checkers.neuroEvolution.Evolution
import com.checkers.neuroEvolution.Matrix
import com.checkers.neuroEvolution.NeuralNetwork
import kotlin.random.Random

fun main() {

//    val brain = NeuralNetwork(32, 16, 1)
//    val game = Game(Computer(AIPicker(brain)), Human())
//
//    game.board.printBoard()
//
//    game.runGame()

//    val matrix = Matrix.randomMatrix(3, 2)
//    matrix.print()
//
//    val other = Matrix.randomMatrix(2, 3)
//    other.print()
//
//    val result = matrix dot other
//    result!!.print()

//    val brain = NeuralNetwork(32, 16, 1)
//    val input = List(32) { Random.nextDouble(1.0)}
//    val rate = brain.rate(input)
//    println(rate)

    Evolution().draw()
}