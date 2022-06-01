package com.checkers.models

class StepSequence(
    private val startingBoard: Board,
    private val steps: List<Coordinates>,
    private val eaten: Boolean = false,
    private val completed: Boolean = false
) {
    private val resultBoard: Board = steps.zipWithNext()
        .fold(startingBoard.clone()) { board, (startCoordinates, endCoordinates) ->
            board.executeStep(startCoordinates, endCoordinates)
        }
    fun resultBoard() = resultBoard

    private val piece: Piece
        get() = resultBoard().getPieceByCoordinates(currentCoordinates())!!

    private val lastDirection: StepDirection?
        get() =
            if (steps.size == 1) null
            else StepDirection.getDirection(steps[steps.lastIndex - 1], steps[steps.lastIndex])

    fun currentCoordinates() = steps.last()

    private fun addEatingStep(direction: StepDirection, numberOfSteps: Int = 2): StepSequence {
        val newCoordinates = currentCoordinates().step(direction, numberOfSteps)!!
        return StepSequence(startingBoard, steps + newCoordinates, eaten = true, completed = false)
    }

    private fun addSimpleStep(direction: StepDirection, numberOfSteps: Int = 1): StepSequence {
        val newCoordinates = currentCoordinates().step(direction, numberOfSteps)!!
        return StepSequence(startingBoard, steps + newCoordinates, eaten = false, completed = true)
    }

    private fun getNextPossibleSteps(): List<StepSequence> {
        val directions =
            ((if (eaten) StepDirection.values() else piece.getDirections()).toList() - lastDirection).map { it!! }

        return when (piece.type) {
            PieceType.REGULAR -> directions.map { direction -> searchNextPossibleStepsForRegularPiece(direction) }
            PieceType.KING -> directions.map { direction -> searchNextPossibleStepForKing(direction) }
        }.flatten()
    }

    private fun searchNextPossibleStepsForRegularPiece(direction: StepDirection): List<StepSequence> {
        return when {
            canEat(direction) -> listOf(addEatingStep(direction))
            canStep(direction) -> listOf(addSimpleStep(direction))
            else -> listOf()
        }
    }

    fun canEat(direction: StepDirection, coordinatesToEat: Coordinates? = null): Boolean {
        // if param "coordinatesToEat" set to null -> we take the next coordinates in the given direction.
        // if those are out of board -> return false (illegal move).
        val coordinatesToEat = coordinatesToEat ?: currentCoordinates().step(direction) ?: return false

        // if there is no piece to eat -> return false.
        val pieceToEat = resultBoard().getPieceByCoordinates(coordinatesToEat) ?: return false

        // if the piece to eat is not the enemy -> return false.
        if (!pieceToEat.enemyOf(piece)) return false

        // if the "landing coordinates" are out of board -> return false.
        val nextCoordinates = coordinatesToEat.step(direction) ?: return false

        // if the "landing coordinates" are taken -> return false.
        val pieceInNextPlace = resultBoard().getPieceByCoordinates(nextCoordinates)
        if (pieceInNextPlace != null) return false

        // if there is another piece standing in the way of eating -> return false.
        if (!resultBoard().isRangeEmpty(currentCoordinates(), coordinatesToEat)) return false

        return true
    }

    fun canStep(direction: StepDirection, targetCoordinates: Coordinates? = null): Boolean {
        // if we've already eaten -> simple step is not allowed
        if (eaten) return false

        // if param "targetCoordinates" is null -> take the next coordinates in direction.
        // if those are out of board -> return false.
        val targetCoordinates = targetCoordinates ?: currentCoordinates().step(direction) ?: return false

        // if the "targetCoordinates" are not empty -> return false.
        val pieceInTargetCoordinates = resultBoard().getPieceByCoordinates(targetCoordinates)
        if (pieceInTargetCoordinates != null) return false

        // if there is another piece standing in the way of moving -> return false.
        if (!resultBoard().isRangeEmpty(currentCoordinates(), targetCoordinates)) return false

        return true
    }

    private fun searchNextPossibleStepForKing(direction: StepDirection): List<StepSequence> {
        val coordinatesRange = currentCoordinates().getAllCoordinatesInDirection(direction)

        return (coordinatesRange.drop(1)).mapNotNull { possibleCoordinate ->
            val numberOfSteps = Coordinates.countSteps(currentCoordinates(), possibleCoordinate)
            when {
                canEat(direction, coordinatesToEat = possibleCoordinate) ->
                    this.addEatingStep(direction, numberOfSteps)
                canStep(direction, targetCoordinates = possibleCoordinate) ->
                    this.addSimpleStep(direction, numberOfSteps)
                else -> null
            }
        }
    }
}
