package app.src.main.kotlin.com.GameOfLife

class Board(val screen: Screen){
    private val ROWS = 20
    private val COLUMNS = 20
    private var board: Array<Array<String>> = Array(ROWS) { Array(COLUMNS) { "." } }

    fun changeBoardPixel() {
        val cursor = screen.getCursor()

        if (board[cursor.first][cursor.second] == ".") {
            board[cursor.first][cursor.second] = "#"
        } else {
            board[cursor.first][cursor.second] = "."
        }

        screen.updateGameBoardPixel(cursor.first, cursor.second, board[cursor.first][cursor.second])
    }
}