package com.checkers

import com.checkers.neuroEvolution.Evolution
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    Evolution().draw(5)
}