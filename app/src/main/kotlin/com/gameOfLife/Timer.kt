package com.gameOfLife

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 * The `Timer` class is responsible for managing the in-game time and controlling the speed of the game.
 * It handles the time increments, updates the game state at each tick, and adjusts the game speed.
 * The `run` method continuously updates the game state based on the current speed.
 */
object Timer {
    private var localTime = AtomicInteger(0) // Tracks the local time in the game (in seconds)
    private val speed = AtomicInteger(1) // Game speed, determines how fast the game progresses (range: 0-5)

    private var timerJob: Job? = null // Job to manage the coroutine running the game loop
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob()) // Coroutine scope for the timer

    /**
     * Increases the game speed, up to a maximum of 5.
     * When speed is increased, the game updates the displayed speed and resumes if paused.
     */
    suspend fun increaseSpeed() {
        val currentSpeed = speed.get()
        if (currentSpeed < 5) {
            speed.incrementAndGet()
            Screen.updateSpeed(speed.get())
            resumeIfPaused()
        }
    }

    /**
     * Decreases the game speed, down to a minimum of 0 (pause state).
     * When speed is decreased, the game updates the displayed speed and pauses if necessary.
     */
    suspend fun decreaseSpeed() {
        val currentSpeed = speed.get()
        if (currentSpeed > 0) {
            speed.decrementAndGet()
            Screen.updateSpeed(speed.get())
        }
    }

    /**
     * Starts or resumes the game loop. If the game is paused, it will resume.
     */
    suspend fun run() {
        if (timerJob == null || timerJob?.isCancelled == true) {
            timerJob =
                scope.launch {
                    while (isActive) {
                        val currentSpeed = speed.get()

                        if (currentSpeed == 0) {
                            pauseGame()
                            delay(Long.MAX_VALUE)
                        }

                        delay(1000L / currentSpeed)

                        localTime.incrementAndGet()
                        Screen.updateTime(localTime.get())
                        GameEngine.calculateNewBoard()
                    }
                }
        }
    }

    /**
     * Pauses the game by canceling the active timer coroutine.
     */
    private fun pauseGame() {
        timerJob?.cancel()
    }

    /**
     * Resumes the game if it is currently paused and the speed is greater than 0.
     */
    private suspend fun resumeIfPaused() {
        if (speed.get() > 0 && (timerJob == null || timerJob?.isCancelled == true)) {
            run()
        }
    }

    /**
     * Stops the game completely by canceling the job and resetting the timer state.
     */
    suspend fun stop() {
        timerJob?.cancel()
        localTime.set(0)
        speed.set(1)
        Screen.updateTime(localTime.get())
        Screen.updateSpeed(speed.get())
    }
}
