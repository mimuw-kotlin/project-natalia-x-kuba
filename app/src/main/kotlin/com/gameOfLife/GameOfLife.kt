package com.gameOfLife

fun main() {
    // Initialize the main menu, game board, timer, and advertisements
    val menu = MainMenu

    // Initialize two background threads for the timer and ads, ensuring they run concurrently
    val threadTimer = Thread { Timer.run() }
    val threadAd = Thread { Ad.run() }

    // Initialize and update the screen before entering the main loop
    Screen.initScreen()
    Screen.updateScreen()

    try {
        // Start both background threads (timer and ads)
        threadTimer.start()
        threadAd.start()

        // Disable input echoing in the terminal and set it to raw mode for non-buffered input
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw -echo < /dev/tty")).waitFor()

        // Variable to track whether the user is in the "game" or "menu" state
        var gameOrMenu = "game"

        // Main loop, handling user input for game or menu navigation
        while (true) {
            val key = System.`in`.read().toChar() // Read user input from the terminal

            // Handle input based on whether the user is in the game or menu
            if (gameOrMenu == "game") {
                when (key) {
                    // Movement commands for the game cursor
                    'w' -> Screen.moveCursor("UP")
                    's' -> Screen.moveCursor("DOWN")
                    'a' -> Screen.moveCursor("LEFT")
                    'd' -> Screen.moveCursor("RIGHT")
                    // Change the state of the selected cell on the game board
                    ' ' -> GameEngine.changeBoardPixel()
                    // Adjust the game speed
                    ',' -> Timer.decreaseSpeed()
                    '.' -> Timer.increaseSpeed()
                    // Switch to the menu state
                    'e' -> {
                        gameOrMenu = "menu"
                        Screen.switchGameOrMenu()
                    }
                    // Exit the program
                    'q' -> break
                }
            } else { // gameOrMenu == "menu"
                when (key) {
                    // Switch back to the game state
                    'e' -> {
                        gameOrMenu = "game"
                        Screen.switchGameOrMenu()
                    }
                    // Exit the program
                    'q' -> break
                    // Handle menu-specific input
                    else -> menu.query(key)
                }
            }
        }
    } catch (e: Exception) {
        // Catch any exceptions that occur during execution
        println("Error: ${e.message}")
    } finally {
        // Interrupt the background threads for the timer and advertisements when the program ends
        threadTimer.interrupt()
        threadAd.interrupt()

        // Restore the terminal's input settings to their original state
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw echo < /dev/tty")).waitFor()

        // Perform any necessary cleanup on the screen
        Screen.exitScreen()
    }
}
