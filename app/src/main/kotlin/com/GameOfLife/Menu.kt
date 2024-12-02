package app.src.main.kotlin.com.GameOfLife

import com.GameOfLife.Settings

abstract class Menu(val screen: Screen, val name: String) {
    var board: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.MENU_BOARD_COLS) { Pixel(" ") } }
    lateinit var children: Array<Pair<Menu, Int>>
    var line_to_hover = 0

    // Initialize board and handle 'name' initialization
    init {
        // Populate the board with default content
        board[1] = Pixel.createArray("                                       ")
        board[2] = Pixel.createArray("     _____ ____        ___  _          ")
        board[3] = Pixel.createArray("    |_   _|  _ \\ __ _ / _ \\| |         ")
        board[4] = Pixel.createArray("      | | | |_) / _` | | | | |         ")
        board[5] = Pixel.createArray("      | | |  _ < (_| | |_| | |___      ")
        board[6] = Pixel.createArray("      |_| |_| \\_\\__, |\\___/|_____|     ")
        board[7] = Pixel.createArray("                 |___/                 ")
        board[8] = Pixel.createArray("                                       ")

        board[10] = centerText("Feature not yet implemented!")
        board[12] = centerText("Consider donating!")
    }

    fun centerText(text: String, leftPadding: Int = 0): Array<Pixel> {
        val space = Settings.MENU_BOARD_COLS - text.length - leftPadding
        val leftSpace = space / 2
        return Pixel.createArray(" ".repeat(leftPadding + leftSpace) + text + " ".repeat(space - leftSpace))
    }

    fun display() {
        line_to_hover = 0
        screen.updateMenuBoard(board)
    }

    fun markHovered() {
        board[line_to_hover].forEach { it.setBgColor("grey") }
    }

    fun unmarkHovered() {
        board[line_to_hover].forEach { it.setBgColor("") }
    }

    abstract fun query(key: Char)
}