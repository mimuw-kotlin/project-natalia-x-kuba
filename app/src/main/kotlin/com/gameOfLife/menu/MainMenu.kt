package com.gameOfLife.menu

import com.gameOfLife.Action
import com.gameOfLife.Screen
import com.gameOfLife.Settings

object MainMenu : Menu(null) {
    // The text that represents the title of the menu
    override var text: String = "Main Menu"

    // The parent of the current menu, which is initially null
    override var parent: Menu? = null

    // List of menu options, which in this case are multiple instances of PremiumMenu
    override var children: Array<Clickable> = arrayOf(PremiumMenu(this), PremiumMenu(this), PremiumMenu(this), ChangeSettingsMenu(this))

    // Keeps track of the currently active menu
    var currentMenu: Menu = this

    init {
        // Set the title of the current menu at the top of the board (row 0)
        boardText[0] = text

        // Set the menu options below the title, starting from row 10
        for (i in 10..10 + children.size - 1) {
            boardText[i] = children[i - 10].text
        }

        // Clear the remaining rows below the options
        for (i in 10 + children.size..<Settings.ROWS) {
            boardText[i] = ""
        }
    }

    // This method handles the user's input (key presses)
    override fun query(key: Char) {
        // If the current menu is not the MainMenu, pass the query to the child menu
        if (currentMenu != this) {
            currentMenu.query(key)
            return
        }

        // Handle the key press based on the current menu state
        when (key) {
            // Move the cursor up through the options
            Settings.getActionKey(Action.UP) -> {
                cursor = (cursor - 1 + children.size) % children.size
                updateBoard()
                Screen.updateMenuBoard(board)
            }
            // Move the cursor down through the options
            Settings.getActionKey(Action.DOWN) -> {
                cursor = (cursor + 1) % children.size
                updateBoard()
                Screen.updateMenuBoard(board)
            }
            // Select the current hovered option
            Settings.getActionKey(Action.SELECT) -> {
                // If the selected option is a sub-menu, switch to that menu
                if (children[cursor] is Menu) {
                    currentMenu = children[cursor] as Menu
                    Screen.setMenu(currentMenu)
                } else {
                    children[cursor].query(key)
                }
            }
        }
    }

    // Reset the current menu to its initial state
    override fun reset() {
        currentMenu = this
        unmarkHovered()
        cursor = 0

        // If there are any menu options, mark the first one as hovered
        if (!children.isNullOrEmpty() && children.isNotEmpty()) {
            markHovered()
        }
    }
}
