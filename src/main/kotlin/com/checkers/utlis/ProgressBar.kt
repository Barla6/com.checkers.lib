package com.checkers.utlis

class ProgressBar(private val name: String, private val total: Int) {
    private var progress = 0

    private val percentage
        get() = ((progress.toDouble()/total)*100).toInt()

    fun step(step: Int = 1) {
        progress += step
        this.print()
    }

    private fun print() {
        val progressPassed = (0 until percentage).fold("") { str, _ -> "$str#" }
        val progressLeft = (percentage until 100).fold("") { str, _ -> "$str " }
        print("$name  |  [$progressPassed$progressLeft]  |  $percentage% \r")
    }
}