package com.gameOfLife

import com.gameOfLife.menu.MainMenu
import com.gameOfLife.menu.Menu
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * The `Screen` class manages the visual aspects of the game, such as updating the game board, handling the menu,
 * managing the cursor, and displaying the screen output. It controls the display of both the game and menu states.
 */
object Screen {
    private var time = 0
    private var speed = 1
    private var cursor = Pair(0, 0)
    private val gameBoard: HashMap<Pair<Int, Int>, CellState> = hashMapOf()
    private var menuBoard = Array(Settings.ROWS) { Array(Settings.MAX_MENU_BOARD_COLS) { Pixel('?') } }
    private var gameOrMenu = GameOrMenu.GAME
    private var ad = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel('$') } }

    private val mutex = Mutex()
    private var menu: Menu = MainMenu

    /**
     * Initializes the screen by clearing it and setting the cursor position.
     * The screen is reset, and the cursor is highlighted.
     */
    public suspend fun initScreen() {
        mutex.withLock {
            // Clear the screen
            print("\u001b[H\u001b[2J")
            // Hide the cursor
            print("\u001B[?25l")
            System.out.flush()
        }
    }

    /**
     * Moves the cursor on the game board based on the direction.
     * The cursor moves according to the specified direction: "UP", "DOWN", "LEFT", or "RIGHT".
     * The position of the cursor is updated and displayed on the screen.
     *
     * @param direction The direction to move the cursor. It can be "UP", "DOWN", "LEFT", or "RIGHT".
     */
    public suspend fun moveCursor(direction: Action) {
        mutex.withLock {
            // Update cursor position based on direction
            when (direction) {
                Action.UP -> cursor = Pair((cursor.first - 1 + Settings.ROWS) % Settings.ROWS, cursor.second)
                Action.DOWN -> cursor = Pair((cursor.first + 1) % Settings.ROWS, cursor.second)
                Action.LEFT -> cursor = Pair(cursor.first, (cursor.second - 1 + Settings.gameBoardCols) % Settings.gameBoardCols)
                Action.RIGHT -> cursor = Pair(cursor.first, (cursor.second + 1) % Settings.gameBoardCols)
                else -> {}
            }
        }

        this.updateScreen()
    }

    public suspend fun moveCamera(direction: Action) {
        mutex.withLock {
            // Update camera position based on direction
            when (direction) {
                Action.CAM_UP -> {
                    Settings.camera = Pair(Settings.camera.first + 1, Settings.camera.second)
                }
                Action.CAM_DOWN -> {
                    Settings.camera = Pair(Settings.camera.first - 1, Settings.camera.second)
                }
                Action.CAM_LEFT -> {
                    Settings.camera = Pair(Settings.camera.first, Settings.camera.second + 1)
                }
                Action.CAM_RIGHT -> {
                    Settings.camera = Pair(Settings.camera.first, Settings.camera.second - 1)
                }
                else -> {}
            }
        }

        this.updateScreen()
    }

    /**
     * Exits the screen by clearing it and displaying a thank-you message.
     */
    public suspend fun exitScreen() {
        mutex.withLock {
            // Clear the screen and reset the cursor position
            print("\u001B[2J")
            print("\u001B[H")
            repeat(Settings.ROWS + 3) { print("\r\u001b[1A") }
            print("\u001b[2K")
            println("\rThank you for playing!!")
            System.out.flush()

            // Show the cursor again
            print("\u001B[?25h")
        }
    }

    /**
     * Updates the screen by printing the current game and menu boards, along with the time and speed.
     * It also handles the display of ads and board content, updating the view based on whether the user is in the game or menu state.
     */
    public suspend fun updateScreen(hardReset: Boolean = false) {
        mutex.withLock {
            repeat(Settings.ROWS + 3) { print("\r\u001b[1A") }
            if (hardReset) {
                print("\u001b[H\u001b[2J")
            }
            val timeText = getTime()
            val speedText = getSpeed()

            // Print top row
            val width = 2 * Settings.gameBoardCols + Settings.AD_COLS + 2
            val spaces = width - 10 - 12 - 8 // len for "TRgOLL NxK", timeText and speedText
            val leftPad = " ".repeat(spaces / 2)
            val rightPad = " ".repeat(spaces - spaces / 2)
            print("TRgOLL NxK" + leftPad + timeText + rightPad + "Speed: $speedText\n\r")

            repeat(width) { print("-") }
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
                    for (j in 0 until Settings.gameBoardCols) {
                        val currPosition = Pair(i + Settings.camera.first, j + Settings.camera.second)
                        val color = if (cursor == Pair(i, j)) "grey" else ""
                        val px = Pixel(Settings.getCellStateChar(gameBoard.getOrDefault(currPosition, CellState.DEAD)))
                        px.setBgColor(color)
                        print(px.getValue())
                        print(" ")
                    }
                    print("\u001B[D|\n\r")
                } else if (gameOrMenu == GameOrMenu.MENU) {
                    // Print the menu board
                    for (j in 0 until Settings.menuBoardCols) {
                        print(menuBoard[i][j].getValue())
                    }
                    print("|\n\r")
                }
            }

            repeat(2 * Settings.gameBoardCols + Settings.AD_COLS + 2) { print("-") }
            println()

            System.out.flush()
        }
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
    public suspend fun updateTime(time: Int) {
        mutex.withLock {
            this.time = time
        }

        this.updateScreen()
    }

    /**
     * Updates the speed value on the screen and refreshes the screen.
     *
     * @param speed The new speed value to display on the screen.
     */
    public suspend fun updateSpeed(speed: Int) {
        mutex.withLock {
            this.speed = speed
        }

        this.updateScreen()
    }

    /**
     * Changes the ad board content to the new ad.
     *
     * @param ad The new ad board content to display.
     */
    public suspend fun changeAd(ad: Array<Array<Pixel>>) {
        mutex.withLock {
            this.ad = ad
        }

        this.updateScreen()
    }

    /**
     * Updates a specific pixel on the game board at the given coordinates.
     *
     * @param x The row index of the pixel to update.
     * @param y The column index of the pixel to update.
     * @param cellState The new cellState to set for the pixel (e.g., a character representing alive or dead cells).
     */
    public suspend fun updateGameBoardPixel(
        position: Pair<Int, Int>,
        cellState: CellState,
    ) {
        mutex.withLock {
            if (cellState == CellState.DEAD) {
                gameBoard.remove(position)
            } else {
                gameBoard[position] = cellState
            }
        }

        this.updateScreen()
    }

    /**
     * Updates the entire game board with a new set of values.
     *
     * @param board A 2D array of strings representing the new game board.
     */
    public suspend fun updateGameBoard(board: HashMap<Pair<Int, Int>, CellState>) {
        mutex.withLock {
            gameBoard.clear()

            for ((position, cellState) in board) {
                gameBoard[position] = cellState
            }
        }

        this.updateScreen()
    }

    /**
     * Updates the menu board with a new set of pixels.
     *
     * @param board A 2D array of `Pixel` objects representing the new menu board.
     */
    public suspend fun updateMenuBoard(board: Array<Array<Pixel>>) {
        mutex.withLock {
            for (i in 0 until Settings.ROWS) {
                for (j in 0 until Settings.menuBoardCols) {
                    menuBoard[i][j] = board[i][j]
                }
            }
        }

        this.updateScreen()
    }

    /**
     * Switches between the game and menu views. If currently in the game view, it switches to the menu,
     * and vice versa.
     */
    public suspend fun switchGameOrMenu() {
        mutex.withLock {
            if (gameOrMenu == GameOrMenu.GAME) {
                gameOrMenu = GameOrMenu.MENU
            } else {
                gameOrMenu = GameOrMenu.GAME
            }
        }

        if (gameOrMenu == GameOrMenu.MENU) {
            menu = MainMenu
            menu.reset()
            menu.display()
        } else {
            this.updateScreen()
        }
    }

    public suspend fun setMenu(menu: Menu) {
        this.menu = menu
        menu.reset()
        menu.display()
    }

    public suspend fun increaseBoardWidth() {
        if (!Settings.changeWidth(1)) return

        this.updateScreen(true)
    }

    public suspend fun decreaseBoardWidth() {
        if (!Settings.changeWidth(-1)) return

        this.updateScreen(true)
    }
}
