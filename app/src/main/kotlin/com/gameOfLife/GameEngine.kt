package com.gameOfLife

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * The `GameEngine` class manages the Game of Life board and updates its state based on game rules.
 * It ensures thread-safe access to the board using a semaphore.
 */
object GameEngine {
    private var board: HashMap<Pair<Int, Int>, CellState> = hashMapOf()

    // Semaphore to manage thread-safe access to the board
    private val boardMutex = Mutex()

    /**
     * Toggles the state of a pixel on the board based on the current cursor position.
     * If the cell is dead (.), it becomes alive (#), and vice versa.
     * Updates the corresponding pixel on the screen.
     */
    suspend fun changeBoardPixel() {
        val currPosition: Pair<Int, Int>

        boardMutex.withLock {
            val cursor = Screen.getCursor()
            currPosition = Pair(Settings.camera.first + cursor.first, Settings.camera.second + cursor.second)

            if (board.getOrDefault(currPosition, CellState.DEAD) == CellState.DEAD) {
                board.put(currPosition, CellState.ALIVE)
            } else {
                board.remove(currPosition)
            }
        }
        Screen.updateGameBoardPixel(currPosition, board.getOrDefault(currPosition, CellState.DEAD))
    }

    /**
     * Calculates the new state of the board based on the Game of Life rules:
     * - Any live cell with fewer than two live neighbors dies (underpopulation).
     * - Any live cell with more than three live neighbors dies (overpopulation).
     * - Any live cell with two or three live neighbors lives.
     * - Any dead cell with exactly three live neighbors becomes alive (reproduction).
     * Updates the screen with the new board state.
     */
    suspend fun calculateNewBoard() {
        var newBoard: HashMap<Pair<Int, Int>, CellState> = hashMapOf()
        var countBoard: HashMap<Pair<Int, Int>, Int> = hashMapOf()

        val directions =
            listOf(
                Pair(-1, -1),
                Pair(-1, 0),
                Pair(-1, 1),
                Pair(0, -1),
                Pair(0, 1),
                Pair(1, -1),
                Pair(1, 0),
                Pair(1, 1),
            )

        boardMutex.withLock {
            for (cell in board) {
                for (direction in directions) {
                    val newRow = cell.key.first + direction.first
                    val newCol = cell.key.second + direction.second

                    countBoard[Pair(newRow, newCol)] = countBoard.getOrDefault(Pair(newRow, newCol), 0) + 1
                }
            }

            for (cell in countBoard) {
                val count = cell.value
                val state = board.getOrDefault(cell.key, CellState.DEAD)

                if (state == CellState.ALIVE) {
                    if (count in Settings.getCellRange(CellRange.ALIVE_LOW)..Settings.getCellRange(CellRange.ALIVE_HIGH)) {
                        newBoard[cell.key] = CellState.ALIVE
                    }
                } else {
                    if (count in Settings.getCellRange(CellRange.DEAD_LOW)..Settings.getCellRange(CellRange.DEAD_HIGH)) {
                        newBoard[cell.key] = CellState.ALIVE
                    }
                }
            }
            board = newBoard
        }

        Screen.updateGameBoard(board)
    }
}
