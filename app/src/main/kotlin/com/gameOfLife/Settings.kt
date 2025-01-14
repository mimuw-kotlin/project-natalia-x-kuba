package com.gameOfLife

/**
 * The `Settings` object contains constant values that define the dimensions of the game board, menu board, and ad board.
 * These values are used throughout the application to maintain consistent sizing and layout.
 */
object Settings {
    const val ROWS = 20
    const val AD_COLS = 20

    const val MIN_GAME_BOARD_COLS = 20
    const val MAX_GAME_BOARD_COLS = 30
    const val MIN_MENU_BOARD_COLS = MIN_GAME_BOARD_COLS * 2 - 1
    const val MAX_MENU_BOARD_COLS = MAX_GAME_BOARD_COLS * 2 - 1

    var gameBoardCols = 20
    var menuBoardCols = 39

    // Initial position of the camera on the game board
    var camera: Pair<Int, Int> = Pair(0, 0)

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
            Action.WIDTH_DEC to '[',
            Action.WIDTH_INC to ']',
            Action.CAM_UP to 'i',
            Action.CAM_DOWN to 'k',
            Action.CAM_LEFT to 'j',
            Action.CAM_RIGHT to 'l',
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

    fun changeWidth(delta: Int): Boolean {
        if (gameBoardCols + delta in MIN_GAME_BOARD_COLS..MAX_GAME_BOARD_COLS) {
            gameBoardCols += delta
            menuBoardCols += delta * 2
            return true
        }
        return false
    }
}
