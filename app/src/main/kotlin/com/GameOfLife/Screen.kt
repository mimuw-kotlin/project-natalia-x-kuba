package app.src.main.kotlin.com.GameOfLife

import java.util.concurrent.Semaphore

class Screen {
    private val ROWS = 20
    private val GAME_BOARD_COLS = 20
    private val MENU_BOARD_COLS = 39
    private val AD_COLS = 20
    private var time = 0
    private var speed = 1
    private var cursor = Pair(0, 0)
    private var game_board = Array(ROWS) { Array(GAME_BOARD_COLS) { Pixel(".") } }
    private var menu_board = Array(ROWS) { Array(MENU_BOARD_COLS) { Pixel("?") } }
    private var game_or_menu  = "game"
    private var ad = Array(ROWS) { Array(AD_COLS) { Pixel("$") } }

    private val mutex = Semaphore(1, true)
    private lateinit var menu: Menu

    public fun setMenu(menu: Menu) {
        this.menu = menu
    }

    public fun initScreen() {
        mutex.acquire()

        print("\u001b[H\u001b[2J")
        print("\u001B[?25l")
        game_board[cursor.first][cursor.second].setBgColor("grey")
        System.out.flush()

        mutex.release()
    }


    public fun moveCursor(direction: String) {
        mutex.acquire()

        game_board[cursor.first][cursor.second].setBgColor("")
        when (direction) {
            "UP" -> cursor = Pair((cursor.first - 1 + ROWS) % ROWS, cursor.second)
            "DOWN" -> cursor = Pair((cursor.first + 1) % ROWS, cursor.second)
            "LEFT" -> cursor = Pair(cursor.first, (cursor.second - 1 + GAME_BOARD_COLS) % GAME_BOARD_COLS)
            "RIGHT" -> cursor = Pair(cursor.first, (cursor.second + 1) % GAME_BOARD_COLS)
        }
        game_board[cursor.first][cursor.second].setBgColor("grey")

        mutex.release()
        this.updateScreen()
    }


    public fun exitScreen() {
        mutex.acquire()

        repeat(ROWS + 3) { print("\r\u001b[1A") }
        print("\u001b[2K")
        println("\rThank you for playing!!")
        System.out.flush()
        print("\u001B[?25h")

        mutex.release()
    }


    public fun updateScreen() {
        mutex.acquire()

        repeat(ROWS + 3) { print("\r\u001b[1A") }
        val timeText = getTime()
        val speedText = getSpeed()

        // print top row
        print("TRgOLL NxK  \t\t$timeText\t\t  Speed: $speedText\n\r")

        repeat(2 * GAME_BOARD_COLS + AD_COLS + 2) { print("-") }
        print("\n\r")

        for (i in 0 until ROWS) {
            print("\r|")

            for (j in 0 until AD_COLS) {
                print(ad[i][j].getValue())
            }

            print("|")
            if (game_or_menu == "game") {
                for (j in 0 until GAME_BOARD_COLS) {
                    print(game_board[i][j].getValue())
                    print(" ")
                }
                print("\u001B[D|\n\r")
            } else if (game_or_menu == "menu") {
                for (j in 0 until MENU_BOARD_COLS) {
                    print(menu_board[i][j].getValue())
                }
                print("|\n\r")
            }
        }

        repeat(2 * GAME_BOARD_COLS + AD_COLS + 2) { print("-") }
        println()

        System.out.flush()
        mutex.release()
    }


    private fun getTime(): String {
        val loc_time = time
        val hours = loc_time / 3600
        val minutes = (loc_time % 3600) / 60
        val seconds = loc_time % 60
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


    public fun updateGameBoardPixel(x: Int, y: Int, value: String) {
        mutex.acquire()

        game_board[x][y].setCharacter(value)

        mutex.release()
        this.updateScreen()
    }

    public fun updateGameBoard(board: Array<Array<String>>) {
        mutex.acquire()

        for (i in 0 until ROWS) {
            for (j in 0 until GAME_BOARD_COLS) {
                game_board[i][j].setCharacter(board[i][j])
            }
        }

        mutex.release()
        this.updateScreen()
    }

    public fun updateMenuBoard(board: Array<Array<Pixel>>) {
        mutex.acquire()

        for (i in 0 until ROWS) {
            for (j in 0 until MENU_BOARD_COLS) {
                menu_board[i][j] = board[i][j]
            }
        }

        mutex.release()
        this.updateScreen()
    }

    public fun switchGameOrMenu() {
        mutex.acquire()

        if (game_or_menu == "game") {
            game_or_menu = "menu"
        } else {
            game_or_menu = "game"
        }

        mutex.release()

        if (game_or_menu == "menu") {
            menu.display()
        }
    }
}