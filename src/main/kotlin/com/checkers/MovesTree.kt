package com.checkers

class MovesTree(
    val currentMove: MoveWithResultBoard?,
    val nextPossibleMoves: MutableList<MovesTree> = mutableListOf()
) {

}