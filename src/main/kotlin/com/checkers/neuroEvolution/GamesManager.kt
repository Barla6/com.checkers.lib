package com.checkers.neuroEvolution

import com.checkers.models.Game
import com.checkers.models.player.AIPicker
import com.checkers.models.player.Computer

class GamesManager(private val population: Population) {

    fun runGames() {
        population.population.forEach { player ->
            val player1 = Computer(AIPicker(player))
            population.population.forEach { contestant ->
                if (player != contestant) {
                    val player2 = Computer(AIPicker(contestant))
                    val game = Game(player1, player2)
                    game.runGame()
                    player.gamesCounter++
                    contestant.gamesCounter++
                    ((game.winner?.type as? Computer)?.picker as? AIPicker)?.brain?.addWinning()
                }
            }
        }
    }

    fun printWinningStats() {
        println("WINNING STATS: ")
        println(" index | winnings | fitness")
        population.population.forEachIndexed { index, neuralNetwork ->
            println("   $index   |     ${neuralNetwork.winningsCount}    |   ${neuralNetwork.fitness}")
        }
    }
}