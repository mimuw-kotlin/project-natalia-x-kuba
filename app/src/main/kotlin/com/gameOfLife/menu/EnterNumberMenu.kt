package com.gameOfLife.menu

import com.gameOfLife.Screen
import com.gameOfLife.menu.MainMenu.currentMenu

class EnterNumberMenu(
    parent: Menu?,
    override var text: String,
    private var keyStrokes: Int,
    private var low: Int,
    private var high: Int,
    var done: (Int) -> Boolean,
) : Menu(
        parent,
    ) {
    override var children: Array<Clickable> = arrayOf()
    private var localText = ""

    init {
        boardText[0] = text

        boardText[11] = "Input a number of length $keyStrokes"
        boardText[12] = "Input a number between $low and $high"
        boardText[13] = "Your input: "
    }

    override fun query(key: Char) {
        localText += key
        boardText[13] = "Your input: $localText"
        updateBoard()
        Screen.updateMenuBoard(board)

        if (localText.length == keyStrokes) {
            var ok = true
            try {
                val number = localText.toInt()
                if (number in low..high) {
                    if (!done(number)) {
                        boardText[13] = "Wrong..."
                        ok = false
                    } else {
                        boardText[13] = "SUCCESS"
                    }
                } else {
                    boardText[13] = "FAILURE: Number not in range"
                    ok = false
                }
            } catch (e: NumberFormatException) {
                boardText[13] = "FAILURE: Invalid number"
                ok = false
            }

            updateBoard()
            Screen.updateMenuBoard(board)
            if (ok) {
                Thread.sleep(500L)
            } else {
                Thread.sleep(3000L)
            }
            boardText[13] = " "
            localText = ""

            currentMenu = this.parent!!
            Screen.setMenu(currentMenu)
        }
    }
}
