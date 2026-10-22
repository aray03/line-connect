package com.example.androiddemo.flow.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs
import kotlin.math.sign

/** Game rules for one level. Paths always begin at one of their color's endpoints. */
class FlowGame(val level: Level) {
    var paths by mutableStateOf<Map<Char, List<Cell>>>(emptyMap())
        private set

    private var active: Char? = null

    private val endpointColor: Map<Cell, Char> = level.endpoints
        .flatMap { (color, ends) -> listOf(ends.first to color, ends.second to color) }
        .toMap()

    fun endpointColorAt(cell: Cell): Char? = endpointColor[cell]

    fun isConnected(color: Char): Boolean {
        val path = paths[color] ?: return false
        val (a, b) = level.endpoints.getValue(color)
        return path.size >= 2 && setOf(path.first(), path.last()) == setOf(a, b)
    }

    val connectedCount: Int get() = level.endpoints.keys.count { isConnected(it) }

    val filledCount: Int get() = (endpointColor.keys + paths.values.flatten()).size

    val isSolved: Boolean
        get() = connectedCount == level.endpoints.size &&
            (!level.boardSize.requireFill || filledCount == level.size * level.size)

    fun startDrag(cell: Cell) {
        val endColor = endpointColor[cell]
        if (endColor != null) {
            paths = paths + (endColor to listOf(cell))
            active = endColor
            return
        }
        val owner = paths.entries.firstOrNull { cell in it.value }
        if (owner != null) {
            paths = paths + (owner.key to owner.value.take(owner.value.indexOf(cell) + 1))
            active = owner.key
        } else {
            active = null
        }
    }

    /** Extends the active path toward [cell], stepping one cell at a time for fast swipes. */
    fun dragTo(cell: Cell) {
        val color = active ?: return
        if (cell.row !in 0 until level.size || cell.col !in 0 until level.size) return
        while (true) {
            val last = paths[color]?.lastOrNull() ?: return
            if (last == cell) return
            val dr = cell.row - last.row
            val dc = cell.col - last.col
            val next = if (abs(dr) >= abs(dc)) Cell(last.row + dr.sign, last.col)
            else Cell(last.row, last.col + dc.sign)
            if (!stepTo(color, next)) return
        }
    }

    fun endDrag() {
        active = null
    }

    fun reset() {
        paths = emptyMap()
        active = null
    }

    /** Moves the head of [color]'s path onto adjacent [next]; returns false if blocked. */
    private fun stepTo(color: Char, next: Cell): Boolean {
        val path = paths.getValue(color)
        val idx = path.indexOf(next)
        if (idx >= 0) {
            paths = paths + (color to path.take(idx + 1))
            return true
        }
        if (isConnected(color)) return false
        val endColor = endpointColor[next]
        if (endColor != null && endColor != color) return false

        var updated = paths
        val other = paths.entries.firstOrNull { it.key != color && next in it.value }
        if (other != null) {
            updated = updated + (other.key to other.value.take(other.value.indexOf(next)))
        }
        paths = updated + (color to path + next)
        return true
    }
}
