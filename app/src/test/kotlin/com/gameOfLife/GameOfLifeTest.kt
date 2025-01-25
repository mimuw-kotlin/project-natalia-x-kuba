package com.gameOfLife

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class GameOfLifeTest {
    @BeforeTest
    fun resetBoard() {
        runBlocking {
            GameEngine.clearBoard()
        }
    }

    // Basic rule validation tests
    // Empty grid
    @Test
    fun emptyGridTest() {
        val emptyGrid = hashMapOf<Pair<Int, Int>, CellState>()

        runBlocking {
            for (i in 0 until 10) {
                GameEngine.calculateNewBoard()
                assertEquals(emptyGrid, GameEngine.getBoard())
            }
        }
    }

    // Stable grid:
    @Test
    fun block2x2() {
        val positions =
            listOf(
                Pair(0, 0),
                Pair(0, 1),
                Pair(1, 0),
                Pair(1, 1),
            )

        val block2x2 = positions.associateWith { CellState.ALIVE }.toMutableMap()

        runBlocking {
            for (position in positions) {
                GameEngine.proxyChangeBoardPixel(position)
            }

            for (i in 0 until 10) {
                GameEngine.calculateNewBoard()
                assertEquals(block2x2, GameEngine.getBoard())
            }
        }
    }

    @Test
    fun bentPaperclip() {
        val positions =
            listOf(
                Pair(0, 2),
                Pair(0, 3),
                Pair(1, 1),
                Pair(1, 4),
                Pair(2, 1),
                Pair(2, 3),
                Pair(2, 4),
                Pair(3, 0),
                Pair(3, 1),
                Pair(3, 3),
                Pair(4, 0),
                Pair(4, 3),
                Pair(5, 1),
                Pair(5, 2),
            )

        val bentPaperClip = positions.associateWith { CellState.ALIVE }.toMutableMap()

        runBlocking {
            for (position in positions) {
                GameEngine.proxyChangeBoardPixel(position)
            }

            for (i in 0 until 10) {
                GameEngine.calculateNewBoard()
                assert(bentPaperClip == GameEngine.getBoard())
            }
        }
    }

    // Oscillators:
    @Test
    fun blinker() {
        val positions =
            listOf(
                Pair(0, 1),
                Pair(1, 1),
                Pair(2, 1),
            )

        val blinker1 = positions.associateWith { CellState.ALIVE }.toMap()
        val blinker2 =
            hashMapOf(
                Pair(1, 0) to CellState.ALIVE,
                Pair(1, 1) to CellState.ALIVE,
                Pair(1, 2) to CellState.ALIVE,
            )

        runBlocking {
            for (position in positions) {
                GameEngine.proxyChangeBoardPixel(position)
            }

            for (i in 0 until 10) {
                GameEngine.calculateNewBoard()
                if (i % 2 == 0) {
                    assertEquals(blinker2, GameEngine.getBoard())
                } else {
                    assertEquals(blinker1, GameEngine.getBoard())
                }
            }
        }
    }

    @Test
    fun octagon2() {
        val period1 =
            listOf(
                Pair(8, 9),
                Pair(4, 5),
                Pair(5, 6),
                Pair(7, 8),
                Pair(8, 10),
                Pair(5, 7),
                Pair(6, 8),
                Pair(3, 5),
                Pair(4, 8),
                Pair(5, 9),
                Pair(5, 10),
                Pair(3, 8),
                Pair(10, 5),
                Pair(8, 3),
                Pair(9, 5),
                Pair(8, 4),
                Pair(5, 3),
                Pair(10, 8),
                Pair(7, 5),
                Pair(8, 6),
                Pair(9, 8),
                Pair(5, 4),
                Pair(6, 5),
                Pair(8, 7),
            )

        val period2 =
            listOf(
                Pair(4, 5),
                Pair(8, 9),
                Pair(5, 6),
                Pair(7, 8),
                Pair(5, 7),
                Pair(6, 8),
                Pair(4, 8),
                Pair(5, 9),
                Pair(9, 5),
                Pair(8, 4),
                Pair(7, 5),
                Pair(8, 6),
                Pair(9, 8),
                Pair(5, 4),
                Pair(6, 5),
                Pair(8, 7),
            )

        val period3 =
            listOf(
                Pair(4, 5),
                Pair(8, 9),
                Pair(5, 6),
                Pair(7, 8),
                Pair(4, 6),
                Pair(5, 7),
                Pair(6, 8),
                Pair(7, 9),
                Pair(4, 7),
                Pair(6, 9),
                Pair(4, 8),
                Pair(5, 9),
                Pair(9, 5),
                Pair(8, 4),
                Pair(9, 6),
                Pair(7, 4),
                Pair(9, 7),
                Pair(6, 4),
                Pair(7, 5),
                Pair(8, 6),
                Pair(5, 4),
                Pair(9, 8),
                Pair(6, 5),
                Pair(8, 7),
            )

        val period4 =
            listOf(
                Pair(4, 5),
                Pair(8, 9),
                Pair(3, 6),
                Pair(7, 10),
                Pair(4, 8),
                Pair(5, 9),
                Pair(6, 10),
                Pair(3, 7),
                Pair(9, 5),
                Pair(10, 6),
                Pair(7, 3),
                Pair(8, 4),
                Pair(10, 7),
                Pair(6, 3),
                Pair(5, 4),
                Pair(9, 8),
            )

        val period5 =
            listOf(
                Pair(4, 5),
                Pair(8, 9),
                Pair(4, 6),
                Pair(7, 9),
                Pair(4, 7),
                Pair(6, 9),
                Pair(3, 6),
                Pair(7, 10),
                Pair(4, 8),
                Pair(5, 9),
                Pair(6, 10),
                Pair(3, 7),
                Pair(9, 5),
                Pair(10, 6),
                Pair(7, 3),
                Pair(8, 4),
                Pair(9, 6),
                Pair(10, 7),
                Pair(6, 3),
                Pair(7, 4),
                Pair(9, 7),
                Pair(6, 4),
                Pair(5, 4),
                Pair(9, 8),
            )

        val octacon1: Map<Pair<Int, Int>, CellState> = period1.associateWith { CellState.ALIVE }.toMap()
        val octacon2: Map<Pair<Int, Int>, CellState> = period2.associateWith { CellState.ALIVE }.toMap()
        val octacon3: Map<Pair<Int, Int>, CellState> = period3.associateWith { CellState.ALIVE }.toMap()
        val octacon4: Map<Pair<Int, Int>, CellState> = period4.associateWith { CellState.ALIVE }.toMap()
        val octacon5: Map<Pair<Int, Int>, CellState> = period5.associateWith { CellState.ALIVE }.toMap()

        runBlocking {
            for (position in period1) {
                GameEngine.proxyChangeBoardPixel(position)
            }

            for (i in 0 until 10) {
                GameEngine.calculateNewBoard()
                when (i % 5) {
                    0 -> assertEquals(octacon2, GameEngine.getBoard())
                    1 -> assertEquals(octacon3, GameEngine.getBoard())
                    2 -> assertEquals(octacon4, GameEngine.getBoard())
                    3 -> assertEquals(octacon5, GameEngine.getBoard())
                    4 -> assertEquals(octacon1, GameEngine.getBoard())
                }
            }
        }
    }

    
}
