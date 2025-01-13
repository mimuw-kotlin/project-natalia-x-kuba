package com.gameOfLife

import java.util.concurrent.Semaphore

/**
 * The `GameEngine` class manages the Game of Life board and updates its state based on game rules.
 * It ensures thread-safe access to the board using a semaphore.
 */
object GameEngine {
    // 2D array representing the game board, initialized with dead cells (".")
    private var board: Array<Array<CellState>> = Array(Settings.ROWS) { Array(Settings.GAME_BOARD_COLS) { CellState.DEAD } }

    // Semaphore to manage thread-safe access to the board
    private val boardMutext = Semaphore(1, true)

    /**
     * Toggles the state of a pixel on the board based on the current cursor position.
     * If the cell is dead (.), it becomes alive (#), and vice versa.
     * Updates the corresponding pixel on the screen.
     */
    fun changeBoardPixel() {
        boardMutext.acquire()
        val cursor = Screen.getCursor()

        if (board[cursor.first][cursor.second] == CellState.DEAD) {
            board[cursor.first][cursor.second] = CellState.ALIVE
        } else {
            board[cursor.first][cursor.second] = CellState.DEAD
        }

        boardMutext.release()
        Screen.updateGameBoardPixel(cursor.first, cursor.second, board[cursor.first][cursor.second])
    }

    /**
     * Calculates the new state of the board based on the Game of Life rules:
     * - Any live cell with fewer than two live neighbors dies (underpopulation).
     * - Any live cell with more than three live neighbors dies (overpopulation).
     * - Any live cell with two or three live neighbors lives.
     * - Any dead cell with exactly three live neighbors becomes alive (reproduction).
     * Updates the screen with the new board state.
     */
    fun calculateNewBoard() {
        var newBoard: Array<Array<CellState>> = Array(Settings.ROWS) { Array(Settings.GAME_BOARD_COLS) { CellState.DEAD } }

        boardMutext.acquire()

        // Iterate through each cell on the board
        for (row in board.indices) {
            for (col in board[row].indices) {
                val liveCount = countLiveNeighbors(row, col)

                // Apply Game of Life rules to determine the state of the cell
                newBoard[row][col] =
                    when {
                        board[row][col] == CellState.ALIVE && (liveCount < 2 || liveCount > 3) -> CellState.DEAD
                        board[row][col] == CellState.ALIVE && (liveCount == 2 || liveCount == 3) -> CellState.ALIVE
                        board[row][col] == CellState.DEAD && liveCount == 3 -> CellState.ALIVE
                        else -> CellState.DEAD
                    }
            }
        }

        board = newBoard // Update the board with the new state
        boardMutext.release() // Release semaphore after update
        Screen.updateGameBoard(board) // Update the screen with the new board state
    }

    /**
     * Counts the number of live neighbors surrounding a cell at the given row and column.
     *
     * @param row the row index of the cell.
     * @param col the column index of the cell.
     * @return the number of live neighbors.
     */
    private fun countLiveNeighbors(
        row: Int,
        col: Int,
    ): Int {
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

        var liveCount = 0

        // Iterate through each direction to check for live neighbors
        for (direction in directions) {
            val newRow = row + direction.first
            val newCol = col + direction.second

            // Skip cells that are out of bounds
            if (newRow < 0 || newRow >= Settings.ROWS || newCol < 0 || newCol >= Settings.GAME_BOARD_COLS) {
                continue
            }

            // Increment liveCount if the neighboring cell is alive ("#")
            if (board[newRow][newCol] == CellState.ALIVE) {
                liveCount++
            }
        }

        return liveCount
    }
}
