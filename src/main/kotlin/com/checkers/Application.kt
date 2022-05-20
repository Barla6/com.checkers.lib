package com.checkers

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.checkers.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.cors.*
import com.google.gson.Gson
import io.ktor.server.request.*

fun main() {

    val game = Game()
    /**
     * simulation with eating backwards
     */
//    game.board.movePiece(Coordinates(5, 2), Coordinates(3, 2))
//    game.board.removePiece(Coordinates(6, 5))

    /**
     * simulation with becoming king
     */
//    game.board.movePiece(Coordinates(2, 1), Coordinates(3, 2))
//    game.board.movePiece(Coordinates(5, 2), Coordinates(4, 3))
//    game.board.removePiece(Coordinates(5, 4))
//    game.board.removePiece(Coordinates(7, 2))

    /**
     * simulation with king
     */
//    game.board.removePiece(Coordinates(7, 4))
//    game.board.movePiece(Coordinates(2, 1), Coordinates(7, 4))
//    game.board.removePiece(Coordinates(6, 3))

//    game.board.movePiece(Coordinates(2, 1), Coordinates(5, 4))
//    game.board.removePiece(Coordinates(5, 2))
//    game.board.removePiece(Coordinates(7, 2))
//    game.board.removePiece(Coordinates(5, 0))
    game.board.printBoard()

//    val result = game.board.getPossibleMovesForPiece(Coordinates(2, 1))
//    println(result)

//    val moves = game.board.getPossibleMovesForPiece(Coordinates(2, 1))

//    val moves = game.board.getPossibleMovesForPlayer(Player.COMPUTER)
//
//    if (moves.size == 0) println("no possible moves")
//    for (move in moves)
//        println(move.getLastPlace())


//    for (move: Move in moves) {
//        println("MOVE")
//        for (coordinate in move.move.steps) {
//            println("[${coordinate.row}, ${coordinate.col}]")
//        }
//        println("RESULT BOARD")
//        move.resultBoard.printBoard()
//    }

//    val turnResult = game.makeTurn()
//    println("MOVE")
//    for (coordinate in turnResult.first.steps) {
//        println("[${coordinate.row}, ${coordinate.col}]")
//    }
//    println("RESULT BOARD")
//    turnResult.second.printBoard()


//    val boardAfterBestMove = game.board.makeTurn(PieceColor.COMPUTER_COLOR)
//    println("---- AFTER BEST MOVE ----")
//    boardAfterBestMove.second.printBoard()

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {

        // configurations and installations
        configureRouting()
        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
        }

        val gson = Gson()

        routing {

            // get request that return the initial board of the game
            get("/board") {
                System.out.println("/board")
                val jsonBoard = gson.toJson(game.board)
                call.respond(jsonBoard)
            }

            post("/possibleMoves") {
                val textBody = call.receiveText()
                val coordinates = gson.fromJson(textBody, Coordinates::class.java)

                call.respond("row: ${coordinates.row} col: ${coordinates.col}")
            }

            post("/play") {
                val textBody = call.receiveText()
                val board = gson.fromJson(textBody, Board::class.java)

                game.board = board
                val start = System.currentTimeMillis()
                game.makeTurn()
                val end = System.currentTimeMillis()
                System.out.println("interval:" + (end-start))
                val resultBoard = gson.toJson(game.board)
                game.board.printBoard()
                call.respond(resultBoard)
            }
        }
    }.start(true)
}
