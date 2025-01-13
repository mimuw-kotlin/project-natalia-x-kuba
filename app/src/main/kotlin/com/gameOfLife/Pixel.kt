package com.gameOfLife

/**
 * The `Pixel` class represents a single character in the game board with its associated foreground and background colors.
 * It encapsulates the character, its color, and its background color using ANSI escape sequences to control terminal output.
 * This class provides methods to manipulate and display the pixel with custom formatting.
 *
 * @param character The character to be displayed in the pixel.
 * @param color The color of the character. This is an ANSI color code or a custom color code.
 * @param bgColor The background color of the pixel. This is an ANSI background color code or a custom color code.
 */
class Pixel(private var character: Char, private var color: String, private var bgColor: String) {
    // ANSI escape sequence to reset text formatting
    private val reset = "\u001b[0m"

    /**
     * Secondary constructor for creating a pixel with a character.
     * This constructor initializes the pixel with the character and empty color and background color.
     *
     * @param character The character to display in the pixel.
     */
    constructor(character: Char) : this(character, "", "")

    /**
     * Secondary constructor for creating a pixel with a character and a color.
     * This constructor initializes the pixel with the character and sets its color.
     *
     * @param character The character to display in the pixel.
     * @param color The color of the character. Supported values include "red", "green", "blue", "black", "white", or a custom color code.
     */
    constructor(character: Char, color: String) : this(character, color, "") {
        setColor(color)
    }

    /**
     * Returns the string representation of the pixel, formatted with color and background color.
     * This value is used for rendering the pixel on the screen with its styles.
     *
     * @return A string containing the pixel's color, background color, and character, formatted for console output.
     */
    public fun getValue(): String {
        return this.color + this.bgColor + this.character + reset
    }

    /**
     * Returns the character of the pixel.
     *
     * @return The character stored in the pixel.
     */
    public fun getCharacter(): Char {
        return this.character
    }

    /**
     * Sets the character of the pixel.
     *
     * @param character The new character to display in the pixel.
     */
    public fun setCharacter(character: Char) {
        this.character = character
    }

    /**
     * Sets the color of the pixel.
     * The color is set using ANSI escape sequences. If a predefined color is provided (e.g., "red", "green"),
     * the corresponding escape sequence is used. If a custom color is passed, it is set directly.
     *
     * @param color The color to set for the pixel. Supported values include:
     *              "red", "green", "black", "white", "blue", or a custom color code (e.g., "#FF5733").
     */
    public fun setColor(color: String) {
        when (color) {
            "red" -> this.color = "\u001B[31m"
            "green" -> this.color = "\u001B[32m"
            "black" -> this.color = "\u001B[30m"
            "white" -> this.color = "\u001B[37m"
            "blue" -> this.color = "\u001B[34m"
            else -> this.color = color
        }
    }

    /**
     * Sets the background color of the pixel.
     * The background color is set using ANSI escape sequences. If a predefined background color is provided
     * (e.g., "grey"), the corresponding escape sequence is used. If a custom background color is provided, it is used directly.
     *
     * @param bgColor The background color to set for the pixel. Supported values include:
     *                "grey", or any custom color code (e.g., "#F0F0F0").
     */
    public fun setBgColor(bgColor: String) {
        if (bgColor == "grey") {
            this.bgColor = "\u001B[48;5;250m" // ANSI escape sequence for grey background
            return
        }
        this.bgColor = bgColor
    }

    companion object {
        /**
         * Creates an array of `Pixel` objects from a string, where each character in the string becomes a `Pixel` object.
         *
         * @param line The string to convert into an array of `Pixel` objects. Each character in the string becomes a `Pixel`.
         * @return An array of `Pixel` objects where each element represents a character from the string.
         */
        fun createArray(line: String): Array<Pixel> {
            return Array(line.length) { Pixel(line[it]) }
        }

        /**
         * Creates an array of `Pixel` objects from a string, with a specified color for each pixel.
         * Each character in the string becomes a `Pixel` object with the specified color.
         *
         * @param line The string to convert into an array of `Pixel` objects. Each character in the string becomes a `Pixel`.
         * @param color The color to apply to all pixels in the array.
         * @return An array of `Pixel` objects where each element represents a character from the string, colored according to the specified color.
         */
        fun createArray(
            line: String,
            color: String,
        ): Array<Pixel> {
            return Array(line.length) { Pixel(line[it], color) }
        }
    }
}
