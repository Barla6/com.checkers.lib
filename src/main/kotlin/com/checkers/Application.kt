package com.checkers

import com.checkers.neuroEvolution.DNA
import com.checkers.neuroEvolution.Evolution
import com.checkers.neuroEvolution.NeuralNetwork
import kotlinx.coroutines.runBlocking
import java.io.FileInputStream
import java.io.ObjectInputStream

fun main() = runBlocking {

//    val path = "C:\\Users\\barla\\Desktop\\programming\\Checkers\\CheckersLib\\src\\main\\kotlin\\com\\checkers\\neuroEvolution\\trainedBrains\\easy.txt"
//
//    val dna = ObjectInputStream(FileInputStream(path)).use { it.readObject() } as DNA
//
//    val nn = NeuralNetwork.fromDNA(32, 16, 1, dna)
//
//    println(nn)

    Evolution().draw()
}