package app.src.main.kotlin.com.GameOfLife

class Pixel(private var character: String, private var color: String, private var bgColor: String) {
    private val RESET = "\u001b[0m"

    constructor(character: String) : this(character, "", "")
    constructor(character: String, color: String) : this(character, color, "") {
        setColor(color)
    }

    public fun getValue(): String {
        return this.color + this.bgColor + this.character + RESET
    }

    public fun getCharacter(): String {
        return this.character
    }

    public fun setCharacter(character: String) {
        this.character = character
    }

    public fun setColor(color: String) {
        if (color == "red") {
            this.color = "\u001B[31m"
            return
        }
        if (color == "green") {
            this.color = "\u001B[32m"
            return
        }
        if (color == "black") {
            this.color = "\u001B[30m"
            return
        }
        if (color == "white") {
            this.color = "\u001B[37m"
            return
        }
        if (color == "blue") {
            this.color = "\u001B[34m"
            return
        }
        this.color = color
    }

    public fun setBgColor(bgColor: String) {
        if (bgColor == "grey") {
            this.bgColor = "\u001B[48;5;250m"
            return
        }
        this.bgColor = bgColor
    }

    companion object {
        fun createArray(line: String): Array<Pixel> {
            return Array(line.length) { Pixel(line[it].toString()) }
        }

        fun createArray(
            line: String,
            color: String,
        ): Array<Pixel> {
            return Array(line.length) { Pixel(line[it].toString(), color) }
        }
    }
}
