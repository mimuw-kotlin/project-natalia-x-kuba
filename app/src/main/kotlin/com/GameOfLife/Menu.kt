package app.src.main.kotlin.com.GameOfLife

interface Menu {
    companion object {
        private const val ROWS = 20
        private const val MENU_BOARD_COLS = 39
        private val board = Array(ROWS) { Array(MENU_BOARD_COLS) { Pixel("?") } }
        private lateinit var screen: Screen

        init {
            board[0] = Pixel.createArray("                                       ")
            board[1] = Pixel.createArray("     _____ ____        ___  _          ")
            board[2] = Pixel.createArray("    |_   _|  _ \\ __ _ / _ \\| |         ")
            board[3] = Pixel.createArray("      | | | |_) / _` | | | | |         ")
            board[4] = Pixel.createArray("      | | |  _ < (_| | |_| | |___      ")
            board[5] = Pixel.createArray("      |_| |_| \\_\\__, |\\___/|_____|     ")
            board[6] = Pixel.createArray("                 |___/                 ")
            board[7] = Pixel.createArray("                                       ")
        }
    }


    fun setScreen(screen: Screen) {
        Companion.screen = screen
    }

    fun query(key: Char)

    fun display() {
        Companion.screen.updateMenuBoard(board)
    }

    /*
  _____ ____        ___  _
 |_   _|  _ \ __ _ / _ \| |
   | | | |_) / _` | | | | |
   | | |  _ < (_| | |_| | |___
   |_| |_| \_\__, |\___/|_____|
             |___/

     */
}