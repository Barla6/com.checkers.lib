package com.checkers.neuroEvolution

import com.checkers.models.Game
import com.checkers.models.Player
import com.checkers.models.PlayerDirection

class GamesManager(private val population: Population) {

    fun runGames() {
        population.population.forEach { player ->
            val player1 = Player.Computer(PlayerDirection.UPWARDS, player)
            population.population.forEach { contestant ->
                if (player != contestant) {
                    val player2 = Player.Computer(PlayerDirection.DOWNWARDS, contestant)
                    val game = Game(player1, player2)
                    game.runGame()
                    (game.winner as? Player.Computer)?.brain?.addWinning()
                }
            }
        }
    }

    fun printWinningStats() {
        println("WINNING STATS: ")
        println(" index | winnings ")
        population.population.forEachIndexed { index, neuralNetwork ->
            println("  $index  |  ${neuralNetwork.winningsNumber}  ")
        }
    }
}