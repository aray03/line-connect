package com.example.lineconnect.flow.model

class Level(val number: Int, val rows: List<String>) {
    val size: Int = rows.size
    val boardSize: BoardSize = BoardSize.of(size)

    /** Dot color letter -> its two endpoint cells. */
    val endpoints: Map<Char, Pair<Cell, Cell>>

    init {
        require(rows.all { it.length == size }) { "Level $number must be square" }
        val found = mutableMapOf<Char, MutableList<Cell>>()
        rows.forEachIndexed { r, line ->
            line.forEachIndexed { c, ch ->
                if (ch != '.') found.getOrPut(ch) { mutableListOf() }.add(Cell(r, c))
            }
        }
        require(found.values.all { it.size == 2 }) { "Level $number: every color needs exactly 2 dots" }
        endpoints = found.mapValues { (_, cells) -> cells[0] to cells[1] }
    }
}
