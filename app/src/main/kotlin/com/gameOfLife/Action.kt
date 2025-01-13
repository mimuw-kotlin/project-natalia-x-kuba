package com.gameOfLife

enum class Action {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    SELECT,
    SPEED_DOWN,
    SPEED_UP,
    MENU,
    QUIT, ;

    companion object {
        fun getNext(action: Action): Action {
            val values = values()
            val nextIndex = (action.ordinal + 1) % values.size
            return values[nextIndex]
        }

        fun getPrev(action: Action): Action {
            val values = values()
            val prevIndex = if (action.ordinal - 1 < 0) values.size - 1 else action.ordinal - 1
            return values[prevIndex]
        }
    }
}
