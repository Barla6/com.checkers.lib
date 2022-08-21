package com.checkers.utlis

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ProgressBar(private val name: String, private val total: Int) {
    private val mutex = Mutex()
    private val scope = CoroutineScope(Dispatchers.Default)

    // progress related:
    private var progress = 0
    private var progressString: String = " ".repeat(100)
    private val percentage
        get() = ((progress.toDouble() / total) * 100).toInt()
    private val isOver
        get() = percentage == 100

    // process spin
    private var spinCharsIndex = 0
    private val currentSpin
        get() = spinChars[spinCharsIndex]

    fun start() = scope.launch {
        while (percentage < 100) {
            delay(100)
            this@ProgressBar.print()
            incrementSpin()
        }
    }

    private suspend fun print() =
        mutex.withLock {
            if (isOver) scope.cancel()
            print("$name  $currentSpin  [$progressString]  |  $percentage% ${if(isOver) "\n" else "\r"}")
        }

    suspend fun step(step: Int = 1) =
        mutex.withLock {
            progress += step
            progressString = "#".repeat(percentage) + " ".repeat(100 - percentage)
        }

    private suspend fun incrementSpin() =
        mutex.withLock {
            spinCharsIndex = (spinCharsIndex + 1) % 4
        }


    companion object {
        private val spinChars = listOf("\\", "|", "/", "-")
    }
}