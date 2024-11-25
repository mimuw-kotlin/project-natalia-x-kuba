package com.GameOfLife

import app.src.main.kotlin.com.GameOfLife.Ad
import app.src.main.kotlin.com.GameOfLife.Screen
import app.src.main.kotlin.com.GameOfLife.Timer
import java.io.Console
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread

class GameOfLife {
    val force_update = Semaphore(1, true)
    val timer_update = Semaphore(1, true)
    val timer_mutex = Semaphore(1, true)
    val ROWS = 24
    val COLUMNS = 80

    var local_time = 0
    var speed = 1
    var cursorX = 0
    var cursorY = 0
    var screen = Array(ROWS) { CharArray(COLUMNS) { '.' } }
    var stillPlaying = true;

    fun updateScreen() {
        while (stillPlaying) {
            force_update.acquire()
            // calculate new game state here TODO
            repeat(ROWS + 1) { print("\u001b[1A") }
            val time = getTime()
            val speedText = getSpeed()
            println("Game Of Life \t\t\t$time\t\t\t    Speed: $speedText")
            for (i in 0 until ROWS) {  // Change to 'until' instead of '..<' for Kotlin
                print("\r")
                for (j in 0 until COLUMNS) { // Same change here
                    if (j == cursorX && i == cursorY) print("\u001b[48;5;250m")
                    print(screen[i][j])
                    if (j == cursorX && i == cursorY) print("\u001b[0m")
                }
                print("\n\r")
            }
            System.out.flush()
        }
        exitScreen()
    }

    fun timer() {
        while (stillPlaying) {
            timer_update.acquire()
            timer_update.release()
            timer_mutex.acquire()
            if (speed == 0) {
                timer_mutex.release()
                timer_update.acquire()
                continue
            }

            Thread.sleep(1000 / speed.toLong())
            timer_mutex.release()
            local_time += 1
            force_update.release()
        }
    }

    fun _userInput() {
        while (stillPlaying) {
            val key = System.`in`.read()
            // Check for q key
            if (key.toChar() == 'q') {
                exitScreen()
                stillPlaying = false
                continue
            }

            // Check for arrow keys (27 91 ?)
            if (key == 91) { // '[' character
                when (readArrowKey()) {
                    "UP" -> if (cursorY > 0) cursorY--
                    "DOWN" -> if (cursorY < ROWS - 1) cursorY++
                    "LEFT" -> if (cursorX > 0) cursorX--
                    "RIGHT" -> if (cursorX < COLUMNS - 1) cursorX++
                }
            }

            if (key == 32) {
                screen[cursorY][cursorX] = if (screen[cursorY][cursorX] == '.') 'X' else '.'
            }

            if (key.toChar() == '.') {
                timer_mutex.acquire()
                speed = kotlin.math.min(5, speed + 1)
                if (speed == 1) {
                    timer_update.release()
                }
                timer_mutex.release()
            }

            if (key.toChar() == ',') {
                timer_mutex.acquire()
                speed = kotlin.math.max(0, speed - 1)
                timer_mutex.release()
            }
            force_update.release()
        }
    }

    // Helper functions

    fun initScreen() {
        print("\u001b[H\u001b[2J")
        System.out.flush()
    }

    fun readArrowKey(): String {
        val arrowChar = System.`in`.read().toChar()

        return when (arrowChar) {
            'A' -> "UP"       // Arrow Up
            'B' -> "DOWN"     // Arrow Down
            'D' -> "LEFT"     // Arrow Left
            'C' -> "RIGHT"    // Arrow Right
            else -> ""        // Invalid key
        }
    }

    fun exitScreen() {
        repeat(ROWS + 1) { print("\u001b[1A") }
        print("\u001b[2K")
        println("Thank you for playing!!\r")
        System.out.flush()
    }

    fun getTime(): String {
        val loc_time = local_time
        val hours = loc_time / 3600
        val minutes = (loc_time % 3600) / 60
        val seconds = loc_time % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun getSpeed(): String {
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
}

fun _main(args: Array<String>) {
    val game = GameOfLife()

    val console: Console? = System.console()
    if (console == null) {
        println("No console available.")
        return
    }
    // Set terminal to raw mode to capture each keypress without Enter
    Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw -echo < /dev/tty")).waitFor()

    game.initScreen()

    val thread_timer = Thread { game.timer() }
    val thread_screen = Thread { game.updateScreen() }
    val thread_input = Thread { game._userInput() }

    // Start both threads
    try {
        thread_timer.start()
        thread_screen.start()
        thread_input.start()

        thread_input.join()
        thread_timer.join()
        thread_screen.join()
    } catch (e: InterruptedException) {
        println("Main thread interrupted")
    }
    finally {
        // Restore terminal to normal mode
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw echo < /dev/tty")).waitFor()
    }
}

fun main(args: Array<String>) {
    val screen = Screen()
    val timer = Timer(screen)
    val ad = Ad(screen)

    val thread_timer = Thread { timer.run() }
    val thread_ad = Thread { ad.run() }

    screen.initScreen()
    screen.updateScreen()

    try {
        thread_timer.start()
        thread_ad.start()
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw -echo < /dev/tty")).waitFor()
        while (true) {
            val key = System.`in`.read().toChar()
            when (key) {
                'w' -> screen.moveCursor("UP")
                's' -> screen.moveCursor("DOWN")
                'a' -> screen.moveCursor("LEFT")
                'd' -> screen.moveCursor("RIGHT")
                ' ' -> screen.changeBoardPixel()
                ',' -> timer.decreaseSpeed()
                '.' -> timer.increaseSpeed()
                'q' -> break
            }
        }
    }
    catch (e: Exception) {
        println("Error: ${e.message}")
    }
    finally {
        thread_timer.interrupt()
        thread_ad.interrupt()
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw echo < /dev/tty")).waitFor()
        screen.exitScreen()
    }



    /*



    // Handle termination (e.g., Ctrl+C)
    val shutdownHook = Thread {
        screen.exitScreen()
    }
    // Register the shutdown hook
    Runtime.getRuntime().addShutdownHook(shutdownHook)

    val userInput = Thread { UserInput(screen).run() }

    try {
        screen.initScreen()
        screen.updateScreen()
        userInput.start()
        Thread.sleep(5000)
    } finally {
        userInput.interrupt()
        screen.exitScreen()

    }
    */
}
