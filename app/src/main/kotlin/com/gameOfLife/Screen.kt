package com.gameOfLife

import java.util.concurrent.Semaphore

class Screen {
    private var time = 0
    private var speed = 1
    private var cursor = Pair(0, 0)
    private var gameBoard = Array(Settings.ROWS) { Array(Settings.GAME_BOARD_COLS) { Pixel(".") } }
    private var menuBoard = Array(Settings.ROWS) { Array(Settings.MENU_BOARD_COLS) { Pixel("?") } }
    private var gameOrMenu = "game"
    private var ad = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }

    private val mutex = Semaphore(1, true)
    private var menu: Menu = MainMenu

    public fun initScreen() {
        mutex.acquire()

        print("\u001b[H\u001b[2J")
        print("\u001B[?25l")
        gameBoard[cursor.first][cursor.second].setBgColor("grey")
        System.out.flush()

        mutex.release()
    }

    public fun moveCursor(direction: String) {
        mutex.acquire()

        gameBoard[cursor.first][cursor.second].setBgColor("")
        when (direction) {
            "UP" -> cursor = Pair((cursor.first - 1 + Settings.ROWS) % Settings.ROWS, cursor.second)
            "DOWN" -> cursor = Pair((cursor.first + 1) % Settings.ROWS, cursor.second)
            "LEFT" -> cursor = Pair(cursor.first, (cursor.second - 1 + Settings.GAME_BOARD_COLS) % Settings.GAME_BOARD_COLS)
            "RIGHT" -> cursor = Pair(cursor.first, (cursor.second + 1) % Settings.GAME_BOARD_COLS)
        }
        gameBoard[cursor.first][cursor.second].setBgColor("grey")

        mutex.release()
        this.updateScreen()
    }

    public fun exitScreen() {
        mutex.acquire()
        print("\u001B[2J")
        print("\u001B[H")
        repeat(Settings.ROWS + 3) { print("\r\u001b[1A") }
        print("\u001b[2K")
        println("\rThank you for playing!!")
        System.out.flush()
        print("\u001B[?25h")
        mutex.release()
    }

    public fun updateScreen() {
        mutex.acquire()

        repeat(Settings.ROWS + 3) { print("\r\u001b[1A") }
        val timeText = getTime()
        val speedText = getSpeed()

        // print top row
        print("TRgOLL NxK  \t\t$timeText\t\t  Speed: $speedText\n\r")

        repeat(2 * Settings.GAME_BOARD_COLS + Settings.AD_COLS + 2) { print("-") }
        print("\n\r")

        for (i in 0 until Settings.ROWS) {
            print("\r|")

            for (j in 0 until Settings.AD_COLS) {
                print(ad[i][j].getValue())
            }

            print("|")
            if (gameOrMenu == "game") {
                for (j in 0 until Settings.GAME_BOARD_COLS) {
                    print(gameBoard[i][j].getValue())
                    print(" ")
                }
                print("\u001B[D|\n\r")
            } else if (gameOrMenu == "menu") {
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

    private fun getTime(): String {
        val locTime = time
        val hours = locTime / 3600
        val minutes = (locTime % 3600) / 60
        val seconds = locTime % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

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

    public fun getCursor(): Pair<Int, Int> {
        return cursor
    }

    public fun updateTime(time: Int) {
        mutex.acquire()

        this.time = time

        mutex.release()
        this.updateScreen()
    }

    public fun updateSpeed(speed: Int) {
        mutex.acquire()

        this.speed = speed

        mutex.release()
        this.updateScreen()
    }

    public fun changeAd(ad: Array<Array<Pixel>>) {
        mutex.acquire()

        this.ad = ad

        mutex.release()
        this.updateScreen()
    }

    public fun updateGameBoardPixel(
        x: Int,
        y: Int,
        value: String,
    ) {
        mutex.acquire()

        gameBoard[x][y].setCharacter(value)

        mutex.release()
        this.updateScreen()
    }

    public fun updateGameBoard(board: Array<Array<String>>) {
        mutex.acquire()

        for (i in 0 until Settings.ROWS) {
            for (j in 0 until Settings.GAME_BOARD_COLS) {
                gameBoard[i][j].setCharacter(board[i][j])
            }
        }

        mutex.release()
        this.updateScreen()
    }

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

    public fun switchGameOrMenu() {
        mutex.acquire()

        if (gameOrMenu == "game") {
            gameOrMenu = "menu"
        } else {
            gameOrMenu = "game"
        }

        mutex.release()

        if (gameOrMenu == "menu") {
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
