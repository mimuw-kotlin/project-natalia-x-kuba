package com.gameOfLife

import java.util.concurrent.Semaphore

/**
 * The `Timer` class is responsible for managing the in-game time and controlling the speed of the game.
 * It handles the time increments, updates the game state at each tick, and adjusts the game speed.
 * The `run` method continuously updates the game state based on the current speed.
 *
 * @param screen The [Screen] object responsible for displaying the game's visuals, including time, speed, and game board.
 * @param board The [Board] object that holds and updates the state of the game board based on the game rules.
 */
class Timer(var screen: Screen, var board: Board) {
    private var localTime = 0 // Tracks the local time in the game (in seconds)
    private var speed = 1 // Game speed, determines how fast the game progresses (range: 0-5)
    private val sleep = Semaphore(1, true) // Semaphore to control game pause state (sleeping when speed is 0)
    private val mutex = Semaphore(1, true) // Mutex to ensure thread-safe operations

    /**
     * Increases the game speed, up to a maximum of 5.
     * When speed is increased, the game updates the displayed speed and releases any pause state.
     */
    fun increaseSpeed() {
        mutex.acquire() // Acquire mutex to ensure thread safety

        if (speed == 5) {
            mutex.release()
            return
        }
        speed += 1
        if (speed == 1) {
            sleep.release() // Release sleep semaphore if the speed increases from 0 to 1; unpause the game,
        }

        mutex.release()
        screen.updateSpeed(speed)
    }

    /**
     * Decreases the game speed, down to a minimum of 0 (pause state).
     * When speed is decreased, the game updates the displayed speed and pauses if necessary.
     */
    fun decreaseSpeed() {
        mutex.acquire()
        if (speed == 0) {
            mutex.release()
            return
        }

        speed -= 1

        if (speed == 0) {
            mutex.release()
            sleep.acquire() // Pause the game.
        } else {
            mutex.release()
        }
        screen.updateSpeed(speed)
    }

    /**
     * The main loop of the `Timer`. Continuously updates the game state and increments the time.
     * It calculates the new state of the game board at each tick and updates the screen accordingly.
     * The loop respects the current speed and pauses when necessary.
     */
    fun run() {
        try {
            while (true) {
                mutex.acquire()

                // If the game is paused (speed == 0), wait until the speed is increased
                while (speed == 0) {
                    mutex.release()
                    sleep.acquire()
                    mutex.acquire()
                }
                mutex.release()

                Thread.sleep(1000L / speed) // Wait for the appropriate time based on the current speed

                mutex.acquire() // Acquire mutex before modifying the time and game state
                localTime += 1 // Increment the local game time
                screen.updateTime(localTime) // Update the screen with the new time
                board.calculateNewBoard() // Calculate and update the board based on the rules of the game
                sleep.release() // Allow the game to run at the current speed again
                mutex.release() // Release the mutex
            }
        } catch (ignored: InterruptedException) {
            // If the thread is interrupted, exit the loop gracefully
        }
    }
}
