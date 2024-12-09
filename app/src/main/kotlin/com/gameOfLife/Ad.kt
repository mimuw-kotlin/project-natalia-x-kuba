package com.gameOfLife
import app.src.main.kotlin.com.GameOfLife.Pixel
import app.src.main.kotlin.com.GameOfLife.Screen
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * The `Ad` class manages pixel-based advertisements to be displayed on a screen.
 * It initializes and updates different types of ads, fetches dynamic Wi-Fi network data
 * for use in ads, and cycles through ads in a loop with animations.
 *
 * @property screen the `Screen` object where the ads will be displayed.
 */
class Ad(val screen: Screen) {
    // 2D array representing a predefined advertisement
    private var ad1: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }
    private var adXtb: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }
    private var adWifi: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }
    private var adCodeforia: Array<Array<Pixel>> = Array(Settings.ROWS) { Array(Settings.AD_COLS) { Pixel("$") } }

    // List to hold multiple advertisements
    private val ads: MutableList<Array<Array<Pixel>>> = mutableListOf(adXtb, adCodeforia, ad1)

    /**
     * Fetches available Wi-Fi networks on Linux systems using the `nmcli` command.
     * On non-Linux systems, it returns a static message indicating unsupported functionality.
     *
     * @return a list of strings representing Wi-Fi network names and their signal strengths.
     */
    private fun getWifiNetworks(): List<String> {
        val networks = mutableListOf<String>()

        try {
            val command =
                when {
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

    /**
     * Initializes the `adXtb` advertisement with predefined pixel art and text.
     */
    fun initXtb() {
        adXtb[0] = Pixel.createArray("                    ")
        adXtb[1] = Pixel.createArray("   ╭━╮╭━┳━━━━┳━━╮   ", "red")
        adXtb[2] = Pixel.createArray("   ╰╮╰╯╭┫╭╮╭╮┃╭╮┃   ", "red")
        adXtb[3] = Pixel.createArray("   ╱╰╮╭╯╰╯┃┃╰┫╰╯╰╮  ", "red")
        adXtb[4] = Pixel.createArray("   ╱╭╯╰╮╱╱┃┃╱┃╭━╮┃  ", "red")
        adXtb[5] = Pixel.createArray("   ╭╯╭╮╰╮╱┃┃╱┃╰━╯┃  ", "red")
        adXtb[6] = Pixel.createArray("   ╰━╯╰━╯╱╰╯╱╰━━━╯  ", "red")
        adXtb[7] = Pixel.createArray("                    ")
        adXtb[8] = Pixel.createArray("                    ")
        adXtb[9] = Pixel.createArray("                    ")
        adXtb[10] = Pixel.createArray("ɪɴᴠᴇsᴛɪɴɢ           ")
        adXtb[11] = Pixel.createArray("ᴍᴀʀᴋᴇᴛ ᴀɴᴀʟʏsɪs     ")
        adXtb[12] = Pixel.createArray("ᴇᴅᴜᴄᴀᴛɪᴏɴ           ")
        adXtb[13] = Pixel.createArray("                    ")
        adXtb[14] = Pixel.createArray("                    ")
        adXtb[15] = Pixel.createArray("                    ")
        adXtb[16] = Pixel.createArray("Over 1000000 clients")
        adXtb[17] = Pixel.createArray("have trusted us.    ")
        adXtb[18] = Pixel.createArray("74% of retail CFD   ")
        adXtb[19] = Pixel.createArray("users incur losses  ")
    }

    /**
     * Initializes the `adCodeforia` advertisement with pixel art and a marketing message.
     */
    fun initCodeforia() {
        adCodeforia[0] = Pixel.createArray("                    ")
        adCodeforia[1] = Pixel.createArray("   ▓█████▓  ▓█████▓ ")
        for (i in 10..19) {
            adCodeforia[1][i].setColor("green")
        }
        adCodeforia[2] = Pixel.createArray(" ▓████▒▓▓  ███▓▓▓▓  ")
        for (i in 10..19) {
            adCodeforia[2][i].setColor("green")
        }
        adCodeforia[3] = Pixel.createArray(" ███       ██▓      ")
        for (i in 10..19) {
            adCodeforia[3][i].setColor("green")
        }
        adCodeforia[4] = Pixel.createArray(" ███▓     ███▓ ██   ")
        for (i in 10..19) {
            adCodeforia[4][i].setColor("green")
        }
        adCodeforia[5] = Pixel.createArray("  ▒███████▒▒▓       ")
        for (i in 9..19) {
            adCodeforia[5][i].setColor("green")
        }
        adCodeforia[6] = Pixel.createArray("    ▓▒▒▒▒▓          ")
        for (i in 8..19) {
            adCodeforia[6][i].setColor("green")
        }
        adCodeforia[7] = Pixel.createArray("                    ")
        adCodeforia[8] = Pixel.createArray("                    ")

        adCodeforia[9] = Pixel.createArray("              |     ")
        adCodeforia[10] = Pixel.createArray(" ___  ___  ___| ___ ")
        adCodeforia[11] = Pixel.createArray("|    |   )|   )|___)")
        adCodeforia[12] = Pixel.createArray("|___ |__/ |__/ |__  ")
        adCodeforia[13] = Pixel.createArray(" .--         /      ", "green")
        adCodeforia[14] = Pixel.createArray("/    ___  __    ___ ", "green")
        adCodeforia[15] = Pixel.createArray("|__ |   )|  )| |   )", "green")
        adCodeforia[16] = Pixel.createArray("|   |__/ |   | |__/|", "green")
        adCodeforia[17] = Pixel.createArray("                    ")
        adCodeforia[18] = Pixel.createArray("   codeforia.com    ")
        for (i in 7..11) {
            adCodeforia[18][i].setColor("green")
        }
        adCodeforia[19] = Pixel.createArray(" learn Python now!  ")
    }

    /**
     * Initializes the `adWifi` advertisement with dynamic Wi-Fi network data.
     * The appearance changes based on the `state` parameter.
     *
     * @param state an integer determining the Wi-Fi animation state.
     */
    private fun initWifi(state: Int) {
        val networks = getWifiNetworks()
        val lines =
            List(6) { index ->
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

        adWifi[0] = Pixel.createArray("                    ")
        if (state == 1) {
            adWifi[1] = Pixel.createArray("                    ", "blue")
            adWifi[2] = Pixel.createArray("                    ", "blue")
            adWifi[3] = Pixel.createArray("                    ", "blue")
            adWifi[4] = Pixel.createArray("                    ", "blue")
            adWifi[5] = Pixel.createArray("                    ", "blue")
            adWifi[6] = Pixel.createArray("                    ", "blue")
            adWifi[7] = Pixel.createArray("         ┌┐         ", "blue")
            adWifi[8] = Pixel.createArray("         └┘         ", "blue")
        } else if (state == 2) {
            adWifi[1] = Pixel.createArray("                    ", "blue")
            adWifi[2] = Pixel.createArray("                    ", "blue")
            adWifi[3] = Pixel.createArray("                    ", "blue")
            adWifi[4] = Pixel.createArray("       ┌────┐       ", "blue")
            adWifi[5] = Pixel.createArray("      ┌┘┌──┐└┐      ", "blue")
            adWifi[6] = Pixel.createArray("      |┌┘  └┐|      ", "blue")
            adWifi[7] = Pixel.createArray("      └┘ ┌┐ └┘      ", "blue")
            adWifi[8] = Pixel.createArray("         └┘         ", "blue")
        } else {
            adWifi[1] = Pixel.createArray("     ┌────────┐     ", "blue")
            adWifi[2] = Pixel.createArray("    ┌┘┌──────┐└┐    ", "blue")
            adWifi[3] = Pixel.createArray("   ┌┘┌┘      └┐└┐   ", "blue")
            adWifi[4] = Pixel.createArray("   │┌┘ ┌────┐ └┐│   ", "blue")
            adWifi[5] = Pixel.createArray("   ││ ┌┘┌──┐└┐ ││   ", "blue")
            adWifi[6] = Pixel.createArray("   └┘ |┌┘  └┐| └┘   ", "blue")
            adWifi[7] = Pixel.createArray("      └┘ ┌┐ └┘      ", "blue")
            adWifi[8] = Pixel.createArray("         └┘         ", "blue")
        }
        adWifi[9] = Pixel.createArray("                    ")
        adWifi[10] = Pixel.createArray("HOT SIGNALS IN      ")
        adWifi[11] = Pixel.createArray("YOUR AREA           ")
        adWifi[12] = Pixel.createArray("                    ")
        adWifi[13] = Pixel.createArray(lines[0])
        adWifi[14] = Pixel.createArray(lines[1])
        adWifi[15] = Pixel.createArray(lines[2])
        adWifi[16] = Pixel.createArray(lines[3])
        adWifi[17] = Pixel.createArray(lines[4])
        adWifi[18] = Pixel.createArray(lines[5])
        adWifi[19] = Pixel.createArray("                    ")
    }

    init {
        initXtb()
        initCodeforia()
        initWifi(1)
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

    /**
     * Main function to cycle through ads and update the screen with each ad.
     */
    fun run() {
        try {
            while (true) {
                // Cycle through the ads in the list
                for (ad in ads) {
                    screen.changeAd(ad)
                    Thread.sleep(3000L)
                }

                // Show animated Wi-Fi ad
                for (i in 0..1) {
                    initWifi(1)
                    screen.changeAd(adWifi)
                    Thread.sleep(500)
                    initWifi(2)
                    screen.changeAd(adWifi)
                    Thread.sleep(500)
                    initWifi(3)
                    screen.changeAd(adWifi)
                    Thread.sleep(500)
                }
            }
        } catch (e: Exception) {
        }
    }
}
