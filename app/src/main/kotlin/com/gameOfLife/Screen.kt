package com.gameOfLife

import com.gameOfLife.menu.MainMenu
import com.gameOfLife.menu.Menu
import java.util.concurrent.Semaphore

/**
 * The `Screen` class manages the visual aspects of the game, such as updating the game board, handling the menu,
 * managing the cursor, and displaying the screen output. It controls the display of both the game and menu states.
 */
object Screen {
    private var time = 0
    private var speed = 1
    private var cursor = Pair(0, 0)
    private var gameBoard = Array(Settings.ROWS) { Array(Settings.GAME_BOARD_COLS) { Pixel(Settings.getCellStateChar(CellState.DEAD)) } }
    private var menuBoard = Array(Settings.ROWS) { Array(Settings.MENU_BOARD_COLS) { Pixel('?') } }
    private var gameOrMenu = GameOrMenu.GAME
    private var ad = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel('$') } }

    private val mutex = Semaphore(1, true)
    private var menu: Menu = MainMenu

    /**
     * Initializes the screen by clearing it and setting the cursor position.
     * The screen is reset, and the cursor is highlighted.
     */
    public fun initScreen() {
        mutex.acquire()

        // Clear the screen
        print("\u001b[H\u001b[2J")
        // Hide the cursor
        print("\u001B[?25l")
        // Set the background color of the cursor to grey
        gameBoard[cursor.first][cursor.second].setBgColor("grey")
        System.out.flush()

        mutex.release()
    }

    /**
     * Moves the cursor on the game board based on the direction.
     * The cursor moves according to the specified direction: "UP", "DOWN", "LEFT", or "RIGHT".
     * The position of the cursor is updated and displayed on the screen.
     *
     * @param direction The direction to move the cursor. It can be "UP", "DOWN", "LEFT", or "RIGHT".
     */
    public fun moveCursor(direction: Action) {
        mutex.acquire()

        // Remove grey background from the current cursor position
        gameBoard[cursor.first][cursor.second].setBgColor("")

        // Update cursor position based on direction
        when (direction) {
            Action.UP -> cursor = Pair((cursor.first - 1 + Settings.ROWS) % Settings.ROWS, cursor.second)
            Action.DOWN -> cursor = Pair((cursor.first + 1) % Settings.ROWS, cursor.second)
            Action.LEFT -> cursor = Pair(cursor.first, (cursor.second - 1 + Settings.GAME_BOARD_COLS) % Settings.GAME_BOARD_COLS)
            Action.RIGHT -> cursor = Pair(cursor.first, (cursor.second + 1) % Settings.GAME_BOARD_COLS)
            else -> {}
        }

        // Set the new position of the cursor with grey background
        gameBoard[cursor.first][cursor.second].setBgColor("grey")

        mutex.release()
        this.updateScreen()
    }

    /**
     * Exits the screen by clearing it and displaying a thank-you message.
     */
    public fun exitScreen() {
        mutex.acquire()

        // Clear the screen and reset the cursor position
        print("\u001B[2J")
        print("\u001B[H")
        repeat(Settings.ROWS + 3) { print("\r\u001b[1A") }
        print("\u001b[2K")
        println("\rThank you for playing!!")
        System.out.flush()

        // Show the cursor again
        print("\u001B[?25h")
        mutex.release()
    }

    /**
     * Updates the screen by printing the current game and menu boards, along with the time and speed.
     * It also handles the display of ads and board content, updating the view based on whether the user is in the game or menu state.
     */
    public fun updateScreen() {
        mutex.acquire()

        repeat(Settings.ROWS + 3) { print("\r\u001b[1A") }
        val timeText = getTime()
        val speedText = getSpeed()

        // print top row
        print("TRgOLL NxK  \t\t$timeText\t\t  Speed: $speedText\n\r")

        repeat(2 * Settings.GAME_BOARD_COLS + Settings.AD_COLS + 2) { print("-") }
        print("\n\r")

        // Print the boards: ad board, game or menu board
        for (i in 0 until Settings.ROWS) {
            print("\r|")

            // Print ad board
            for (j in 0 until Settings.AD_COLS) {
                print(ad[i][j].getValue())
            }

            print("|")
            if (gameOrMenu == GameOrMenu.GAME) {
                // Print the game board
                for (j in 0 until Settings.GAME_BOARD_COLS) {
                    print(gameBoard[i][j].getValue())
                    print(" ")
                }
                print("\u001B[D|\n\r")
            } else if (gameOrMenu == GameOrMenu.MENU) {
                // Print the menu board
                for (j in 0 until Settings.MENU_BOARD_COLS) {
                    print(menuBoard[i][j].getValue())
                }
                print("|\n\r")
            }
        }

        repeat(2 * Settings.GAME_BOARD_COLS + Settings.AD_COLS + 2) { print("-") }
        println()

        System.out.flush()
        mutex.release()
    }

    /**
     * Returns the current time in a formatted string (HH:mm:ss).
     *
     * @return The formatted string representing the time elapsed in the game.
     */
    private fun getTime(): String {
        val locTime = time
        val hours = locTime / 3600
        val minutes = (locTime % 3600) / 60
        val seconds = locTime % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /**
     * Returns a string representation of the current speed, shown as a progress bar.
     *
     * @return A string representing the current speed in a visual form (e.g., "#####").
     */
    private fun getSpeed(): String {
        return when (speed) {
            0 -> "-----"
            1 -> "#----"
            2 -> "##---"
            3 -> "###--"
            4 -> "####-"
            5 -> "#####"
            else -> "?????"
        }
    }

    /**
     * Returns the current position of the cursor on the game board.
     *
     * @return A pair representing the (row, column) of the cursor.
     */
    public fun getCursor(): Pair<Int, Int> {
        return cursor
    }

    /**
     * Updates the time value on the screen and refreshes the screen.
     *
     * @param time The new time value to display on the screen (in seconds).
     */
    public fun updateTime(time: Int) {
        mutex.acquire()

        this.time = time

        mutex.release()
        this.updateScreen()
    }

    /**
     * Updates the speed value on the screen and refreshes the screen.
     *
     * @param speed The new speed value to display on the screen.
     */
    public fun updateSpeed(speed: Int) {
        mutex.acquire()

        this.speed = speed

        mutex.release()
        this.updateScreen()
    }

    /**
     * Changes the ad board content to the new ad.
     *
     * @param ad The new ad board content to display.
     */
    public fun changeAd(ad: Array<Array<Pixel>>) {
        mutex.acquire()

        this.ad = ad

        mutex.release()
        this.updateScreen()
    }

    /**
     * Updates a specific pixel on the game board at the given coordinates.
     *
     * @param x The row index of the pixel to update.
     * @param y The column index of the pixel to update.
     * @param cellState The new cellState to set for the pixel (e.g., a character representing alive or dead cells).
     */
    public fun updateGameBoardPixel(
        x: Int,
        y: Int,
        cellState: CellState,
    ) {
        mutex.acquire()

        gameBoard[x][y].setCharacter(Settings.getCellStateChar(cellState))

        mutex.release()
        this.updateScreen()
    }

    /**
     * Updates the entire game board with a new set of values.
     *
     * @param board A 2D array of strings representing the new game board.
     */
    public fun updateGameBoard(board: Array<Array<CellState>>) {
        mutex.acquire()

        for (i in 0 until Settings.ROWS) {
            for (j in 0 until Settings.GAME_BOARD_COLS) {
                gameBoard[i][j].setCharacter(Settings.getCellStateChar(board[i][j]))
            }
        }

        mutex.release()
        this.updateScreen()
    }

    /**
     * Updates the menu board with a new set of pixels.
     *
     * @param board A 2D array of `Pixel` objects representing the new menu board.
     */
    public fun updateMenuBoard(board: Array<Array<Pixel>>) {
        mutex.acquire()

        for (i in 0 until Settings.ROWS) {
            for (j in 0 until Settings.MENU_BOARD_COLS) {
                menuBoard[i][j] = board[i][j]
            }
        }

        mutex.release()
        this.updateScreen()
    }

    /**
     * Switches between the game and menu views. If currently in the game view, it switches to the menu,
     * and vice versa.
     */
    public fun switchGameOrMenu() {
        mutex.acquire()

        if (gameOrMenu == GameOrMenu.GAME) {
            gameOrMenu = GameOrMenu.MENU
        } else {
            gameOrMenu = GameOrMenu.GAME
        }

        mutex.release()

        if (gameOrMenu == GameOrMenu.MENU) {
            menu = MainMenu
            menu.reset()
            menu.display()
        } else {
            this.updateScreen()
        }
    }

    public fun setMenu(menu: Menu) {
        this.menu = menu
        menu.reset()
        menu.display()
    }
}
