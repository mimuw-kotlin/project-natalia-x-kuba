package com.gameOfLife.ads
import com.gameOfLife.Pixel
import com.gameOfLife.Settings
import java.io.File

class AdFrame(filePath: String) {
    private val pixels: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel('$') } }

    init {
        try {
            val lines = File(filePath).readLines()
            if (lines.size < Settings.ROWS) {
                throw IllegalArgumentException("The file must have at least $Settings.ROWS rows for characters. Has ${lines.size} rows.")
            }

            // Parse character section (first `height` lines)
            for (row in 0 until Settings.ROWS) {
                val line = lines[row]
                for (col in 0 until line.length) {
                    pixels[row][col] = Pixel(line[col], "white")
                }
                for (col in line.length until Settings.AD_COLS) {
                    pixels[row][col] = Pixel(' ', "white")
                }
            }

            // Parse color section (remaining lines)
            for (line in lines.drop(Settings.ROWS)) {
                if (line.isBlank() || line.startsWith("#")) continue

                val parts = line.split(" ")
                if (parts.size != 4) {
                    throw IllegalArgumentException("Color line must have format: row start end color")
                }

                val row = parts[0].toInt()
                val start = parts[1].toInt()
                val end = parts[2].toInt()
                val color = parts[3]

                if (row !in 0 until Settings.ROWS || start !in 0 until Settings.AD_COLS || end !in start until Settings.AD_COLS) {
                    throw IllegalArgumentException("Invalid range: row=$row, start=$start, end=$end.")
                }

                for (col in start..end) {
                    pixels[row][col].setColor(color)
                }
            }
        } catch (e: Exception) {
            println("Error loading ad from file: ${e.message}")
            throw e
        }
    }

    fun getPixels(): Array<Array<Pixel>> {
        return pixels
    }

    fun changePixel(
        row: Int,
        col: Int,
        pixel: Pixel,
    ) {
        pixels[row][col] = pixel
    }
}
