package app.src.main.kotlin.com.GameOfLife

import app.src.main.kotlin.com.GameOfLife.Pixel

class Ad (val screen: Screen) {
    private val ROWS = 20
    private val AD_COLS = 20
    private var ad1: Array<Array<Pixel>> = Array(ROWS) { Array(AD_COLS) { Pixel("$") } }
    private var ad2: Array<Array<Pixel>> = Array(ROWS) { Array(AD_COLS) { Pixel("$") } }
    private var current_add = 1

    init {
        ad1[0] = Pixel.createArray("     .-''''''-.     ")
        ad1[1] = Pixel.createArray("   .'          '.   ")
        ad1[2] = Pixel.createArray("  /   O      O   \\  ")
        ad1[3] = Pixel.createArray(" :                : ")
        ad1[4] = Pixel.createArray(" |                | ")
        ad1[5] = Pixel.createArray(" : ',          ,' : ")
        ad1[6] = Pixel.createArray("  \\  '-......-'  /  ")
        ad1[7] = Pixel.createArray("   '.          .'   ")
        ad1[8] = Pixel.createArray("     '-......-'     ")
        ad1[9] = Pixel.createArray("         |          ")
        ad1[10] = Pixel.createArray("        |           ")
        ad1[11] = Pixel.createArray("        |           ")
        ad1[12] = Pixel.createArray("        |           ")
        ad1[13] = Pixel.createArray("        |           ")
        ad1[14] = Pixel.createArray("        |           ")
        ad1[15] = Pixel.createArray("        |           ")
        ad1[16] = Pixel.createArray("        |           ")
        ad1[17] = Pixel.createArray("        |           ")
        ad1[18] = Pixel.createArray("        |           ")
        ad1[19] = Pixel.createArray("        |           ")
    }

    fun run() {
        try {
            while (true) {
                Thread.sleep(5000L)
                screen.changeAd(ad1)
                Thread.sleep(5000L)
                screen.changeAd(ad2)
                screen.updateScreen()
            }
        } catch (e: Exception) {
            ;
        }
    }
}