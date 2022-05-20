package com.checkers

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

}