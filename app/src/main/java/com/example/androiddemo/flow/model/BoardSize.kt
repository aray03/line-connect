package com.example.androiddemo.flow.model

/**
 * Supported board sizes. On boards that [requireFill], every cell must be piped to win and
 * levels are built so the only way to connect all flows fills the board; that guarantee is
 * too expensive to generate on 10x10, so there connecting every flow is enough.
 */
enum class BoardSize(val cells: Int, val colorCounts: IntRange, val requireFill: Boolean) {
    FIVE(5, 5..7, requireFill = true),
    SEVEN(7, 8..10, requireFill = true),
    TEN(10, 10..14, requireFill = false);

    val label: String get() = "$cells×$cells"

    companion object {
        fun of(cells: Int): BoardSize = entries.first { it.cells == cells }
    }
}
