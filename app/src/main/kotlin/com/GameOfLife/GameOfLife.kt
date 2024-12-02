package com.GameOfLife

import app.src.main.kotlin.com.GameOfLife.*
import java.io.Console
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread


fun main(args: Array<String>) {
    val screen = Screen()
    val menu = MainMenu(screen, "Main Menu"); screen.setMenu(menu)
    val timer = Timer(screen)
    val ad = Ad(screen)
    val gameBoard = Board(screen)

    val thread_timer = Thread { timer.run() }
    val thread_ad = Thread { ad.run() }

    screen.initScreen()
    screen.updateScreen()

    try {
        thread_timer.start()
        thread_ad.start()
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
                        gameOrMenu = "menu"; screen.switchGameOrMenu()
                    }

                    'q' -> break
                }
            } else if (gameOrMenu == "menu") {
                when (key) {
                    'e' -> {
                        gameOrMenu = "game"; screen.switchGameOrMenu()
                    }
                    'q' -> break
                    else -> menu.query(key)
                }
            }
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    } finally {
        thread_timer.interrupt()
        thread_ad.interrupt()
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw echo < /dev/tty")).waitFor()
        screen.exitScreen()
    }
}

