package com.GameOfLife

import app.src.main.kotlin.com.GameOfLife.Menu
import app.src.main.kotlin.com.GameOfLife.Screen

object PremiumMenu : Menu() {
    override var text: String = "$ Get Premium $"
    override var children: Array<Pair<Clickable, Int>> = arrayOf()

    init {
        board[0] = centerText(text)

        for (i in 10..Settings.ROWS - 1) {
            board[i] = centerText("")
        }
    }

    override fun query(key: Char) {
        // println("Key pressed: $key")
    }
}