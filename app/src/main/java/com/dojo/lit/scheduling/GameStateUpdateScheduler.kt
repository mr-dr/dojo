package com.dojo.lit.scheduling

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object GameStateUpdateScheduler {
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    fun schedule(runnable: Runnable, delayInMillis: Long) {
        scheduler.scheduleAtFixedRate(runnable, 0, delayInMillis, TimeUnit.MILLISECONDS)
    }

    fun stopScheduledTask() {
        scheduler.shutdown()
    }
}