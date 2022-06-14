package com.checkers.models

class Piece(
        val player: Player,
        var type: PieceType = PieceType.REGULAR
) {

    fun getDirections(): Array<StepDirection> {
        return if (type == PieceType.KING) StepDirection.values()
        else player.directions
    }

    fun makeKingIfNeeded(coordinates: Coordinates) {
        if (type != PieceType.KING && hasReachedOtherSide(coordinates))
            this.type = PieceType.KING
    }

    fun clone(): Piece = Piece(player, type)

    private fun hasReachedOtherSide(coordinates: Coordinates): Boolean =
        player.crowningRow == coordinates.row

    override fun equals(other: Any?): Boolean =
        (other is Piece) && (type == other.type) && (player == other.player)

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    fun enemyOf(other: Piece) = this.player.direction oppositeTo other.player.direction
}