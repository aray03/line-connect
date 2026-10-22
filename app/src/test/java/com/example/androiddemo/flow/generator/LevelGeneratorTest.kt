package com.example.androiddemo.flow.generator

import com.example.androiddemo.flow.model.BoardSize
import com.example.androiddemo.flow.model.FlowGame
import com.example.androiddemo.flow.playSolution
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LevelGeneratorTest {
    private val boardsPerSize = mapOf(BoardSize.FIVE to 20, BoardSize.SEVEN to 5, BoardSize.TEN to 10)

    @Test
    fun generatedBoardsMatchTheirSize() {
        val random = Random(42)
        boardsPerSize.forEach { (size, count) ->
            repeat(count) {
                val board = LevelGenerator.generate(size, random)
                assertEquals(size.cells, board.level.size)
                assertTrue(board.level.endpoints.size in size.colorCounts)
            }
        }
    }

    @Test
    fun generatedSolutionsWin() {
        val random = Random(7)
        boardsPerSize.forEach { (size, count) ->
            repeat(count) {
                val board = LevelGenerator.generate(size, random)
                val game = FlowGame(board.level)
                game.playSolution(board.solution)
                assertTrue("${size.label} solution should win", game.isSolved)
                assertEquals(size.cells * size.cells, game.filledCount)
            }
        }
    }

    @Test
    fun fillRequiredBoardsHaveOneWayToConnect() {
        val random = Random(99)
        boardsPerSize.filterKeys { it.requireFill }.forEach { (size, count) ->
            repeat(count) {
                assertEquals(1, LevelGenerator.generate(size, random).level.countConnections(limit = 2))
            }
        }
    }
}
