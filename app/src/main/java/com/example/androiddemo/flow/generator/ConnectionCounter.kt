package com.example.androiddemo.flow.generator

import com.example.androiddemo.flow.model.Level

/**
 * Counts ways to connect every pair, empty cells allowed, stopping once [limit] is reached.
 * A result of 1 means the only way to connect all flows is the full-board solution. If the
 * search exceeds [maxSteps] it gives up and returns [limit], i.e. "assume not unique".
 */
fun Level.countConnections(limit: Int, maxSteps: Long = 2_000_000): Int {
    val n = size
    val adjacent = Array(n * n) { i ->
        val r = i / n
        val c = i % n
        listOfNotNull(
            (i - n).takeIf { r > 0 }, (i + n).takeIf { r < n - 1 },
            (i - 1).takeIf { c > 0 }, (i + 1).takeIf { c < n - 1 },
        ).toIntArray()
    }
    val ends = endpoints.values.toList()
    val starts = IntArray(ends.size) { ends[it].first.row * n + ends[it].first.col }
    val targets = IntArray(ends.size) { ends[it].second.row * n + ends[it].second.col }
    val used = BooleanArray(n * n)
    starts.forEach { used[it] = true }
    targets.forEach { used[it] = true }

    // Breadth-first search scratch space, reused between calls.
    val seen = IntArray(n * n)
    val queue = IntArray(n * n)
    var stamp = 0
    fun reachable(from: Int, to: Int): Boolean {
        stamp++
        var head = 0
        var tail = 0
        queue[tail++] = from
        seen[from] = stamp
        while (head < tail) {
            for (next in adjacent[queue[head++]]) {
                if (next == to) return true
                if (!used[next] && seen[next] != stamp) {
                    seen[next] = stamp
                    queue[tail++] = next
                }
            }
        }
        return false
    }

    /** Prunes branches where the current or any later color can no longer reach its partner. */
    fun stillConnectable(colorIndex: Int, head: Int): Boolean {
        if (!reachable(head, targets[colorIndex])) return false
        return (colorIndex + 1 until starts.size).all { reachable(starts[it], targets[it]) }
    }

    var found = 0
    var steps = 0L
    fun extend(colorIndex: Int, head: Int) {
        if (found >= limit || ++steps > maxSteps) return
        for (next in adjacent[head]) {
            if (next == targets[colorIndex]) {
                if (colorIndex == starts.lastIndex) found++
                else if (stillConnectable(colorIndex + 1, starts[colorIndex + 1])) {
                    extend(colorIndex + 1, starts[colorIndex + 1])
                }
            } else if (!used[next]) {
                used[next] = true
                if (stillConnectable(colorIndex, next)) extend(colorIndex, next)
                used[next] = false
            }
            if (found >= limit || steps > maxSteps) return
        }
    }
    if (stillConnectable(0, starts[0])) extend(0, starts[0])
    return if (steps > maxSteps) limit else found
}
