package com.example.androiddemo.flow.model

import com.example.androiddemo.flow.levels.PACK_5X5
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FlowGameTest {
    @Test
    fun crossingAnotherPipeCutsIt() {
        val game = FlowGame(PACK_5X5[0])
        // Red runs down column 0 from (0,0) to (3,0), above blue's endpoint at (4,0).
        game.startDrag(Cell(0, 0))
        game.dragTo(Cell(3, 0))
        game.endDrag()
        // Blue from (1,1) goes left across red's pipe at (1,0).
        game.startDrag(Cell(1, 1))
        game.dragTo(Cell(1, 0))
        assertEquals(listOf(Cell(1, 1), Cell(1, 0)), game.paths['B'])
        assertEquals(listOf(Cell(0, 0)), game.paths['R'])
    }

    @Test
    fun cannotEnterOtherEndpoint() {
        val game = FlowGame(PACK_5X5[0])
        game.startDrag(Cell(0, 0))
        game.dragTo(Cell(0, 3))
        game.dragTo(Cell(0, 4)) // green endpoint
        assertEquals(Cell(0, 3), game.paths['R']!!.last())
    }

    @Test
    fun backtrackingTruncates() {
        val game = FlowGame(PACK_5X5[0])
        game.startDrag(Cell(0, 0))
        game.dragTo(Cell(0, 3))
        game.dragTo(Cell(0, 1))
        assertEquals(listOf(Cell(0, 0), Cell(0, 1)), game.paths['R'])
        assertFalse(game.isSolved)
    }

    @Test
    fun smallBoardsNeedEveryCellFilled() {
        val game = FlowGame(Level(0, listOf("R...R", ".....", ".....", ".....", ".....")))
        game.startDrag(Cell(0, 0))
        game.dragTo(Cell(0, 4))
        assertTrue(game.isConnected('R'))
        assertFalse(game.isSolved)
    }

    @Test
    fun tenByTenWinsOnceEveryFlowConnects() {
        val game = FlowGame(Level(0, listOf("R........R") + List(9) { ".........." }))
        game.startDrag(Cell(0, 0))
        game.dragTo(Cell(0, 9))
        assertTrue(game.isSolved)
    }
}
