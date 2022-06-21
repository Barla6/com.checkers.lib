package com.checkers.neuroEvolution

import kotlin.random.Random

class Population(
    val population: List<NeuralNetwork>,
    private val generationNumber: Int,
    private val mutationRate: Double
) {

    fun repopulate(): Population {
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

    private fun selectParent(partner: NeuralNetwork? = null): NeuralNetwork {
        val fitnessSum = population.sumOf { it.fitness!! }
        val neuralNetworkAndFitness = population.map { Pair(it, it.fitness!! / fitnessSum) }
        var randomParent = neuralNetworkAndFitness.random()
        val randomProbability = Random.nextDouble(0.0, 1.0)
        var foundParent = false
        while (!foundParent) {
            if (randomParent.first != partner && randomProbability < randomParent.second) foundParent = true
            randomParent = neuralNetworkAndFitness.random()
        }
        return randomParent.first
    }

    private fun crossover(parent1: NeuralNetwork, parent2: NeuralNetwork): NeuralNetwork {
        val DNA1 = parent1.DNA
        val DNA2 = parent2.DNA

        if (DNA1.size != DNA2.size) throw Throwable("can't perform crossover on different sizes DNAs")

        val childDNA = DNA1.zip(DNA2).map { pair ->
            if (pair.first.size != pair.second.size) throw Throwable("can't perform crossover on different sizes DNAs")
            val size = pair.first.size
            val randomMiddle = (0 until size).random()
            pair.first.subList(0, randomMiddle) + pair.second.subList(randomMiddle, size)
        }

        return NeuralNetwork.fromDNA(parent1.input_nodes, parent1.hidden_nodes, parent1.output_nodes, childDNA)
    }

    private fun mutation(original: NeuralNetwork): NeuralNetwork {
        val DNA = original.DNA

        val mutatedDNA = DNA.map { row ->
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
            }, generationNumber, 0.01).apply {
                population.forEachIndexed { index, nn -> nn.name = "g$generationNumber-i$index" }
            }
        }
    }
}
