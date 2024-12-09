package com.gameOfLife

import app.src.main.kotlin.com.GameOfLife.Board
import app.src.main.kotlin.com.GameOfLife.Screen
import java.util.concurrent.Semaphore

class Timer(var screen: Screen, var board: Board) {
    private var localTime = 0
    private var speed = 1
    private val sleep = Semaphore(1, true)
    private val mutex = Semaphore(1, true)

    fun increaseSpeed() {
        mutex.acquire()
        if (speed == 5) {
            mutex.release()
            return
        }

        speed += 1

        if (speed == 1) {
            sleep.release()
        }
        mutex.release()
        screen.updateSpeed(speed)
    }

    fun decreaseSpeed() {
        mutex.acquire()
        if (speed == 0) {
            mutex.release()
            return
        }

        speed -= 1

        if (speed == 0) {
            mutex.release()
            sleep.acquire()
        } else {
            mutex.release()
        }
        screen.updateSpeed(speed)
    }

    fun run() {
        try {
            while (true) {
                mutex.acquire()
                while (speed == 0) {
                    mutex.release()
                    sleep.acquire()
                    mutex.acquire()
                }
                mutex.release()
                Thread.sleep(1000L / speed) // FIXME: This is not the correct way to implement a timer
                mutex.acquire()
                localTime += 1
                screen.updateTime(localTime)
                board.calculateNewBoard()
                sleep.release()
                mutex.release()
            }
        } catch (ignored: InterruptedException) {
        }
    }
}
