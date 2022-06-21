package com.checkers.neuroEvolution

import com.checkers.GameRunner
import com.checkers.models.AIPlayer
import com.checkers.models.Game

class GameManager(private val population: Population) {

    fun runGames() {
        // todo: run async all games
        population.population.forEach { brain1 ->
            population.population.forEach { brain2 ->
                if (brain1 != brain2) {
                    val player1 = AIPlayer(brain1)
                    val player2 = AIPlayer(brain2)
                    val game = Game(player1, player2)
                    GameRunner.runGame(game)
                    brain1.gamesCounter++
                    brain2.gamesCounter++
                    game.winner?.let {
                        (it as AIPlayer).brain.winningsCount++
                    }
                }
            }
        }
    }

    fun printWinningStats() {
        println("WINNING STATS: ")
        println("   name   | winnings | fitness")
        population.population.forEach { neuralNetwork ->
            println("   ${neuralNetwork.name}  |     ${neuralNetwork.winningsCount}    |   ${neuralNetwork.fitness}")
        }
    }
}