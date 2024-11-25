package app.src.main.kotlin.com.GameOfLife

import java.util.concurrent.Semaphore

class Timer (var screen: Screen) {
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
        screen.updateSpeed(speed)

        if (speed == 1) {
            sleep.release()
        }
        mutex.release()
    }

    fun decreaseSpeed() {
        mutex.acquire()
        if (speed == 0) {
            mutex.release()
            return
        }

        speed -= 1
        screen.updateSpeed(speed)

        if (speed == 0) {
            mutex.release()
            sleep.acquire()
        } else {
            mutex.release()
        }
    }

    fun run () {
        try {
            while (true) {
                mutex.acquire()
                while (speed == 0) {
                    mutex.release()
                    sleep.acquire()
                    mutex.acquire()
                }
                Thread.sleep(1000L / speed)
                localTime += 1
                screen.updateTime(localTime)
                sleep.release()
                mutex.release()
            }
        }
        catch (ignored: InterruptedException) {
            ;
        }
    }
}