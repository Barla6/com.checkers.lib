package com.checkers.models

import com.checkers.utlis.assert

class StepSequence(
        private val startingBoard: Board,
        private val steps: List<Coordinates>,
        private val eaten: Boolean = false,
        private val completed: Boolean = false
) {
    val resultBoard: Board = steps.zipWithNext()
            .fold(startingBoard.clone()) { board, (startCoordinates, endCoordinates) ->
                board.executeStep(startCoordinates, endCoordinates)
            }
        get() = field

    private val piece: Piece
        get() = resultBoard.getPieceByCoordinates(currentCoordinates)!!

    private val lastDirection: StepDirection?
        get() =
            if (steps.size <= 1) null
            else StepDirection.getDirection(steps[steps.lastIndex - 1], steps[steps.lastIndex])

    val currentCoordinates
        get() = steps.last()

    public fun addStep(newCoordinates: Coordinates, eaten: Boolean = false) =
            StepSequence(startingBoard, steps + newCoordinates, eaten, !eaten)

    public fun completeStepSequence() =
            StepSequence(startingBoard, steps, eaten, true)

    fun getPossibleTurnsForPiece(): List<StepSequence> {

        val allNextPossibleSteps = getNextPossibleSteps()
        if (allNextPossibleSteps.isEmpty()) return listOf()

        val (completed, inProgress) = allNextPossibleSteps
                .map {
                    if (!it.completed && it.getNextPossibleSteps().isEmpty()) it.completeStepSequence()
                    else it
                }.partition { it.completed }
        return completed + inProgress.map { it.getPossibleTurnsForPiece() }.flatten()
    }

    fun getNextPossibleSteps(): List<StepSequence> {
        val directions =
                ((if (eaten) StepDirection.values() else piece.getDirections()).toList() - lastDirection).requireNoNulls()

        return when (piece.type) {
            PieceType.REGULAR -> directions.map { direction -> searchNextPossibleStepsForPiece(direction) }
            PieceType.KING -> directions.map { direction -> searchNextPossibleStepsForKing(direction) }
        }.flatten()
    }

    private fun searchNextPossibleStepsForPiece(direction: StepDirection): List<StepSequence> {
        val eatSteps: List<StepSequence> by lazy {
            listOf(
                    addStep(
                            currentCoordinates.step(direction, 2)!!,
                            eaten = true
                    )
            )
        }
        val simpleSteps: List<StepSequence> by lazy { listOf(addStep(currentCoordinates.step(direction)!!)) }
        return when {
            canEat(direction) -> eatSteps
            canStep(direction) -> simpleSteps
            else -> listOf()
        }
    }

    fun canEat(
            direction: StepDirection,
            // if param "landingCoordinates" is null -> we take the coordinate 2 steps from currentCoordinate in direction.
            landingCoordinate: Coordinates? = currentCoordinates.step(direction, 2)
    ) =
            assert {
                // require landing coordinate to be in board and empty
                require(resultBoard.isCoordinateEmpty(landingCoordinate!!))
                require(currentCoordinates != landingCoordinate)

                require(
                        Coordinates
                                // coordinates in between
                                .range(currentCoordinates, landingCoordinate).drop(1).dropLast(1)
                                // map to pieces
                                .mapNotNull { resultBoard.getPieceByCoordinates(it) }
                                // require single piece to be of the opposite player
                                .single().enemyOf(piece)
                )
            }

    fun canStep(direction: StepDirection, targetCoordinates: Coordinates? = currentCoordinates.step(direction)) =
            assert {
                require(!eaten)
                require(resultBoard.isCoordinateEmpty(targetCoordinates!!))
                require(resultBoard.isRangeEmpty(currentCoordinates, targetCoordinates))
            }

    private fun searchNextPossibleStepsForKing(direction: StepDirection): List<StepSequence> {
        val coordinatesInDirection = currentCoordinates.getAllCoordinatesInDirection(direction)

        return (coordinatesInDirection).mapNotNull { possibleCoordinate ->
            when {
                canEat(direction, landingCoordinate = possibleCoordinate) ->
                    this.addStep(possibleCoordinate, eaten = true)
                canStep(direction, targetCoordinates = possibleCoordinate) ->
                    this.addStep(possibleCoordinate)
                else -> null
            }
        }
    }


    override fun hashCode(): Int {
        var result = startingBoard.hashCode()
        result = 31 * result + steps.hashCode()
        result = 31 * result + eaten.hashCode()
        result = 31 * result + completed.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean =
            (other is StepSequence) && (startingBoard == other.startingBoard) &&
                    (steps == other.steps) && (eaten == other.eaten) && (completed == other.completed)
}
