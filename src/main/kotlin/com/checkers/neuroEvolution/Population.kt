package com.checkers.neuroEvolution

class Population(val population: List<NeuralNetwork>) {

    fun createNewGeneration(): Population {
        // todo: implement
        return Population(listOf())
    }

    private fun crossover(): NeuralNetwork {
        // todo: implement
        return NeuralNetwork(1, 2,3)
    }

    private fun mutation(): NeuralNetwork {
        // todo: implement
        return NeuralNetwork(1, 2,3)
    }

    companion object {
        fun generatePopulation(amount: Int): Population {
            return Population(List(amount) { NeuralNetwork(32, 16, 1) })
        }
    }
}
