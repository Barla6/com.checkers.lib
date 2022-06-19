package com.checkers.neuroEvolution

class Evolution {

    private var generationsNumber = 0

    fun draw(generations: Int) {
        while (generationsNumber < generations) {
            val population = Population.generatePopulation(5, generationsNumber)
            generationsNumber++
            val gamesManager = GamesManager(population)
            gamesManager.runGames()
            gamesManager.printWinningStats()
        }
    }
}