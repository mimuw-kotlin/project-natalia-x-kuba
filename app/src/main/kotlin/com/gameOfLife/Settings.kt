package com.gameOfLife

/**
 * The `Settings` object contains constant values that define the dimensions of the game board, menu board, and ad board.
 * These values are used throughout the application to maintain consistent sizing and layout.
 */
object Settings {
    const val ROWS = 20
    const val AD_COLS = 20

    const val GAME_BOARD_COLS = 20
    const val MENU_BOARD_COLS = 39

    private val keyBindings: MutableMap<Action, Char> =
        mutableMapOf(
            Action.UP to 'w',
            Action.DOWN to 's',
            Action.LEFT to 'a',
            Action.RIGHT to 'd',
            Action.SELECT to ' ',
            Action.SPEED_DOWN to ',',
            Action.SPEED_UP to '.',
            Action.MENU to 'e',
            Action.QUIT to 'q',
        )

    private val boardCharacters: MutableMap<CellState, Char> =
        mutableMapOf(
            CellState.DEAD to '.',
            CellState.ALIVE to '#',
        )

    private val usedKeys: MutableSet<Char> = keyBindings.values.toMutableSet()

    fun setKey(
        action: Action,
        newKey: Char,
    ): Boolean {
        if (newKey in usedKeys) {
            return false
        }

        keyBindings[action]?.let { oldKey ->
            usedKeys.remove(oldKey)
        }

        keyBindings[action] = newKey
        usedKeys.add(newKey)

        return true
    }

    fun getActionKey(action: Action): Char {
        return keyBindings[action]!!
    }

    fun getCellStateChar(cellState: CellState): Char {
        return boardCharacters[cellState]!!
    }
}
