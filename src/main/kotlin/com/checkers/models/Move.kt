package com.checkers.models

// TODO: maybe make class Move to typealias
// Move represents a concatenation of a single piece moves
class Move(val steps: MutableList<Coordinates> = mutableListOf()) {

    fun addStep(coordinates: Coordinates) =
        steps.add(coordinates)


    fun getLastPlace(): Coordinates =
        steps.last()


    fun cloneMove(): Move =
        Move(
            steps.map { coordinates -> coordinates.clone() } as MutableList<Coordinates>
        )


    override fun equals(other: Any?): Boolean =
        (other is Move) &&
                other.steps.size == steps.size &&
                other.steps.zip(steps).all { (a, b) -> a == b }

    fun hasEaten(): Boolean =
        !steps.zipWithNext().all { (a,b) -> a nextTo b }

    override fun hashCode(): Int {
        return steps.hashCode()
    }
}