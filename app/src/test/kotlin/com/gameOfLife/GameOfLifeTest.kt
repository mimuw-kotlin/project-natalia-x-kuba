package com.gameOfLife

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.io.File
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
    fun manyTests() {
        val inputDirectoryPath = "src/test/kotlin/com/gameOfLife/testInputs"
        val outputDirectoryPath = "src/test/kotlin/com/gameOfLife/testOutputs"

        val inputDirectory = File(inputDirectoryPath)
        val outputDirectory = File(outputDirectoryPath)

        inputDirectory.listFiles { _, name -> name.endsWith(".in") }?.forEach { inputFile ->
            val inputFileName = inputFile.name
            val correspondingOutputFileName = inputFileName.replace(".in", ".out")
            val correspondingOutputFile = File(outputDirectory, correspondingOutputFileName)

            if (correspondingOutputFile.exists()) {
                val expectedOutput = correspondingOutputFile.readText()
                val input = inputFile.readText()

                val inputLines = input.lines()
                val expectedOutputLines = expectedOutput.lines()

                val board = hashMapOf<Pair<Int, Int>, CellState>()
                for (i in inputLines.indices - 1) {
                    for (j in inputLines[i].indices) {
                        if (inputLines[i][j] == '#') {
                            board[Pair(i, j / 2)] = CellState.ALIVE
                        }
                    }
                }

                println("Running test for $inputFileName")

                val numberOfIterations = inputLines.last().toInt()
                println("Number of iterations: $numberOfIterations")

                runBlocking {
                    resetBoard()

                    for ((position, state) in board) {
                        if (state == CellState.ALIVE) {
                            GameEngine.proxyChangeBoardPixel(position)
                        }
                    }

                    for (i in 0 until numberOfIterations) {
                        GameEngine.calculateNewBoard()
                    }

                    val actualOutput = StringBuilder()
                    for (i in 0 until Settings.ROWS) {
                        for (j in 0 until Settings.gameBoardCols) {
                            val currPosition = Pair(i, j)
                            if (GameEngine.getBoard().containsKey(currPosition)) {
                                actualOutput.append("# ")
                            } else {
                                actualOutput.append(". ")
                            }
                        }
                        actualOutput.append(System.lineSeparator())
                    }

                    assertEquals(expectedOutputLines, actualOutput.toString().lines())
                }
            }
        }
    }
}
