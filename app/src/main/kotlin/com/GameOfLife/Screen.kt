package app.src.main.kotlin.com.GameOfLife

class Screen {
    private val ROWS = 20
    private val BOARD_COLS = 20
    private val AD_COLS = 20
    private var time = 0
    private var speed = 1
    private var cursor = Pair(0, 0)
    private var board = Array(ROWS) { Array(BOARD_COLS) { Pixel(".") } }
    private var ad = Array(ROWS) { Array(AD_COLS) { Pixel("$") } }

    public fun initScreen() {
        print("\u001b[H\u001b[2J")
        print("\u001B[?25l")
        board[cursor.first][cursor.second].setBgColor("grey")
        System.out.flush()
    }

    public fun moveCursor(direction: String) {
        board[cursor.first][cursor.second].setBgColor("")
        when (direction) {
            "UP" -> cursor = Pair((cursor.first - 1 + ROWS) % ROWS, cursor.second)
            "DOWN" -> cursor = Pair((cursor.first + 1) % ROWS, cursor.second)
            "LEFT" -> cursor = Pair(cursor.first, (cursor.second - 1 + BOARD_COLS) % BOARD_COLS)
            "RIGHT" -> cursor = Pair(cursor.first, (cursor.second + 1) % BOARD_COLS)
        }
        board[cursor.first][cursor.second].setBgColor("grey")
        this.updateScreen()
    }

    public fun changeBoardPixel() {
        if (board[cursor.first][cursor.second].getCharacter() == ".") {
            board[cursor.first][cursor.second].setCharacter("#")
        } else {
            board[cursor.first][cursor.second].setCharacter(".")
        }
        this.updateScreen()
    }

    public fun exitScreen() {
        repeat(ROWS + 3) { print("\r\u001b[1A") }
        print("\u001b[2K")
        println("\rThank you for playing!!")
        System.out.flush()
        print("\u001B[?25h")
    }

    @Synchronized
    public fun updateScreen() {
        repeat(ROWS + 3) { print("\r\u001b[1A") }
        val timeText = getTime()
        val speedText = getSpeed()

        // print top row
        print("TRgOLL NxK  \t\t$timeText\t\t  Speed: $speedText\n\r")

        repeat(2 * BOARD_COLS + AD_COLS + 2) { print("-") }
        print("\n\r")

        for (i in 0 until ROWS) {
            print("\r|")

            for (j in 0 until AD_COLS) {
                print(ad[i][j].getValue())
            }

            print("|")

            for (j in 0 until BOARD_COLS) { // Same change here
                print(board[i][j].getValue())
                print(" ")
            }

            print("\u001B[D|\n\r")
        }

        repeat(2 * BOARD_COLS + AD_COLS + 2) { print("-") }
        println()

        System.out.flush()
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

    public fun updateTime(time: Int) {
        this.time = time
        this.updateScreen()
    }

    public fun updateSpeed(speed: Int) {
        this.speed = speed
        this.updateScreen()
    }

    public fun changeAd(ad: Array<Array<Pixel>>) {
        this.ad = ad
        this.updateScreen()
    }
}