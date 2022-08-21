package com.checkers

import com.checkers.models.*
import com.checkers.neuroEvolution.Evolution
import com.checkers.neuroEvolution.Matrix
import com.checkers.neuroEvolution.NeuralNetwork
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    Evolution().draw()

//    val brain = NeuralNetwork.randomNeuralNetwork(32, 16, 1)
//    brain.name = "bob"
//    val aiPlayer1 = AIPlayer(brain)
//    val brain2 = NeuralNetwork.randomNeuralNetwork(32, 16, 1)
//    brain2.name = "alice"
//    val aiPlayer2 = AIPlayer(brain)
//    val game = Game(aiPlayer1, aiPlayer2)

//    GameRunner.runGame(game)
}