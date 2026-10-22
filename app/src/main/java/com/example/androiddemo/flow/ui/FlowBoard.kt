package com.example.androiddemo.flow.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import com.example.androiddemo.flow.model.Cell
import com.example.androiddemo.flow.model.FlowGame

@Composable
internal fun FlowBoard(game: FlowGame, modifier: Modifier = Modifier) {
    val n = game.level.size
    Canvas(
        modifier.pointerInput(game) {
            fun cellAt(offset: Offset): Cell {
                val cellSize = size.width.toFloat() / n
                return Cell(
                    (offset.y / cellSize).toInt().coerceIn(0, n - 1),
                    (offset.x / cellSize).toInt().coerceIn(0, n - 1),
                )
            }
            detectDragGestures(
                onDragStart = { game.startDrag(cellAt(it)) },
                onDrag = { change, _ -> game.dragTo(cellAt(change.position)) },
                onDragEnd = game::endDrag,
                onDragCancel = game::endDrag,
            )
        }
    ) {
        val cellSize = size.width / n
        fun center(cell: Cell) = Offset((cell.col + 0.5f) * cellSize, (cell.row + 0.5f) * cellSize)

        // Tint filled cells.
        game.paths.forEach { (color, path) ->
            val tint = DOT_COLORS.getValue(color).copy(alpha = 0.22f)
            path.forEach { cell ->
                drawRect(tint, Offset(cell.col * cellSize, cell.row * cellSize), Size(cellSize, cellSize))
            }
        }

        // Grid lines.
        val gridColor = Color(0xFF555555)
        for (i in 0..n) {
            val p = i * cellSize
            drawLine(gridColor, Offset(p, 0f), Offset(p, size.height), strokeWidth = 2f)
            drawLine(gridColor, Offset(0f, p), Offset(size.width, p), strokeWidth = 2f)
        }

        // Pipes.
        game.paths.forEach { (color, path) ->
            path.zipWithNext { a, b ->
                drawLine(
                    DOT_COLORS.getValue(color),
                    center(a),
                    center(b),
                    strokeWidth = cellSize * 0.32f,
                    cap = StrokeCap.Round,
                )
            }
        }

        // Endpoint dots.
        game.level.endpoints.forEach { (color, ends) ->
            listOf(ends.first, ends.second).forEach {
                drawCircle(DOT_COLORS.getValue(color), radius = cellSize * 0.34f, center = center(it))
            }
        }
    }
}
