package com.gameOfLife

import app.src.main.kotlin.com.GameOfLife.*

fun main() {
    val screen = Screen()
    MainMenu.screen = screen
    PremiumMenu.screen = screen

    val menu = MainMenu
    val gameBoard = Board(screen)
    val timer = Timer(screen, gameBoard)
    val ad = Ad(screen)

    // Initialize the threads running in the background
    val threadTimer = Thread { timer.run() }
    val threadAd = Thread { ad.run() }

    screen.initScreen()
    screen.updateScreen()

    try {
        threadTimer.start()
        threadAd.start()
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw -echo < /dev/tty")).waitFor()

        var gameOrMenu = "game"
        while (true) {
            val key = System.`in`.read().toChar()
            if (gameOrMenu == "game") {
                when (key) {
                    'w' -> screen.moveCursor("UP")
                    's' -> screen.moveCursor("DOWN")
                    'a' -> screen.moveCursor("LEFT")
                    'd' -> screen.moveCursor("RIGHT")
                    ' ' -> gameBoard.changeBoardPixel()
                    ',' -> timer.decreaseSpeed()
                    '.' -> timer.increaseSpeed()
                    'e' -> {
                        gameOrMenu = "menu"
                        screen.switchGameOrMenu()
                    }

                    'q' -> break
                }
            } else { // gameOrMenu == "menu"
                when (key) {
                    'e' -> {
                        gameOrMenu = "game"
                        screen.switchGameOrMenu()
                    }
                    'q' -> break
                    else -> menu.query(key)
                }
            }
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    } finally {
        threadTimer.interrupt()
        threadAd.interrupt()
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw echo < /dev/tty")).waitFor()
        screen.exitScreen()
    }
}
