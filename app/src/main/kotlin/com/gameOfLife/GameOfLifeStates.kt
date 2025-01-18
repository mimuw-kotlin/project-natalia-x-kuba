package com.gameOfLife

enum class CellState {
    DEAD,
    ALIVE,
}

enum class GameOrMenu {
    GAME,
    MENU,
}

enum class CellRange {
    ALIVE_LOW,
    ALIVE_HIGH,
    DEAD_LOW,
    DEAD_HIGH, ;

    companion object {
        fun getNext(cellRange: CellRange): CellRange {
            val values = CellRange.values()
            val nextIndex = (cellRange.ordinal + 1) % values.size
            return values[nextIndex]
        }

        fun getPrev(cellRange: CellRange): CellRange {
            val values = CellRange.values()
            val prevIndex = if (cellRange.ordinal - 1 < 0) values.size - 1 else cellRange.ordinal - 1
            return values[prevIndex]
        }
    }
}
