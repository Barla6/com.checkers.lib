package com.checkers

import com.checkers.neuroEvolution.DNA
import com.checkers.models.AIPlayer
import com.checkers.models.Game
import com.checkers.models.HumanPlayer
import com.checkers.models.MovesTree
import com.checkers.neuroEvolution.Evolution
import com.checkers.neuroEvolution.NeuralNetwork
import kotlinx.coroutines.runBlocking
import java.io.FileInputStream
import java.io.ObjectInputStream
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    Evolution().draw()

//    val brain = NeuralNetwork.randomNeuralNetwork(32, 16, 1)
//    brain.name = "bob"
//    val AIPlayer = AIPlayer(brain)
//    val brain2 = NeuralNetwork.randomNeuralNetwork(32, 16, 1)
//    brain2.name = "alice"
//    val AIPlayer2 = AIPlayer(brain)
//    val game = Game(AIPlayer, AIPlayer2)
//
//    var mt: MovesTree
//    val t = measureTimeMillis {
//        mt = MovesTree(AIPlayer, game.board, 3)
//    }
//
//    var asyncmt: MovesTree
//    val at = measureTimeMillis {
//        asyncmt = MovesTree.create(AIPlayer, game.board, 3)
//    }
//
//    println("equal? ${mt == asyncmt}")
//    println("sync $t")
//    println("async $at")

//    val brain = NeuralNetwork.randomNeuralNetwork(32, 16, 1)
//    brain.name = "BOB"
//    val aiPlayer = AIPlayer(brain)
//
//    val human = HumanPlayer("Bar")
//
//    val game = Game(aiPlayer, human)
//
//    GameRunner.runGame(game)
}