package com.gameOfLife

fun main() {
    // Create a Screen object to manage the display
    val screen = Screen()

    // Set the screen for both MainMenu and PremiumMenu to allow them to interact with the same screen
    MainMenu.screen = screen
    PremiumMenu.screen = screen

    // Initialize the main menu, game board, timer, and advertisements
    val menu = MainMenu
    val gameBoard = Board(screen)
    val timer = Timer(screen, gameBoard)
    val ad = Ad(screen)

    // Initialize two background threads for the timer and ads, ensuring they run concurrently
    val threadTimer = Thread { timer.run() }
    val threadAd = Thread { ad.run() }

    // Initialize and update the screen before entering the main loop
    screen.initScreen()
    screen.updateScreen()

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
                    'w' -> screen.moveCursor("UP")
                    's' -> screen.moveCursor("DOWN")
                    'a' -> screen.moveCursor("LEFT")
                    'd' -> screen.moveCursor("RIGHT")
                    // Change the state of the selected cell on the game board
                    ' ' -> gameBoard.changeBoardPixel()
                    // Adjust the game speed
                    ',' -> timer.decreaseSpeed()
                    '.' -> timer.increaseSpeed()
                    // Switch to the menu state
                    'e' -> {
                        gameOrMenu = "menu"
                        screen.switchGameOrMenu()
                    }
                    // Exit the program
                    'q' -> break
                }
            } else { // gameOrMenu == "menu"
                when (key) {
                    // Switch back to the game state
                    'e' -> {
                        gameOrMenu = "game"
                        screen.switchGameOrMenu()
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
        screen.exitScreen()
    }
}
