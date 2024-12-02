package app.src.main.kotlin.com.GameOfLife

import com.GameOfLife.Settings

class MainMenu(screen: Screen, name: String) : Menu(screen, name) {


    init {
        board[0] = centerText("Main Menu")

        for (i in 10..Settings.ROWS - 1) {
            board[i] = centerText("")
        }
    }

    override fun query(key: Char) {
        // println("Key pressed: $key")
    }
}