package com.checkers.neuroEvolution

class Evolution {

    private var generationsNumber = 0

    suspend fun draw(generations: Int) {
        var population = Population.generatePopulation(5, generationsNumber)
        while (generationsNumber < generations) {
            generationsNumber++
            val gamesManager = GameManager(population)
            gamesManager.runGamesParallel()
            population.printPopulationStats()
            population = population.repopulate()
        }
    }
}