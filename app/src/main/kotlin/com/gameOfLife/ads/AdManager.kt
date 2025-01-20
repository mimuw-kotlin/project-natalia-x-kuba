package com.gameOfLife.ads

import com.gameOfLife.Pixel
import com.gameOfLife.Screen
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * The `Ad` object manages pixel-based advertisements to be displayed on a screen.
 * It initializes and updates different types of ads, fetches dynamic Wi-Fi network data
 * for use in ads, and cycles through ads in a loop with animations.
 */
object AdManager {
    private val adLame: AnimatedAd
    private val adCodeforia: AnimatedAd
    private val adXtb: AnimatedAd
    private val adWifi: AnimatedAd

    private val ads: Array<AnimatedAd>
    private val networks: MutableList<String> = mutableListOf()

    /**
     * Fetches available Wi-Fi networks on Linux systems using the `nmcli` command.
     * On macOS, it uses the `airport` command to fetch Wi-Fi networks.
     * On non-Unix systems, it returns a static message indicating unsupported functionality.
     */
    private fun initWifiNetworks() {
        try {
            when {
                System.getProperty("os.name").contains("Windows", ignoreCase = true) -> {
                    networks.add("sorry, only Linux")
                    networks.add("users can get")
                    networks.add("hot signals")
                }
                System.getProperty("os.name").contains("Mac", ignoreCase = true) -> {
                    val command =
                        arrayOf(
                            "/bin/bash",
                            "-c",
                            "/System/Library/PrivateFrameworks/Apple80211.framework/Versions" +
                                "/Current/Resources/airport -s | awk 'NR>1{print \$1}'",
                        )

                    val process = ProcessBuilder(*command).start()
                    val reader = BufferedReader(InputStreamReader(process.inputStream))

                    reader.useLines { lines ->
                        lines.forEach { line ->
                            if (line.isNotEmpty()) {
                                networks.add(" $line")
                            }
                        }
                    }
                }
                else -> {
                    val command = "nmcli dev wifi list"
                    val process = ProcessBuilder(command.split(" ")).start()
                    val reader = BufferedReader(InputStreamReader(process.inputStream))

                    reader.useLines { lines ->
                        lines.forEach { line ->
                            if (!line.contains("SSID", ignoreCase = true) && !line.contains("--")) {
                                val parts = line.drop(1).split(Regex("\\s{2,}")).filter { it.isNotEmpty() }
                                val networkName = parts[1].take(6)
                                val signalBars = parts[parts.size - 2]
                                networks.add("$networkName $signalBars")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Error while fetching Wi-Fi networks: ${e.message}")
        }
    }

    /**
     * Initializes the `adWifi` advertisement with dynamic Wi-Fi network data.
     */
    private fun initWifi() {
        val lines =
            MutableList(6) { index ->
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

        if (lines[0] == " ".repeat(20)) {
            lines[0] = "Weird... no Wi-Fi?"
        }

        for (adFrame in adWifi.frames) {
            for (row in 0 until 6) {
                for (col in 0 until 20) {
                    adFrame.changePixel(row + 13, col, Pixel(lines[row][col]))
                }
            }
        }
    }

    init {
        adLame = AnimatedAd(arrayOf((AdFrame("src/main/resources/ads/lame.txt"))))
        runBlocking {
            adLame.play(Screen, 0)
        }
        // adLame.play(Screen, 0)

        adCodeforia = AnimatedAd(arrayOf(AdFrame("src/main/resources/ads/codeforia.txt")))
        adXtb = AnimatedAd(arrayOf(AdFrame("src/main/resources/ads/xtb.txt")))

        adWifi =
            AnimatedAd(
                arrayOf(
                    AdFrame("src/main/resources/ads/wifi1.txt"),
                    AdFrame("src/main/resources/ads/wifi2.txt"),
                    AdFrame("src/main/resources/ads/wifi3.txt"),
                ),
            )
        initWifiNetworks()
        initWifi()

        ads = arrayOf(adCodeforia, adXtb, adWifi)
    }

    /**
     * Main function to cycle through ads and update the screen with each ad.
     */
    suspend fun run() {
        try {
            while (true) {
                // Cycle through the ads in the list
                for (ad in ads) {
                    ad.play(Screen, 3000, 2)
                }
            }
        } catch (_: Exception) {
        }
    }
}
