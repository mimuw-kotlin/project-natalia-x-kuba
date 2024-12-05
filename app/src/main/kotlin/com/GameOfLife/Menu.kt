package app.src.main.kotlin.com.GameOfLife

import com.GameOfLife.Clickable
import com.GameOfLife.Settings

abstract class Menu : Clickable {
    var board: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.MENU_BOARD_COLS) { Pixel(" ") } }
    abstract var children: Array<Pair<Clickable, Int>>
    lateinit var screen: Screen
    private var lineToHover = 0

    // Initialize board
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
        lineToHover = 0
        screen.updateMenuBoard(board)
    }

    fun markHovered() {
        board[lineToHover].forEach { it.setBgColor("grey") }
    }

    fun unmarkHovered() {
        board[lineToHover].forEach { it.setBgColor("") }
    }
}