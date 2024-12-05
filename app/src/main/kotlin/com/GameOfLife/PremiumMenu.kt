package com.GameOfLife

import app.src.main.kotlin.com.GameOfLife.MainMenu
import app.src.main.kotlin.com.GameOfLife.Menu
import app.src.main.kotlin.com.GameOfLife.Screen

object PremiumMenu : Menu() {
    override var text: String = "$ Get Premium $"
    override var children: Array<Clickable> = arrayOf(object : Clickable {
        override var text: String = "Back"
        override var parent: Menu?
            get() = PremiumMenu.parent // Dynamically resolve the parent
            set(value) {
                PremiumMenu.parent = value // Update PremiumMenu's parent dynamically
            }
        override fun query(key: Char) {
            MainMenu.currentMenu = this.parent!!
            screen.setMenu(MainMenu.currentMenu)
        }
    })
    override var parent : Menu? = null

    init {
        board[0] = centerText(this.text)

        for (i in 10..10+ this.children.size-1) {
            board[i] = centerText(this.children[i-10].text)
        }

        for (i in 10+ this.children.size..<Settings.ROWS) {
            board[i] = centerText("")
        }
    }

    override fun query(key: Char) {
        when (key) {
            ' ' -> {
                if (this.children[cursor] is Menu) {
                    // TODO
                } else {
                    this.children[cursor].query(' ')
                }
            }
        }
    }
}