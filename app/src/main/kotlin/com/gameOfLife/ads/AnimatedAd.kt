package com.gameOfLife.ads

import com.gameOfLife.Screen

class AnimatedAd(public val frames: Array<AdFrame>) {
    fun play(
        screen: Screen,
        time: Int,
        times: Int = 1,
    ) {
        for (i in 0 until times) {
            for (frame in frames) {
                screen.changeAd(frame.getPixels())
                Thread.sleep(1L * (time / (frames.size * times))) // Frame duration
            }
        }
    }
}
