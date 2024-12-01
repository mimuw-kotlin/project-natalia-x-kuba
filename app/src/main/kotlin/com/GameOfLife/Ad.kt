package app.src.main.kotlin.com.GameOfLife

import app.src.main.kotlin.com.GameOfLife.Pixel

class Ad (val screen: Screen) {
    private val ROWS = 20
    private val AD_COLS = 20
    private var ad1: Array<Array<Pixel>> = Array(ROWS) { Array(AD_COLS) { Pixel("$") } }
    private var ad_xtb: Array<Array<Pixel>> = Array(ROWS) { Array(AD_COLS) { Pixel("$") } }
    private var ad_codeforia: Array<Array<Pixel>> = Array(ROWS) { Array(AD_COLS) { Pixel("$") } }
    private var current_add = 1

    fun init_xtb() {
        ad_xtb[0] = Pixel.createArray("                    ")
        ad_xtb[1] = Pixel.createArray("   ╭━╮╭━┳━━━━┳━━╮   ", "\u001B[31m")
        ad_xtb[2] = Pixel.createArray("   ╰╮╰╯╭┫╭╮╭╮┃╭╮┃   ", "\u001B[31m")
        ad_xtb[3] = Pixel.createArray("   ╱╰╮╭╯╰╯┃┃╰┫╰╯╰╮  ", "\u001B[31m")
        ad_xtb[4] = Pixel.createArray("   ╱╭╯╰╮╱╱┃┃╱┃╭━╮┃  ", "\u001B[31m")
        ad_xtb[5] = Pixel.createArray("   ╭╯╭╮╰╮╱┃┃╱┃╰━╯┃  ", "\u001B[31m")
        ad_xtb[6] = Pixel.createArray("   ╰━╯╰━╯╱╰╯╱╰━━━╯  ", "\u001B[31m")
        ad_xtb[7] = Pixel.createArray("                    ")
        ad_xtb[8] = Pixel.createArray("                    ")
        ad_xtb[9] = Pixel.createArray("                    ")
        ad_xtb[10] = Pixel.createArray("ɪɴᴠᴇsᴛɪɴɢ           ")
        ad_xtb[11] = Pixel.createArray("ᴍᴀʀᴋᴇᴛ ᴀɴᴀʟʏsɪs     ")
        ad_xtb[12] = Pixel.createArray("ᴇᴅᴜᴄᴀᴛɪᴏɴ           ")
        ad_xtb[13] = Pixel.createArray("                    ")
        ad_xtb[14] = Pixel.createArray("                    ")
        ad_xtb[15] = Pixel.createArray("                    ")
        ad_xtb[16] = Pixel.createArray("Over 1000000 clients")
        ad_xtb[17] = Pixel.createArray("have trusted us.    ")
        ad_xtb[18] = Pixel.createArray("74% of retail CFD   ")
        ad_xtb[19] = Pixel.createArray("users incur losses  ")
    }

    fun init_codeforia() {
        ad_codeforia[0] = Pixel.createArray("                    ")
        ad_codeforia[1] = Pixel.createArray("   ▓█████▓  ▓█████▓ ")
        for (i in 10..19) { ad_codeforia[1][i].setColor("green") }
        ad_codeforia[2] = Pixel.createArray(" ▓████▒▓▓  ███▓▓▓▓  ")
        for (i in 10..19) { ad_codeforia[2][i].setColor("green") }
        ad_codeforia[3] = Pixel.createArray(" ███       ██▓      ")
        for (i in 10..19) { ad_codeforia[3][i].setColor("green") }
        ad_codeforia[4] = Pixel.createArray(" ███▓     ███▓ ██   ")
        for (i in 10..19) { ad_codeforia[4][i].setColor("green") }
        ad_codeforia[5] = Pixel.createArray("  ▒███████▒▒▓       ")
        for (i in 9..19) { ad_codeforia[5][i].setColor("green") }
        ad_codeforia[6] = Pixel.createArray("    ▓▒▒▒▒▓          ")
        for (i in 8..19) { ad_codeforia[6][i].setColor("green") }
        ad_codeforia[7] = Pixel.createArray("                    ")
        ad_codeforia[8] = Pixel.createArray("                    ")

        ad_codeforia[9] = Pixel.createArray("              |     ")
        ad_codeforia[10] = Pixel.createArray(" ___  ___  ___| ___ ")
        ad_codeforia[11] = Pixel.createArray("|    |   )|   )|___)")
        ad_codeforia[12] = Pixel.createArray("|___ |__/ |__/ |__  ")
        ad_codeforia[13] = Pixel.createArray(" .--         /      ", "\u001B[32m")
        ad_codeforia[14] = Pixel.createArray("/    ___  __    ___ ", "\u001B[32m")
        ad_codeforia[15] = Pixel.createArray("|__ |   )|  )| |   )", "\u001B[32m")
        ad_codeforia[16] = Pixel.createArray("|   |__/ |   | |__/|", "\u001B[32m")
        ad_codeforia[17] = Pixel.createArray("                    ")
        ad_codeforia[18] = Pixel.createArray("codefora.com - learn")
        ad_codeforia[19] = Pixel.createArray("Python for free now!")
        for (i in 10..15) { ad_codeforia[19][i].setColor("green") }


        /*
         _ _  _| _  |` _  _. _
        (_(_)(_|(/_~|~(_)| |(_|
         */

        /*
                              __
              |      /              /
 ___  ___  ___| ___ (     ___  ___    ___
|    |   )|   )|___)|___ |   )|   )| |   )
|__  |__/ |__/ |__  |    |__/ |    | |__/|
         */

    }

    init {
        init_xtb()
        init_codeforia()
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
                screen.changeAd(ad_xtb)
                Thread.sleep(3000L)
                screen.changeAd(ad_codeforia)
                Thread.sleep(3000L)
            }
        } catch (e: Exception) {
            ;
        }
    }
}