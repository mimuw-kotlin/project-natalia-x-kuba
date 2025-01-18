package com.gameOfLife.menu

import com.gameOfLife.Action
import com.gameOfLife.Screen
import com.gameOfLife.Settings
import com.gameOfLife.menu.MainMenu.currentMenu

/**
 * The `PremiumMenu` class represents a menu where users can access premium features or go back to the main menu.
 * It extends from the abstract `Menu` class and provides a specific implementation for the premium menu UI and interactions.
 * The menu includes an option to go back to the `MainMenu`.
 */
class PremiumMenu(parent: Menu?) : Menu(parent) {
    /**
     * The text displayed at the top of the premium menu. It is typically a prompt for users to explore premium features.
     */
    override var text: String = "$ Get Premium $"

    /**
     * The list of clickable options in the menu. Currently, it contains a single option to go back to the main menu.
     * Each `Clickable` represents a user-selectable menu item.
     */
    override var children: Array<Clickable> =
        arrayOf(
            object : Clickable {
                override var text = "Enter *premium* code!"

                override var parent: Menu? = this@PremiumMenu

                fun done(number: Int): Boolean {
                    if (number == 6969) {
                        Settings.unlockPremium()
                        return true
                    }
                    return false
                }

                override fun query(key: Char) {
                    when (key) {
                        Settings.getActionKey(Action.SELECT) -> {
                            currentMenu = EnterNumberMenu(this.parent, "Choose a number!", 4, 0, 9999, ::done)
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

        boardText[14] = "Currently we accept only cash"
        boardText[15] = "Please donate $69.69"
        boardText[16] = "to unlock ALL premium features"
        boardText[17] = "You will receive a premium code"
        boardText[18] = "that You can enter above"

        for (i in 10..10 + children.size - 1) {
            boardText[i] = children[i - 10].text
        }
    }

    /**
     * Handles user input when navigating through the premium menu. Depending on the key pressed, it can perform actions
     * like selecting an option or navigating between menu items.
     * Currently, it only handles the ' ' (space) key to select an option.
     *
     * @param key The input key pressed by the user. The only recognized key here is the spacebar (' ').
     */
    override fun query(key: Char) {
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
