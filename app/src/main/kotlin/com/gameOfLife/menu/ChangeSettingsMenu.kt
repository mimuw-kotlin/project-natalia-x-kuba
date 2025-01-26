package com.gameOfLife.menu

import com.gameOfLife.Action
import com.gameOfLife.CellRange
import com.gameOfLife.Screen
import com.gameOfLife.Settings
import com.gameOfLife.menu.MainMenu.currentMenu

/**
 * The `ChangeKeyMenu` class represents a menu where users can change the key bindings for the game actions.
 */
class ChangeSettingsMenu(parent: Menu?) : Menu(parent) {
    /**
     * The text displayed at the top of the change-key-menu.
     */
    override var text: String = "Change Settings"

    /**
     * The list of clickable options in the menu. Currently, it contains a single option to go back to the main menu.
     * Each `Clickable` represents a user-selectable menu item.
     */
    override var children: Array<Clickable> =
        arrayOf(
            PremiumMenu(this),
            object : Clickable {
                var currentKey = Action.UP
                override var text = "◀ Select Key: $currentKey ▶"

                override var parent: Menu? = this@ChangeSettingsMenu

                override suspend fun query(key: Char) {
                    when (key) {
                        Settings.getActionKey(Action.LEFT) -> {
                            currentKey = Action.getPrev(currentKey)
                            text = "◀ Select Key: $currentKey ▶"
                            boardText[cursor + 10] = text
                            updateBoard()
                            Screen.updateMenuBoard(board)
                        }
                        Settings.getActionKey(Action.RIGHT) -> {
                            currentKey = Action.getNext(currentKey)
                            text = "◀ Select Key: $currentKey ▶"
                            boardText[cursor + 10] = text
                            updateBoard()
                            Screen.updateMenuBoard(board)
                        }
                        Settings.getActionKey(Action.SELECT) -> {
                            currentMenu = SelectMenu(this.parent, currentKey)
                            Screen.setMenu(currentMenu)
                        }
                    }
                }
            },
            object : Clickable {
                var currentKey = CellRange.ALIVE_LOW
                override var text = "◀ Select Key: $currentKey ▶"

                override var parent: Menu? = this@ChangeSettingsMenu

                fun done(number: Int): Boolean {
                    Settings.setCellRange(currentKey, number)
                    return true
                }

                override suspend fun query(key: Char) {
                    when (key) {
                        Settings.getActionKey(Action.LEFT) -> {
                            currentKey = CellRange.getPrev(currentKey)
                            text = "◀ Select Key: $currentKey ▶"
                            boardText[cursor + 10] = text
                            updateBoard()
                            Screen.updateMenuBoard(board)
                        }
                        Settings.getActionKey(Action.RIGHT) -> {
                            currentKey = CellRange.getNext(currentKey)
                            text = "◀ Select Key: $currentKey ▶"
                            boardText[cursor + 10] = text
                            updateBoard()
                            Screen.updateMenuBoard(board)
                        }
                        Settings.getActionKey(Action.SELECT) -> {
                            currentMenu = EnterNumberMenu(this.parent, "Choose a number!", 1, 1, 8, ::done)
                            Screen.setMenu(currentMenu)
                        }
                    }
                }
            },
            Back(this.parent),
        )

    /**
     * Initializes the premium menu by setting up the board and populating it with the menu text and clickable options.
     * The first row contains the title text, and subsequent rows contain the clickable options, such as "Back".
     * Empty rows are filled with blank spaces after the menu items.
     */
    init {
        boardText[0] = text

        for (i in 10..10 + children.size - 1) {
            boardText[i] = children[i - 10].text
        }

        for (i in 10 + children.size..<Settings.ROWS) {
            boardText[i] = ""
        }
    }

    /**
     * Handles user input when navigating through the premium menu. Depending on the key pressed, it can perform actions
     * like selecting an option or navigating between menu items.
     *
     * @param key The input key pressed by the user.
     */
    override suspend fun query(key: Char) {
        when (key) {
            // Move the cursor up through the options
            Settings.getActionKey(Action.UP) -> {
                unmarkHovered()
                cursor = (cursor - 1 + children.size) % children.size
                updateBoard()
                Screen.updateMenuBoard(board)
            }
            // Move the cursor down through the options
            Settings.getActionKey(Action.DOWN) -> {
                unmarkHovered()
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
            else -> {
                children[cursor].query(key)
            }
        }
    }
}
