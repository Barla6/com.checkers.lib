package com.checkers.neuroEvolution

import com.checkers.GameRunner
import com.checkers.models.AIPlayer
import com.checkers.models.Game
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.checkers.utlis.ProgressBar

class GameManager(private val population: Population) {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val gamesAmount = population.population.size * (population.population.size - 1)
    private val progressBar = ProgressBar("Gen ${population.generationNumber}", gamesAmount)

    suspend fun runGamesParallel() {
        progressBar.start()
        population.population.flatMap { brain1 ->
            population.population.map { brain2 ->
                scope.async {
                    createAndRunGame(brain1, brain2)
                }
            }
        }
            .awaitAll()
            .filterNotNull()
            .forEach { game ->
                (game.player1 as AIPlayer).brain.updateFitness(game)
                (game.player2 as AIPlayer).brain.updateFitness(game)
            }
    }

    private suspend fun createAndRunGame(brain1: NeuralNetwork, brain2: NeuralNetwork): Game? {

        if (brain1 == brain2) return null
        val player1 = AIPlayer(brain1)
        val player2 = AIPlayer(brain2)
        val game = Game(player1, player2)
        GameRunner.runGame(game)
        progressBar.step()
        return game
    }
}