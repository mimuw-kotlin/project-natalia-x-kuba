package com.gameOfLife.menu

import com.gameOfLife.Action
import com.gameOfLife.Screen
import com.gameOfLife.Settings
import com.gameOfLife.menu.MainMenu.currentMenu

object SelectMenu : Menu() {
    override var text: String = "Select New UNIQUE keybind"
    override var parent: Menu? = null
    override var children: Array<Clickable> = arrayOf()
    public var keyToChange: Action = Action.UP

    init {
        board[0] = centerText(text)

        board[11] = centerText("Press a key to change the keybind")
        board[12] = centerText("Strongly advised to use only A-Z, 0-9")
        board[13] = centerText("")
    }

    override fun query(key: Char) {
        var localText = "FAILURE"
        if (Settings.setKey(keyToChange, key)) {
            localText = "SUCCESS"
        }
        board[13] = centerText(localText)
        Screen.updateMenuBoard(board)
        Thread.sleep(500)
        board[13] = centerText(" ")
        currentMenu = this.parent!!
        Screen.setMenu(currentMenu)
    }
}
