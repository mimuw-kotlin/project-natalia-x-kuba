package com.gameOfLife.menu

import com.gameOfLife.Action
import com.gameOfLife.Screen
import com.gameOfLife.Settings
import com.gameOfLife.menu.MainMenu.currentMenu

/**
 * The `ChangeKeyMenu` class represents a menu where users can change the key bindings for the game actions.
 */
object ChangeKeyMenu : Menu() {
    /**
     * The text displayed at the top of the change-key-menu.
     */
    override var text: String = "Change Key Bindings"

    /**
     * The list of clickable options in the menu. Currently, it contains a single option to go back to the main menu.
     * Each `Clickable` represents a user-selectable menu item.
     */
    override var children: Array<Clickable> =
        arrayOf(
            PremiumMenu,
            object : Clickable {
                var currentKey = Action.UP
                override var text = "◀ Select Key: $currentKey ▶"

                override var parent: Menu?
                    get() = ChangeKeyMenu.parent // Dynamically resolve the parent
                    set(value) {
                        ChangeKeyMenu.parent = value // Update PremiumMenu's parent dynamically
                    }

                override fun query(key: Char) {
                    when (key) {
                        Settings.getActionKey(Action.LEFT) -> {
                            currentKey = Action.getPrev(currentKey)
                            text = "◀ Select Key: $currentKey ▶"
                            board[cursor + 10] = centerText(text)
                            markHovered()
                            Screen.updateMenuBoard(board)
                        }
                        Settings.getActionKey(Action.RIGHT) -> {
                            currentKey = Action.getNext(currentKey)
                            text = "◀ Select Key: $currentKey ▶"
                            board[cursor + 10] = centerText(text)
                            markHovered()
                            Screen.updateMenuBoard(board)
                        }
                        Settings.getActionKey(Action.SELECT) -> {
                            currentMenu = SelectMenu
                            SelectMenu.parent = ChangeKeyMenu
                            SelectMenu.keyToChange = currentKey
                            Screen.setMenu(currentMenu)
                        }
                    }
                }
            },
            object : Clickable {
                // Text for the "Back" option to return to the Main Menu
                override var text: String = "Back"

                /**
                 * The parent menu of this clickable item. It is dynamically resolved to refer to `PremiumMenu`.
                 * This allows us to navigate back to the `PremiumMenu`'s parent, which could be another menu in the future.
                 */
                override var parent: Menu?
                    get() = ChangeKeyMenu.parent // Dynamically resolve the parent
                    set(value) {
                        ChangeKeyMenu.parent = value // Update PremiumMenu's parent dynamically
                    }

                /**
                 * Handles user input to navigate back to the parent menu. In this case, it switches back to the `MainMenu`.
                 * This method is invoked when the user selects the "Back" option.
                 *
                 * @param key The input key, which is ignored in this case since the action is predefined to return to the parent menu.
                 */
                override fun query(key: Char) {
                    when (key) {
                        Settings.getActionKey(Action.SELECT) -> {
                            currentMenu = this.parent!!
                            Screen.setMenu(currentMenu)
                        }
                        else -> {}
                    }
                }
            },
        )

    /**
     * The parent menu for the `ChangeKeyMenu`. This is set dynamically to the `MainMenu` when navigating between menus.
     */
    override var parent: Menu? = null

    /**
     * Initializes the premium menu by setting up the board and populating it with the menu text and clickable options.
     * The first row contains the title text, and subsequent rows contain the clickable options, such as "Back".
     * Empty rows are filled with blank spaces after the menu items.
     */
    init {
        board[0] = centerText(text)

        for (i in 10..10 + children.size - 1) {
            board[i] = centerText(children[i - 10].text)
        }

        for (i in 10 + children.size..<Settings.ROWS) {
            board[i] = centerText("")
        }
    }

    /**
     * Handles user input when navigating through the premium menu. Depending on the key pressed, it can perform actions
     * like selecting an option or navigating between menu items.
     *
     * @param key The input key pressed by the user.
     */
    override fun query(key: Char) {
        when (key) {
            // Move the cursor up through the options
            Settings.getActionKey(Action.UP) -> {
                unmarkHovered()
                cursor = (cursor - 1 + children.size) % children.size
                markHovered()
                Screen.updateMenuBoard(board)
            }
            // Move the cursor down through the options
            Settings.getActionKey(Action.DOWN) -> {
                unmarkHovered()
                cursor = (cursor + 1) % children.size
                markHovered()
                Screen.updateMenuBoard(board)
            }
            // Select the current hovered option
            Settings.getActionKey(Action.SELECT) -> {
                // If the selected option is a sub-menu, switch to that menu
                if (children[cursor] is Menu) {
                    currentMenu = children[cursor] as Menu
                    children[cursor].parent = this
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
