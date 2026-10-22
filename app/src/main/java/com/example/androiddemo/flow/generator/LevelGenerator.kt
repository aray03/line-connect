package com.example.androiddemo.flow.generator

import com.example.androiddemo.flow.model.BoardSize
import com.example.androiddemo.flow.model.Level
import kotlin.random.Random

/** A generated level together with the full-board solution it was cut from. */
class GeneratedBoard(val level: Level, val solution: List<String>)

/**
 * Builds random boards by cutting a path through every cell into pieces. On boards that
 * require filling, it only keeps boards whose one way to connect all flows fills every cell.
 */
object LevelGenerator {
    const val COLORS = "RGBYOCPMKWSLTNAI"

    fun generate(
        size: BoardSize,
        random: Random = Random.Default,
        colorCount: Int = size.colorCounts.random(random),
    ): GeneratedBoard {
        require(colorCount <= COLORS.length) { "At most ${COLORS.length} colors" }
        val n = size.cells
        val path = zigzagPath(n)
        scramble(path, n, random, moves = n * n * 20)
        while (true) {
            // Each attempt reshuffles the same path a little rather than starting from scratch.
            scramble(path, n, random, moves = n * n)
            val segments = splitPath(path, colorCount, random) ?: continue
            val solution = Array(n) { CharArray(n) }
            segments.forEachIndexed { i, segment ->
                segment.forEach { solution[it / n][it % n] = COLORS[i] }
            }
            if (isBoring(solution, segments, n)) continue

            val rows = Array(n) { CharArray(n) { '.' } }
            segments.forEachIndexed { i, segment ->
                listOf(segment.first(), segment.last()).forEach { rows[it / n][it % n] = COLORS[i] }
            }
            val level = Level(0, rows.map { String(it) })
            if (!size.requireFill || level.countConnections(limit = 2) == 1) {
                return GeneratedBoard(level, solution.map { String(it) })
            }
        }
    }

    /** Path through every cell (as row * n + col), snaking back and forth row by row. */
    private fun zigzagPath(n: Int) = IntArray(n * n) { i ->
        val row = i / n
        row * n + if (row % 2 == 0) i % n else n - 1 - i % n
    }

    /** Randomizes a path through every cell in place using "backbite" moves. */
    private fun scramble(path: IntArray, n: Int, random: Random, moves: Int) {
        val position = IntArray(path.size)
        repeat(moves) {
            if (random.nextBoolean()) path.reverse()
            val end = path.last()
            val r = end / n
            val c = end % n
            val pivots = listOfNotNull(
                (end - n).takeIf { r > 0 }, (end + n).takeIf { r < n - 1 },
                (end - 1).takeIf { c > 0 }, (end + 1).takeIf { c < n - 1 },
            ).filter { it != path[path.size - 2] }
            path.forEachIndexed { i, cell -> position[cell] = i }
            path.reverse(position[pivots.random(random)] + 1, path.size)
        }
    }

    /** Cuts [path] into [count] pieces of at least 3 cells, or null if the random cuts don't fit. */
    private fun splitPath(path: IntArray, count: Int, random: Random): List<IntArray>? {
        val cuts = (3..path.size - 3).shuffled(random).take(count - 1).sorted()
        val bounds = listOf(0) + cuts + path.size
        val segments = bounds.zipWithNext { a, b -> path.copyOfRange(a, b) }
        return segments.takeIf { s -> s.all { it.size >= 3 } }
    }

    /** Rejects adjacent dot pairs and whole rows/columns of one color, which play too trivially. */
    private fun isBoring(solution: Array<CharArray>, segments: List<IntArray>, n: Int): Boolean {
        val adjacentEnds = segments.any { s ->
            Math.abs(s.first() / n - s.last() / n) + Math.abs(s.first() % n - s.last() % n) == 1
        }
        val solidRow = solution.any { row -> row.toSet().size == 1 }
        val solidCol = (0 until n).any { c -> solution.map { it[c] }.toSet().size == 1 }
        return adjacentEnds || solidRow || solidCol
    }
}
