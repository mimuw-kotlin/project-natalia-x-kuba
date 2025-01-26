package com.gameOfLife.menu

import com.gameOfLife.Action
import com.gameOfLife.Screen
import com.gameOfLife.Settings
import com.gameOfLife.menu.MainMenu.currentMenu

class Back(override var parent: Menu?) : Clickable {
    // Text for the "Back" option to return to the Main Menu
    override var text: String = "Back"

    /**
     * Handles user input to navigate back to the parent menu. In this case, it switches back to the parenting menu.
     * This method is invoked when the user selects the "Back" option.
     *
     * @param key The input key, which is ignored in this case since the action is predefined to return to the parent menu.
     */
    override suspend fun query(key: Char) {
        when (key) {
            Settings.getActionKey(Action.SELECT) -> {
                currentMenu = this.parent!!
                Screen.setMenu(currentMenu)
            }
            else -> {}
        }
    }
}
