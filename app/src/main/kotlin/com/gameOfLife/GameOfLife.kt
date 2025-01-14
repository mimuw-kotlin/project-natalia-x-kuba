package com.gameOfLife

import com.gameOfLife.ads.AdManager
import com.gameOfLife.menu.MainMenu
import com.gameOfLife.menu.SelectMenu

fun main() {
    // Apologize for windows
    if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
        println("Sorry, this program is not supported on Windows.")
        return
    }

    // Initialize the main menu, game board, timer, and advertisements
    val menu = MainMenu

    // Initialize two background threads for the timer and ads, ensuring they run concurrently
    val threadTimer = Thread { Timer.run() }
    val threadAdManager = Thread { AdManager.run() }

    // Initialize and update the screen before entering the main loop
    Screen.initScreen()
    Screen.updateScreen()

    try {
        // Start both background threads (timer and ads)
        threadTimer.start()
        threadAdManager.start()

        // Disable input echoing in the terminal and set it to raw mode for non-buffered input
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw -echo < /dev/tty")).waitFor()

        // Variable to track whether the user is in the "game" or "menu" state
        var gameOrMenu = GameOrMenu.GAME

        // Main loop, handling user input for game or menu navigation
        while (true) {
            val key = System.`in`.read().toChar() // Read user input from the terminal

            // Handle input based on whether the user is in the game or menu
            if (gameOrMenu == GameOrMenu.GAME) {
                when (key) {
                    // Movement commands for the game cursor
                    Settings.getActionKey(Action.UP) -> Screen.moveCursor(Action.UP)
                    Settings.getActionKey(Action.DOWN) -> Screen.moveCursor(Action.DOWN)
                    Settings.getActionKey(Action.LEFT) -> Screen.moveCursor(Action.LEFT)
                    Settings.getActionKey(Action.RIGHT) -> Screen.moveCursor(Action.RIGHT)
                    // Change the state of the selected cell on the game board
                    Settings.getActionKey(Action.SELECT) -> GameEngine.changeBoardPixel()
                    // Adjust the game speed
                    Settings.getActionKey(Action.SPEED_DOWN) -> Timer.decreaseSpeed()
                    Settings.getActionKey(Action.SPEED_UP) -> Timer.increaseSpeed()
                    // Adjust the width of the game board
                    Settings.getActionKey(Action.WIDTH_INC) -> Screen.increaseBoardWidth()
                    Settings.getActionKey(Action.WIDTH_DEC) -> Screen.decreaseBoardWidth()
                    // Move the camera on the game board
                    Settings.getActionKey(Action.CAM_UP) -> Screen.moveCamera(Action.CAM_UP)
                    Settings.getActionKey(Action.CAM_DOWN) -> Screen.moveCamera(Action.CAM_DOWN)
                    Settings.getActionKey(Action.CAM_LEFT) -> Screen.moveCamera(Action.CAM_LEFT)
                    Settings.getActionKey(Action.CAM_RIGHT) -> Screen.moveCamera(Action.CAM_RIGHT)
                    // Switch to the menu state
                    Settings.getActionKey(Action.MENU) -> {
                        gameOrMenu = GameOrMenu.MENU
                        Screen.switchGameOrMenu()
                    }
                    // Exit the program
                    Settings.getActionKey(Action.QUIT) -> break
                }
            } else { // gameOrMenu == "menu"
                if (menu.currentMenu is SelectMenu) {
                    menu.query(key)
                } else {
                    when (key) {
                        // Switch back to the game state
                        Settings.getActionKey(Action.MENU) -> {
                            gameOrMenu = GameOrMenu.GAME
                            Screen.switchGameOrMenu()
                        }
                        // Exit the program
                        Settings.getActionKey(Action.QUIT) -> break
                        // Handle menu-specific input
                        else -> menu.query(key)
                    }
                }
            }
        }
    } catch (e: Exception) {
        // Catch any exceptions that occur during execution
        println("Error: ${e.message}")
    } finally {
        // Interrupt the background threads for the timer and advertisements when the program ends
        threadTimer.interrupt()
        threadAdManager.interrupt()

        // Restore the terminal's input settings to their original state
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw echo < /dev/tty")).waitFor()

        // Perform any necessary cleanup on the screen
        Screen.exitScreen()
    }
}
