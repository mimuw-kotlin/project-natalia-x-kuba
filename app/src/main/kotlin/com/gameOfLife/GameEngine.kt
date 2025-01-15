package com.gameOfLife

import java.util.concurrent.Semaphore

/**
 * The `GameEngine` class manages the Game of Life board and updates its state based on game rules.
 * It ensures thread-safe access to the board using a semaphore.
 */
object GameEngine {
    private var board: HashMap<Pair<Int, Int>, CellState> = hashMapOf()

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
        val currPosition = Pair(Settings.camera.first + cursor.first, Settings.camera.second + cursor.second)

        if (board.getOrDefault(currPosition, CellState.DEAD) == CellState.DEAD) {
            board.put(currPosition, CellState.ALIVE)
        } else {
            board.remove(currPosition)
        }

        boardMutext.release()
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
    fun calculateNewBoard() {
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

        boardMutext.acquire()

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
                if (count == 2 || count == 3) {
                    newBoard[cell.key] = CellState.ALIVE
                }
            } else {
                if (count == 3) {
                    newBoard[cell.key] = CellState.ALIVE
                }
            }
        }

        board = newBoard
        boardMutext.release()
        Screen.updateGameBoard(board)
    }
}
