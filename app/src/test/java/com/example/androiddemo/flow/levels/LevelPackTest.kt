package com.example.androiddemo.flow.levels

import com.example.androiddemo.flow.generator.countConnections
import com.example.androiddemo.flow.model.BoardSize
import com.example.androiddemo.flow.model.FlowGame
import com.example.androiddemo.flow.playSolution
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LevelPackTest {
    private val solutions = mapOf(
        BoardSize.FIVE to SOLUTIONS_5X5,
        BoardSize.SEVEN to SOLUTIONS_7X7,
        BoardSize.TEN to SOLUTIONS_10X10,
    )

    @Test
    fun everyPackHasFifteenLevelsOfItsSize() {
        BoardSize.entries.forEach { size ->
            val pack = LEVEL_PACKS.getValue(size)
            assertEquals(15, pack.size)
            assertTrue(pack.all { it.boardSize == size })
            assertEquals((1..15).toList(), pack.map { it.number })
        }
    }

    @Test
    fun allLevelsSolvable() {
        BoardSize.entries.forEach { size ->
            LEVEL_PACKS.getValue(size).zip(solutions.getValue(size)).forEach { (level, solution) ->
                val game = FlowGame(level)
                game.playSolution(solution)
                assertTrue("${size.label} level ${level.number} should be solved", game.isSolved)
                assertEquals(level.size * level.size, game.filledCount)
            }
        }
    }

    @Test
    fun connectingAllFlowsAlwaysFillsBoard() {
        BoardSize.entries.filter { it.requireFill }.forEach { size ->
            LEVEL_PACKS.getValue(size).forEach { level ->
                assertEquals(
                    "${size.label} level ${level.number} must have one way to connect",
                    1,
                    level.countConnections(limit = 2),
                )
            }
        }
    }
}
