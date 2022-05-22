package com.checkers.models

class MovesTree(
    val currentMove: MoveWithResultBoard?,
    val nextPossibleMoves: MutableList<MovesTree> = mutableListOf()
) {

}