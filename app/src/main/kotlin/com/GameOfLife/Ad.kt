package app.src.main.kotlin.com.GameOfLife
import java.io.BufferedReader
import kotlinx.coroutines.*
import java.io.InputStreamReader

import com.GameOfLife.Settings

class Ad (val screen: Screen) {
    private var ad1: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }
    private var ad_xtb: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }
    private var ad_wifi: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }
    private var ad_codeforia: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }
    private var current_ad = 1

    private fun getWifiNetworks(): List<String> {
        val networks = mutableListOf<String>()

        try {
            val command = when {
                System.getProperty("os.name").contains("Windows", ignoreCase = true) -> {
                    networks.add("sorry, only Linux")
                    networks.add("users can get")
                    networks.add("hot signals")
                    ""
                    return networks
                }
                System.getProperty("os.name").contains("Mac", ignoreCase = true) -> {
                    networks.add("sorry, only Linux")
                    networks.add("users can get")
                    networks.add("hot signals")
                    ""
                    return networks
                }
                else -> "nmcli dev wifi list"
            }

            val process = ProcessBuilder(command.split(" ")).start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            reader.useLines { lines ->
                lines.forEach { line ->
                    if (!line.contains("SSID", ignoreCase = true) && !line.contains("--")) {
                        val parts = line.drop(1).split(Regex("\\s{2,}")).filter { it.isNotEmpty() }
                        val networkName = parts[1].take(6)
                        val signalBars = parts[parts.size - 2]
                        networks.add(networkName + " " + signalBars)
                    }
                }
            }
        } catch (e: Exception) {
            println("Error while fetching Wi-Fi networks: ${e.message}")
        }

        return networks
    }


    /*
  ┌────────┐
 ┌┘┌──────┐└┐
┌┘┌┘      └┐└┐
│┌┘ ┌────┐ └┐│
││ ┌┘┌──┐└┐ ││
└┘ |┌┘  └┐| └┘
   └┘ ┌┐ └┘
      └┘
 */
    fun init_xtb() {
        ad_xtb[0] = Pixel.createArray("                    ")
        ad_xtb[1] = Pixel.createArray("   ╭━╮╭━┳━━━━┳━━╮   ", "red")
        ad_xtb[2] = Pixel.createArray("   ╰╮╰╯╭┫╭╮╭╮┃╭╮┃   ", "red")
        ad_xtb[3] = Pixel.createArray("   ╱╰╮╭╯╰╯┃┃╰┫╰╯╰╮  ", "red")
        ad_xtb[4] = Pixel.createArray("   ╱╭╯╰╮╱╱┃┃╱┃╭━╮┃  ", "red")
        ad_xtb[5] = Pixel.createArray("   ╭╯╭╮╰╮╱┃┃╱┃╰━╯┃  ", "red")
        ad_xtb[6] = Pixel.createArray("   ╰━╯╰━╯╱╰╯╱╰━━━╯  ", "red")
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
        ad_codeforia[13] = Pixel.createArray(" .--         /      ", "green")
        ad_codeforia[14] = Pixel.createArray("/    ___  __    ___ ", "green")
        ad_codeforia[15] = Pixel.createArray("|__ |   )|  )| |   )", "green")
        ad_codeforia[16] = Pixel.createArray("|   |__/ |   | |__/|", "green")
        ad_codeforia[17] = Pixel.createArray("                    ")
        ad_codeforia[18] = Pixel.createArray("   codeforia.com    ")
        for (i in 7..11) { ad_codeforia[18][i].setColor("green") }
        ad_codeforia[19] = Pixel.createArray(" learn Python now!  ")


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

    private fun init_wifi(state: Int) {
        val networks = getWifiNetworks()
        val lines = List(6) { index ->
            if (index < networks.size) {
                val network = networks[index]
                if (network.length > 20) {
                    network.substring(0, 20)
                } else {
                    network.padEnd(20)
                }
            } else {
                " ".repeat(20)
            }
        }

        ad_wifi[0] = Pixel.createArray("                    ")
        if (state == 1) {
            ad_wifi[1] = Pixel.createArray("                    ", "blue")
            ad_wifi[2] = Pixel.createArray("                    ", "blue")
            ad_wifi[3] = Pixel.createArray("                    ", "blue")
            ad_wifi[4] = Pixel.createArray("                    ", "blue")
            ad_wifi[5] = Pixel.createArray("                    ", "blue")
            ad_wifi[6] = Pixel.createArray("                    ", "blue")
            ad_wifi[7] = Pixel.createArray("         ┌┐         ", "blue")
            ad_wifi[8] = Pixel.createArray("         └┘         ", "blue")
        } else if (state == 2) {
            ad_wifi[1] = Pixel.createArray("                    ", "blue")
            ad_wifi[2] = Pixel.createArray("                    ", "blue")
            ad_wifi[3] = Pixel.createArray("                    ", "blue")
            ad_wifi[4] = Pixel.createArray("       ┌────┐       ", "blue")
            ad_wifi[5] = Pixel.createArray("      ┌┘┌──┐└┐      ", "blue")
            ad_wifi[6] = Pixel.createArray("      |┌┘  └┐|      ", "blue")
            ad_wifi[7] = Pixel.createArray("      └┘ ┌┐ └┘      ", "blue")
            ad_wifi[8] = Pixel.createArray("         └┘         ", "blue")
        } else {
            ad_wifi[1] = Pixel.createArray("     ┌────────┐     ", "blue")
            ad_wifi[2] = Pixel.createArray("    ┌┘┌──────┐└┐    ", "blue")
            ad_wifi[3] = Pixel.createArray("   ┌┘┌┘      └┐└┐   ", "blue")
            ad_wifi[4] = Pixel.createArray("   │┌┘ ┌────┐ └┐│   ", "blue")
            ad_wifi[5] = Pixel.createArray("   ││ ┌┘┌──┐└┐ ││   ", "blue")
            ad_wifi[6] = Pixel.createArray("   └┘ |┌┘  └┐| └┘   ", "blue")
            ad_wifi[7] = Pixel.createArray("      └┘ ┌┐ └┘      ", "blue")
            ad_wifi[8] = Pixel.createArray("         └┘         ", "blue")
        }
        ad_wifi[9] = Pixel.createArray("                    ")
        ad_wifi[10] = Pixel.createArray("HOT SIGNALS IN      ")
        ad_wifi[11] = Pixel.createArray("YOUR AREA           ")
        ad_wifi[12] = Pixel.createArray("                    ")
        ad_wifi[13] = Pixel.createArray(lines[0])
        ad_wifi[14] = Pixel.createArray(lines[1])
        ad_wifi[15] = Pixel.createArray(lines[2])
        ad_wifi[16] = Pixel.createArray(lines[3])
        ad_wifi[17] = Pixel.createArray(lines[4])
        ad_wifi[18] = Pixel.createArray(lines[5])
        ad_wifi[19] = Pixel.createArray("                    ")
    }

    init {
        init_xtb()
        init_codeforia()
        init_wifi(1)
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

                for (i in 0..1) {
                    init_wifi(1)
                    screen.changeAd(ad_wifi)
                    Thread.sleep(500)
                    init_wifi(2)
                    screen.changeAd(ad_wifi)
                    Thread.sleep(500)
                    init_wifi(3)
                    screen.changeAd(ad_wifi)
                    Thread.sleep(500)
                }
            }
        } catch (e: Exception) {
            ;
        }
    }
}