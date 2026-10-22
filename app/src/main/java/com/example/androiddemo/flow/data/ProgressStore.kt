package com.example.androiddemo.flow.data

import android.content.Context
import com.example.androiddemo.flow.model.BoardSize

/** Saves, per board size, which pack levels are solved and how many random boards have been solved. */
class ProgressStore(context: Context) {
    private val prefs = context.getSharedPreferences("flow", Context.MODE_PRIVATE)

    fun completedLevels(size: BoardSize): Set<Int> =
        prefs.getStringSet(key(KEY_COMPLETED, size), emptySet())!!
            .mapNotNull { it.toIntOrNull() }
            .toSet()

    fun setCompletedLevels(size: BoardSize, levels: Set<Int>) {
        prefs.edit()
            .putStringSet(key(KEY_COMPLETED, size), levels.map { it.toString() }.toSet())
            .apply()
    }

    fun randomSolved(size: BoardSize): Int = prefs.getInt(key(KEY_RANDOM_SOLVED, size), 0)

    fun setRandomSolved(size: BoardSize, count: Int) {
        prefs.edit().putInt(key(KEY_RANDOM_SOLVED, size), count).apply()
    }

    // 5x5 keeps the original unsuffixed keys so progress saved before other sizes existed survives.
    private fun key(base: String, size: BoardSize) =
        if (size == BoardSize.FIVE) base else "${base}_${size.cells}"

    private companion object {
        const val KEY_COMPLETED = "completed"
        const val KEY_RANDOM_SOLVED = "random_solved"
    }
}
