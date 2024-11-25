package app.src.main.kotlin.com.GameOfLife

class Pixel(private var character: String, private var color: String, private var bgColor: String) {
    private val RESET = "\u001b[0m"

    constructor(character: String) : this(character, "", "")

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
        this.color = color
    }

    public fun setBgColor(bgColor: String) {
        if (bgColor == "grey") {
            this.bgColor = "\u001B[48;5;250m";
            return;
        }
        this.bgColor = bgColor
    }

    companion object {
        fun createArray(line: String): Array<Pixel> {
            return Array(line.length) { Pixel(line[it].toString()) }
        }
    }
}