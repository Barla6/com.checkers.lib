package com.checkers.neuroEvolution

import java.io.FileOutputStream
import java.io.ObjectOutputStream

class Evolution {

    private var generationsNumber = 0

    suspend fun draw() {
        var population = Population.generatePopulation(10, generationsNumber)
        while (generationsNumber <= hard) {
            generationsNumber++
            val gamesManager = GameManager(population)
            gamesManager.runGamesParallel()
            population.printPopulationStats()
            saveIfNeeded(population)
            population = population.repopulate()
        }
    }

    private fun saveIfNeeded(population: Population) {
        val path = when (generationsNumber) {
            easy -> "$absPath\\easy.txt"
            medium -> "$absPath\\medium.txt"
            hard -> "$absPath\\hard.txt"
            else -> null
        } ?: return

        val dnaToSave = population.pickBest()!!.dna

        ObjectOutputStream(FileOutputStream(path)).use { it.writeObject(dnaToSave) }
    }

    companion object {
        const val easy = 1
        const val medium = 20
        const val hard = 40
        const val absPath = "C:\\Users\\barla\\Desktop\\programming\\Checkers\\CheckersLib\\src\\main\\kotlin\\com\\checkers\\neuroEvolution\\trainedBrains"
    }
}