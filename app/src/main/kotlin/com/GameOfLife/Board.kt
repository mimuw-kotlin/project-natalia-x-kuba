package app.src.main.kotlin.com.GameOfLife

import com.GameOfLife.Settings

class Board(val screen: Screen){
    private var board: Array<Array<String>> = Array(Settings.ROWS) { Array(Settings.GAME_BOARD_COLS) { "." } }

    fun changeBoardPixel() {
        val cursor = screen.getCursor()

        if (board[cursor.first][cursor.second] == ".") {
            board[cursor.first][cursor.second] = "#"
        } else {
            board[cursor.first][cursor.second] = "."
        }

        screen.updateGameBoardPixel(cursor.first, cursor.second, board[cursor.first][cursor.second])
    }

    fun calculateNewBoard() {
        // TODO

        screen.updateGameBoard(board)
    }
}