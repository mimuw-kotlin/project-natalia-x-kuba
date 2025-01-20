package com.gameOfLife.menu

/**
 * The `Clickable` interface represents a UI element that can respond to user input (clicks or key presses).
 */
interface Clickable {
    var parent: Menu?
    var text: String

    /**
     * Handles user input by processing a specific key press.
     *
     * @param key the character key pressed by the user.
     */
    suspend fun query(key: Char)
}
