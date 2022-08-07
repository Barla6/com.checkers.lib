package com.checkers.neuroEvolution

import java.io.FileOutputStream
import java.io.ObjectOutputStream

class Evolution {

    private var generationsNumber = 0

    suspend fun draw() {
        var population = Population.generatePopulation(5, generationsNumber)
        while (generationsNumber <= hard) {
            generationsNumber++
            val gamesManager = GameManager(population)
            gamesManager.runGamesParallel()
//            population.printPopulationStats()
//            saveIfNeeded(population)
            population = population.repopulate()
        }
    }

    private fun saveIfNeeded(population: Population) {

        val levelName = when (generationsNumber) {
            easy -> "easy"
            medium -> "medium"
            hard -> "hard"
            else -> null
        } ?: return

        val path = "$absPath\\$levelName.txt"
        val dnaToSave = population.pickBest()!!.dna

        ObjectOutputStream(FileOutputStream(path)).use { it.writeObject(dnaToSave) }

        println("AI of level $levelName was saved!")
    }

    companion object {
        const val easy = 1
        const val medium = 20
        const val hard = 40
        // todo: try to use relative path
        const val absPath = "C:\\Users\\barla\\Desktop\\programming\\Checkers\\CheckersLib\\src\\main\\kotlin\\com\\checkers\\neuroEvolution\\trainedBrains"
    }
}