package com.checkers.neuroEvolution

import com.checkers.utlis.initOnce
import kotlin.math.roundToInt
import kotlin.random.Random

class Population(
    val population: List<NeuralNetwork>,
    val generationNumber: Int,
    private val mutationRate: Double
) {

    private var selectionPool: List<NeuralNetwork> by initOnce()

    init {
        population.forEachIndexed { index, nn -> nn.name = "g$generationNumber-i$index" }
    }

    fun repopulate(): Population {
        createSelectionPool()
        print("Creating new generation...")
        return Population((population.indices)
            .map {
                val parent1 = selectParent()
                val parent2 = selectParent(partner = parent1)
                Pair(parent1, parent2)
            }.map { parents ->
                crossover(parents.first, parents.second)
            }.map { child ->
                mutation(child)
            },
            generationNumber + 1
        ,mutationRate
        )
    }

    private fun createSelectionPool() {
        selectionPool = population.map { nn -> List(nn.fitness.roundToInt()) {nn} }.flatten()
    }

    private fun selectParent(partner: NeuralNetwork? = null): NeuralNetwork {
        var randomParent = selectionPool.random()
        while (randomParent == partner) randomParent = selectionPool.random()
        return randomParent
    }

    private fun crossover(parent1: NeuralNetwork, parent2: NeuralNetwork): NeuralNetwork {
        val dna1 = parent1.dna
        val dna2 = parent2.dna

        if (dna1.size != dna2.size) throw Throwable("can't perform crossover on different sizes DNAs")

        val childDNA = dna1.zip(dna2).map { pair ->
            if (pair.first.size != pair.second.size) throw Throwable("can't perform crossover on different sizes DNAs")
            val size = pair.first.size
            val randomMiddle = (0 until size).random()
            pair.first.subList(0, randomMiddle) + pair.second.subList(randomMiddle, size)
        }

        return NeuralNetwork.fromDNA(parent1.input_nodes, parent1.hidden_nodes, parent1.output_nodes, childDNA)
    }

    private fun mutation(original: NeuralNetwork): NeuralNetwork {
        val dna = original.dna

        val mutatedDNA = dna.map { row ->
            row.map { value ->
                val random = Random.nextDouble(0.0, 1.0)
                if (mutationRate > random) Random.nextDouble(0.0, 1.0)
                else value
            }
        }

        return NeuralNetwork.fromDNA(original.input_nodes, original.hidden_nodes, original.output_nodes, mutatedDNA)
    }

    companion object {
        fun generatePopulation(amount: Int, generationNumber: Int): Population {
            return Population(List(amount) {
                NeuralNetwork.randomNeuralNetwork(32, 16, 1)
            }, generationNumber, 0.01)
        }
    }

    fun printPopulationStats() {
        println("    name    |  fitness  ")
        population.forEach { nn ->
            println("   ${nn.name}   |   ${nn.fitness}")
        }
    }
}
