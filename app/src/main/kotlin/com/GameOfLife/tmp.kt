package app.src.main.kotlin.com.GameOfLife

import kotlinx.coroutines.*


fun main() {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    with(scope) {
        val job1 = launch {
            delay(500)
            throw Exception("Ugabuga")
        }

        val job2 = launch {
            println("first print")
            delay(3000)
            println("second print")
        }
    }
    scope.coroutineContext[Job]?.let {job -> runBlocking { job.children.forEach { it.join() } }}
}
