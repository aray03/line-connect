package com.example.androiddemo.flow

import com.example.androiddemo.flow.model.Cell
import com.example.androiddemo.flow.model.FlowGame

/** Drags each color's [solution] cells (one letter per cell) from one endpoint to the other. */
fun FlowGame.playSolution(solution: List<String>) {
    level.endpoints.forEach { (color, ends) ->
        val cells = solution.flatMapIndexed { r, row ->
            row.mapIndexedNotNull { c, ch -> if (ch == color) Cell(r, c) else null }
        }.toSet()
        val route = route(listOf(ends.first), ends.second, cells)!!
        startDrag(route.first())
        route.drop(1).forEach(::dragTo)
        endDrag()
    }
}

/** Depth-first search for an order visiting every cell in [cells], ending at [end]. */
private fun route(path: List<Cell>, end: Cell, cells: Set<Cell>): List<Cell>? {
    val head = path.last()
    if (path.size == cells.size) return if (head == end) path else null
    return cells
        .filter { it !in path && Math.abs(it.row - head.row) + Math.abs(it.col - head.col) == 1 }
        .firstNotNullOfOrNull { route(path + it, end, cells) }
}
