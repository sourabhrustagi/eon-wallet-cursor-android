package com.mobizonetech.aeon_wallet_cursor.util

import kotlin.system.measureTimeMillis

/**
 * Performance monitoring utilities
 * 
 * Tracks execution time and performance metrics
 */
object PerformanceMonitor {
    
    /**
     * Measure execution time of a block
     * 
     * @param tag Tag for logging
     * @param block Code block to measure
     * @return Result of the block execution
     */
    inline fun <T> measure(tag: String, block: () -> T): T {
        var result: T
        val timeMs = measureTimeMillis {
            result = block()
        }
        Logger.d("PerformanceMonitor", "[$tag] took ${timeMs}ms")
        return result
    }
    
    /**
     * Measure and log if execution time exceeds threshold
     * 
     * @param tag Tag for logging
     * @param thresholdMs Threshold in milliseconds
     * @param block Code block to measure
     * @return Result of the block execution
     */
    inline fun <T> measureWithThreshold(
        tag: String,
        thresholdMs: Long = 1000,
        block: () -> T
    ): T {
        var result: T
        val timeMs = measureTimeMillis {
            result = block()
        }
        
        if (timeMs > thresholdMs) {
            Logger.w(
                "PerformanceMonitor",
                "[$tag] SLOW: ${timeMs}ms (threshold: ${thresholdMs}ms)"
            )
        } else {
            Logger.d("PerformanceMonitor", "[$tag] ${timeMs}ms")
        }
        
        return result
    }
    
    /**
     * Create a stopwatch for manual timing
     */
    class Stopwatch(private val tag: String) {
        private val startTime = System.currentTimeMillis()
        private var lapTime = startTime
        
        /**
         * Record a lap time
         */
        fun lap(label: String) {
            val currentTime = System.currentTimeMillis()
            val lapDuration = currentTime - lapTime
            val totalDuration = currentTime - startTime
            Logger.d(
                "PerformanceMonitor",
                "[$tag] Lap '$label': ${lapDuration}ms (total: ${totalDuration}ms)"
            )
            lapTime = currentTime
        }
        
        /**
         * Stop and log total time
         */
        fun stop(): Long {
            val totalTime = System.currentTimeMillis() - startTime
            Logger.d("PerformanceMonitor", "[$tag] Total: ${totalTime}ms")
            return totalTime
        }
    }
    
    /**
     * Start a stopwatch
     */
    fun startStopwatch(tag: String): Stopwatch {
        Logger.d("PerformanceMonitor", "[$tag] Started")
        return Stopwatch(tag)
    }
}

/**
 * Extension function for measuring suspend functions
 */
suspend inline fun <T> measureSuspend(
    tag: String,
    crossinline block: suspend () -> T
): T {
    val startTime = System.currentTimeMillis()
    val result = block()
    val timeMs = System.currentTimeMillis() - startTime
    Logger.d("PerformanceMonitor", "[$tag] took ${timeMs}ms")
    return result
}

