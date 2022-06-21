package com.checkers.neuroEvolution

import com.checkers.GameRunner
import com.checkers.models.Game
import com.checkers.models.player.AIPicker
import com.checkers.models.player.Computer

class GamesManager(private val population: Population) {

    fun runGames() {
        population.population.forEach { player ->
            val player1 = Computer(AIPicker(player))
            population.population.forEach { contestant ->
                if (player != contestant) {
                    val player2 = Computer(AIPicker(contestant))
                    val game = Game(player1, player2)
                    GameRunner.runGame(game)
                    player.gamesCounter++
                    contestant.gamesCounter++
                    ((game.winner?.type as? Computer)?.picker as? AIPicker)?.brain?.addWinning()
                }
            }
        }
    }

    fun printWinningStats() {
        println("WINNING STATS: ")
        println("   name   | winnings | fitness")
        population.population.forEachIndexed { index, neuralNetwork ->
            println("   ${neuralNetwork.name}  |     ${neuralNetwork.winningsCount}    |   ${neuralNetwork.fitness}")
        }
    }

//    private fun runGame(game: Game) {
//        var player = game.getRandomPlayer()
//        while (!game.isOver) {
//            val newBoard = player.playTurn(game.board)
//            game.turnCounter++
//            if (newBoard != null) {
//                game.board = newBoard
//                if (game.board.countPiecesOfPlayer(player.oppositePlayer) == 0) game.winner = player
//                game.board.printBoard()
//            } else {
//                game.winner = player
//            }
//            player = player.oppositePlayer
//        }
//
//        println("GAME OVER")
//        println("players: ${game.player1.name} VS ${game.player2.name}")
//        println("WINNER: ${game.winner?.name ?: "tie"}")
//        println("turnsCount: ${game.turnCounter}")
//    }
}