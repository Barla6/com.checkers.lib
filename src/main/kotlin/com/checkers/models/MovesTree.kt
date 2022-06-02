package com.checkers.models

data class MovesTree(
    val currentMove: MoveWithResultBoard,
    var nextPossibleMoves: MutableList<MovesTree> = mutableListOf()
)