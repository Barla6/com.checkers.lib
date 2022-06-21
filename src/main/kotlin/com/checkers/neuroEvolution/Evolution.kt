package com.checkers.neuroEvolution

class Evolution {

    private var generationsNumber = 0

    fun draw(generations: Int) {
        var population = Population.generatePopulation(5, generationsNumber)
        while (generationsNumber < generations) {
            generationsNumber++
            val gamesManager = GameManager(population)
            gamesManager.runGames()
            gamesManager.printWinningStats()
            population = population.repopulate()
        }
    }
}