package com.gameOfLife.menu

import com.gameOfLife.Action
import com.gameOfLife.Screen
import com.gameOfLife.Settings
import com.gameOfLife.menu.MainMenu.currentMenu

class SelectMenu(parent: Menu?, private var keyToChange: Action) : Menu(parent) {
    override var text: String = "Select New UNIQUE keybind"
    override var children: Array<Clickable> = arrayOf()

    init {
        boardText[0] = text

        boardText[11] = "Press a key to change the keybind"
        boardText[12] = "Strongly advised to use only A-Z, 0-9"
        boardText[13] = ""
    }

    override fun query(key: Char) {
        var localText = "FAILURE"
        if (Settings.setKey(keyToChange, key)) {
            localText = "SUCCESS"
        }
        boardText[13] = localText
        updateBoard()
        Screen.updateMenuBoard(board)
        Thread.sleep(500)
        boardText[13] = " "
        updateBoard()
        currentMenu = this.parent!!
        Screen.setMenu(currentMenu)
    }
}
