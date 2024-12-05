package com.GameOfLife

interface Clickable {
    var text: String

    fun query(key: Char)
}