package com.gameOfLife

interface Clickable {
    var parent: Menu?
    var text: String

    fun query(key: Char)
}
