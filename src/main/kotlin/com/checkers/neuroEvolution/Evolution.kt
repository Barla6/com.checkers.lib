package com.checkers.neuroEvolution

class Evolution {

    fun draw() {
        val population = Population.generatePopulation(10)
        val gamesManager = GamesManager(population)
        gamesManager.runGames()
        gamesManager.printWinningStats()
    }
}