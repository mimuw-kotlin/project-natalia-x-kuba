package app.src.main.kotlin.com.GameOfLife

import com.gameOfLife.Settings
import java.util.concurrent.Semaphore

class Board(val screen: Screen) {
    private var board: Array<Array<String>> = Array(Settings.ROWS) { Array(Settings.GAME_BOARD_COLS) { "." } }
    private val board_mutext = Semaphore(1, true)

    fun changeBoardPixel() {
        board_mutext.acquire()
        val cursor = screen.getCursor()

        if (board[cursor.first][cursor.second] == ".") {
            board[cursor.first][cursor.second] = "#"
        } else {
            board[cursor.first][cursor.second] = "."
        }

        board_mutext.release()
        screen.updateGameBoardPixel(cursor.first, cursor.second, board[cursor.first][cursor.second])
    }

    fun calculateNewBoard() {
        var newBoard: Array<Array<String>> = Array(Settings.ROWS) { Array(Settings.GAME_BOARD_COLS) { "." } }

        board_mutext.acquire()
        for (row in board.indices) {
            for (col in board[row].indices) {
                val liveCount = countLiveNeighbors(row, col)

                if (board[row][col] == "#" && (liveCount < 2 || liveCount > 3)) {
                    // Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                    // Any live cell with more than three live neighbours dies, as if by overpopulation.
                    newBoard[row][col] = "."
                } else if (board[row][col] == "#" && (liveCount == 2 || liveCount == 3)) {
                    // Any live cell with two or three live neighbours lives on to the next generation.

                    newBoard[row][col] = "#"
                } else if (board[row][col] == "." && liveCount == 3) {
                    // Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                    newBoard[row][col] = "#"
                } else {
                    newBoard[row][col] = "."
                }
            }
        }

        board = newBoard
        board_mutext.release()
        screen.updateGameBoard(board)
    }

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

        for (direction in directions) {
            val newRow = row + direction.first
            val newCol = col + direction.second

            if (newRow < 0 || newRow >= Settings.ROWS || newCol < 0 || newCol >= Settings.GAME_BOARD_COLS) {
                continue
            }

            if (board[newRow][newCol] == "#") {
                liveCount++
            }
        }

        return liveCount
    }
}
