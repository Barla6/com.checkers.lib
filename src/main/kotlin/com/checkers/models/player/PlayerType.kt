package com.checkers.models.player

sealed interface PlayerType {
}

class Human : PlayerType {
}

class Computer(val picker: BoardPicker) : PlayerType {
}
