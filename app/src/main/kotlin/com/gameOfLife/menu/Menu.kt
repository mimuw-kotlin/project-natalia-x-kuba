package com.gameOfLife.menu

import com.gameOfLife.Pixel
import com.gameOfLife.Screen
import com.gameOfLife.Settings

// Abstract class representing a Menu, which implements Clickable
abstract class Menu : Clickable {
    // 2D array representing the board where the menu items are drawn
    var board: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.MENU_BOARD_COLS) { Pixel(' ') } }

    // An array of Clickable elements (submenus or other options in the menu)
    abstract var children: Array<Clickable>

    // The index of the currently selected item (menu option)
    protected var cursor: Int = 0

    // Initialize board
    init {
        reset()
        // Populate the board with default content (static text and other items)
        board[1] = Pixel.createArray("                                       ")
        board[2] = Pixel.createArray("  _____ ____        ___  _     _       ")
        board[3] = Pixel.createArray(" |_   _|  _ \\ __ _ / _ \\| |   | |      ")
        board[4] = Pixel.createArray("   | | | |_) / _` | | | | |   | |      ")
        board[5] = Pixel.createArray("   | | |  _ < (_| | |_| | |___| |___   ")
        board[6] = Pixel.createArray("   |_| |_| \\_\\__, |\\___/|_____|_____|   ")
        board[7] = Pixel.createArray("              |___/                    ")
        board[8] = Pixel.createArray("                                       ")
    }

    /**
     * This function centers the provided text in the menu board with optional left padding.
     *
     * @param text The text to be centered.
     * @param leftPadding Optional padding for the text (default is 0).
     * @return A Pixel array representing the centered text.
     */
    fun centerText(
        text: String,
        leftPadding: Int = 0,
    ): Array<Pixel> {
        // Calculate the space needed for padding
        val space = Settings.MENU_BOARD_COLS - text.length - leftPadding
        val leftSpace = space / 2

        // Create a Pixel array with spaces on both sides of the text
        return Pixel.createArray(" ".repeat(leftPadding + leftSpace) + text + " ".repeat(space - leftSpace))
    }

    /**
     * Display the menu by resetting cursor and updating the screen.
     */
    fun display() {
        cursor = 0
        Screen.updateMenuBoard(board) // Update the screen with the current menu board
    }

    /**
     * Marks the currently hovered menu item by changing its background and text color.
     */
    fun markHovered() {
        // Change the background color and text color for the current hovered menu item
        board[cursor + 10].forEach { it.setBgColor("grey") }
        board[cursor + 10].forEach { it.setColor("black") }
    }

    /**
     * Unmarks the currently hovered menu item by resetting its colors.
     */
    fun unmarkHovered() {
        board[cursor + 10].forEach { it.setBgColor("") }
        board[cursor + 10].forEach { it.setColor("white") }
    }

    /**
     * Resets the menu to its initial state.
     */
    open fun reset() {
        unmarkHovered()
        cursor = 0

        // If there are children (submenus), mark the first one as hovered
        if (!this.children.isNullOrEmpty() && this.children.isNotEmpty()) {
            markHovered()
        }
    }
}
