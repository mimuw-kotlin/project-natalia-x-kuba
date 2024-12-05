package app.src.main.kotlin.com.GameOfLife

import com.GameOfLife.Settings
import com.GameOfLife.Clickable
import com.GameOfLife.PremiumMenu

object MainMenu : Menu() {
    override var text: String = "Main Menu"
    override var parent: Menu? = null
    override var children: Array<Clickable> = arrayOf(
        PremiumMenu, PremiumMenu, PremiumMenu, PremiumMenu, PremiumMenu
    )
    var currentMenu: Menu = this

    init {
        board[0] = centerText(this.text)

        for (i in 10..10+children.size-1) {
            board[i] = centerText(children[i-10].text)
        }

        for (i in 10+children.size..<Settings.ROWS) {
            board[i] = centerText("")
        }
    }

    override fun query(key: Char) {
        if (this.currentMenu != this) {
            this.currentMenu.query(key)
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
                    this.currentMenu = children[cursor] as Menu
                    this.children[cursor].parent = this
                    screen.setMenu(this.currentMenu)
                } else {
                    children[cursor].query(' ')
                }
            }
        }
    }

    override fun reset() {
        this.currentMenu = this
        unmarkHovered()
        cursor = 0
        if (!this.children.isNullOrEmpty() && this.children.isNotEmpty()) {
            markHovered()
        }
    }
}