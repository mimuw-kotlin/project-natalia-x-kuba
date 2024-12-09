package com.gameOfLife

import app.src.main.kotlin.com.GameOfLife.Pixel

abstract class Menu : Clickable {
    var board: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.MENU_BOARD_COLS) { Pixel(" ") } }
    abstract var children: Array<Clickable>
    lateinit var screen: Screen
    protected var cursor: Int = 0

    // Initialize board
    init {
        reset()
        // Populate the board with default content
        board[1] = Pixel.createArray("                                       ")
        board[2] = Pixel.createArray("  _____ ____        ___  _     _       ")
        board[3] = Pixel.createArray(" |_   _|  _ \\ __ _ / _ \\| |   | |      ")
        board[4] = Pixel.createArray("   | | | |_) / _` | | | | |   | |      ")
        board[5] = Pixel.createArray("   | | |  _ < (_| | |_| | |___| |___   ")
        board[6] = Pixel.createArray("   |_| |_| \\_\\__, |\\___/|_____|_____|   ")
        board[7] = Pixel.createArray("              |___/                    ")
        board[8] = Pixel.createArray("                                       ")

        board[10] = centerText("Feature not yet implemented!")
        board[12] = centerText("Consider donating!")
    }

    fun centerText(
        text: String,
        leftPadding: Int = 0,
    ): Array<Pixel> {
        val space = Settings.MENU_BOARD_COLS - text.length - leftPadding
        val leftSpace = space / 2
        return Pixel.createArray(" ".repeat(leftPadding + leftSpace) + text + " ".repeat(space - leftSpace))
    }

    fun display() {
        cursor = 0
        screen.updateMenuBoard(board)
    }

    fun markHovered() {
        board[cursor + 10].forEach { it.setBgColor("grey") }
        board[cursor + 10].forEach { it.setColor("black") }
    }

    fun unmarkHovered() {
        board[cursor + 10].forEach { it.setBgColor("") }
        board[cursor + 10].forEach { it.setColor("white") }
    }

    open fun reset() {
        unmarkHovered()
        cursor = 0
        if (!this.children.isNullOrEmpty() && this.children.isNotEmpty()) {
            markHovered()
        }
    }
}
