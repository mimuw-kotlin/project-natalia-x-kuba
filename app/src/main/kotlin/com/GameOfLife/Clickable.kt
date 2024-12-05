package com.GameOfLife

import app.src.main.kotlin.com.GameOfLife.MainMenu
import app.src.main.kotlin.com.GameOfLife.Menu

interface Clickable {
    var parent: Menu?
    var text: String

    fun query(key: Char)
}