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
object PremiumMenu : Menu() {
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
                // Text for the "Back" option to return to the Main Menu
                override var text: String = "Back"

                /**
                 * The parent menu of this clickable item. It is dynamically resolved to refer to `PremiumMenu`.
                 * This allows us to navigate back to the `PremiumMenu`'s parent, which could be another menu in the future.
                 */
                override var parent: Menu?
                    get() = PremiumMenu.parent // Dynamically resolve the parent
                    set(value) {
                        PremiumMenu.parent = value // Update PremiumMenu's parent dynamically
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
     * The parent menu for the `PremiumMenu`. This is set dynamically to the `MainMenu` when navigating between menus.
     */
    override var parent: Menu? = null

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
     * Currently, it only handles the ' ' (space) key to select an option.
     *
     * @param key The input key pressed by the user. The only recognized key here is the spacebar (' ').
     */
    override fun query(key: Char) {
        when (key) {
            Settings.getActionKey(Action.SELECT) -> {
                if (children[cursor] is Menu) {
                    // TODO
                } else {
                    children[cursor].query(key)
                }
            }
        }
    }
}
