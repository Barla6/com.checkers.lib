package com.checkers

import com.checkers.models.*
import java.util.*

object GameRunner {

    fun runGame(game: Game) {
        checkGameType(game).runGame(game)
    }

    private fun checkGameType(game: Game): GameType {
        return when (game.player1) {
            is HumanPlayer -> when (game.player2) {
                is HumanPlayer -> GameType.HUMAN_VS_HUMAN
                is AIPlayer -> GameType.HUMAN_VS_COMPUTER
            }
            is AIPlayer -> when (game.player2) {
                is HumanPlayer -> GameType.HUMAN_VS_COMPUTER
                is AIPlayer -> GameType.COMPUTER_VS_COMPUTER
            }
        }
    }
}

enum class GameType {
    HUMAN_VS_HUMAN {
        override fun runGame(game: Game) {
            TODO("Not yet implemented")
        }
    },
    HUMAN_VS_COMPUTER {
        override fun runGame(game: Game) {
            val reader = Scanner(System.`in`)
            val humanPlayer = getHumanPlayer(game)

            while (!game.isOver) {
                val movesTree = MovesTree(humanPlayer, game.board, 1)
                val leadingStepsAndFinalBoards = movesTree.getLeadingStepsAndFinalBoards()

                println("choose move:")
                leadingStepsAndFinalBoards.map { it.first }.forEachIndexed { index, step ->
                    println("${index+1}) ${step.stringStepTrace()}")
                }
                val chosenStepIndex = reader.nextInt()-1

                game.playTurn(leadingStepsAndFinalBoards[chosenStepIndex].first, humanPlayer)
            }
        }

        private fun getHumanPlayer(game: Game): Player {
            return listOf(game.player1, game.player2).find { it is HumanPlayer }!!
        }
    },
    COMPUTER_VS_COMPUTER {
        override fun runGame(game: Game) {
            var player = game.getRandomPlayer() as AIPlayer
            while (!game.isOver) {
                val newBoard = player.playTurn(game.board)
                game.turnCounter++
                if (newBoard != null) {
                    game.board = newBoard
                    if (game.board.countPiecesOfPlayer(player.oppositePlayer) == 0) game.winner = player
                    game.board.printBoard()
                } else {
                    game.winner = player
                }
                player = player.oppositePlayer as AIPlayer
            }

            println("GAME OVER")
            println("players: ${game.player1.name} VS ${game.player2.name}")
            println("WINNER: ${game.winner?.name ?: "tie"}")
            println("turnsCount: ${game.turnCounter}")
        }
    };

    abstract fun runGame(game: Game)
}