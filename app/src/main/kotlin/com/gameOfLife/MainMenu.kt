package com.gameOfLife

object MainMenu : Menu() {
    override var text: String = "Main Menu"
    override var parent: Menu? = null
    override var children: Array<Clickable> =
        arrayOf(
            PremiumMenu,
            PremiumMenu,
            PremiumMenu,
            PremiumMenu,
            PremiumMenu,
        )
    var currentMenu: Menu = this

    init {
        board[0] = centerText(text)

        for (i in 10..10 + children.size - 1) {
            board[i] = centerText(children[i - 10].text)
        }

        for (i in 10 + children.size..<Settings.ROWS) {
            board[i] = centerText("")
        }
    }

    override fun query(key: Char) {
        if (currentMenu != this) {
            currentMenu.query(key)
            return
        }

        when (key) {
            'w' -> {
                unmarkHovered()
                cursor = (cursor - 1 + children.size) % children.size
                markHovered()
                screen.updateMenuBoard(board)
            }
            's' -> {
                unmarkHovered()
                cursor = (cursor + 1) % children.size
                markHovered()
                screen.updateMenuBoard(board)
            }
            ' ' -> {
                if (children[cursor] is Menu) {
                    currentMenu = children[cursor] as Menu
                    children[cursor].parent = this
                    screen.setMenu(currentMenu)
                } else {
                    children[cursor].query(' ')
                }
            }
        }
    }

    override fun reset() {
        currentMenu = this
        unmarkHovered()
        cursor = 0
        if (!children.isNullOrEmpty() && children.isNotEmpty()) {
            markHovered()
        }
    }
}
