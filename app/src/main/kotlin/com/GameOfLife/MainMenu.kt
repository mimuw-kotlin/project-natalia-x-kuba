package app.src.main.kotlin.com.GameOfLife

import com.GameOfLife.Settings
import com.GameOfLife.Clickable
import com.GameOfLife.PremiumMenu

object MainMenu : Menu() {
    override var text: String = "Main Menu"
    override var children: Array<Pair<Clickable, Int>> = arrayOf(
        PremiumMenu to 0, PremiumMenu to 0
    )

    init {
        board[0] = centerText(this.text)


        for (i in 10..10+children.size-1) {
            board[i] = centerText(children[i-10].first.text)
        }

        for (i in 10+children.size..<Settings.ROWS) {
            board[i] = centerText("")
        }
    }

    override fun query(key: Char) {
        // println("Key pressed: $key")
    }
}