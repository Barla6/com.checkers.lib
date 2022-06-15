package com.checkers

import com.checkers.models.Game
import com.checkers.neuroEvolution.Matrix
import com.checkers.models.MovesTree
import com.checkers.models.Player
import com.checkers.models.PlayerDirection
import com.checkers.neuroEvolution.NeuralNetwork
import kotlin.random.Random

fun main() {

//    val game = Game(Player.Computer(PlayerDirection.DOWNWARDS), Player.Computer(PlayerDirection.UPWARDS))
//
//    game.board.printBoard()

//    game.startGame()

//    val matrix = Matrix.randomMatrix(3, 2)
//    matrix.print()
//
//    val other = Matrix.randomMatrix(2, 3)
//    other.print()
//
//    val result = matrix dot other
//    result!!.print()

    val brain = NeuralNetwork()
    val input = List<Double>(NeuralNetwork.INPUT_NODES) {Random.nextDouble(1.0)}
    brain.predict(input)
}