package com.mobizonetech.aeon_wallet_cursor.data.remote.retry

import com.mobizonetech.aeon_wallet_cursor.util.Logger
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.min
import kotlin.math.pow

/**
 * Retry policy for API calls with exponential backoff
 * 
 * Features:
 * - Configurable retry attempts
 * - Exponential backoff with jitter
 * - Retryable error detection
 * - Detailed logging
 * 
 * Usage:
 * ```kotlin
 * val result = RetryPolicy.executeWithRetry {
 *     apiService.getData()
 * }
 * ```
 */
object RetryPolicy {
    
    private const val TAG = "RetryPolicy"
    
    /**
     * Default number of retry attempts
     */
    const val DEFAULT_MAX_RETRIES = 3
    
    /**
     * Initial delay in milliseconds before first retry
     */
    const val DEFAULT_INITIAL_DELAY_MS = 1000L
    
    /**
     * Maximum delay in milliseconds between retries
     */
    const val DEFAULT_MAX_DELAY_MS = 10000L
    
    /**
     * Multiplier for exponential backoff
     */
    const val DEFAULT_BACKOFF_MULTIPLIER = 2.0
    
    /**
     * Execute a suspending block with retry logic
     * 
     * @param maxRetries Maximum number of retry attempts (default: 3)
     * @param initialDelayMs Initial delay before first retry (default: 1000ms)
     * @param maxDelayMs Maximum delay between retries (default: 10000ms)
     * @param backoffMultiplier Exponential backoff multiplier (default: 2.0)
     * @param shouldRetry Custom predicate to determine if error is retryable
     * @param block The suspending block to execute
     * @return Result of the block execution
     * @throws Exception The last exception if all retries fail
     */
    suspend fun <T> executeWithRetry(
        maxRetries: Int = DEFAULT_MAX_RETRIES,
        initialDelayMs: Long = DEFAULT_INITIAL_DELAY_MS,
        maxDelayMs: Long = DEFAULT_MAX_DELAY_MS,
        backoffMultiplier: Double = DEFAULT_BACKOFF_MULTIPLIER,
        shouldRetry: (Exception) -> Boolean = ::isRetryableError,
        block: suspend () -> T
    ): T {
        var currentAttempt = 0
        var lastException: Exception? = null
        
        while (currentAttempt <= maxRetries) {
            try {
                if (currentAttempt > 0) {
                    Logger.d(TAG, "Retry attempt $currentAttempt of $maxRetries")
                }
                
                return block()
                
            } catch (e: Exception) {
                lastException = e
                
                // Check if we should retry
                if (!shouldRetry(e)) {
                    Logger.e(TAG, "Non-retryable error encountered", e)
                    throw e
                }
                
                // Check if we've exhausted all retries
                if (currentAttempt >= maxRetries) {
                    Logger.e(TAG, "Max retries ($maxRetries) exhausted", e)
                    throw e
                }
                
                // Calculate delay with exponential backoff and jitter
                val delay = calculateDelay(
                    attempt = currentAttempt,
                    initialDelayMs = initialDelayMs,
                    maxDelayMs = maxDelayMs,
                    backoffMultiplier = backoffMultiplier
                )
                
                Logger.w(TAG, "Attempt $currentAttempt failed: ${e.message}. Retrying in ${delay}ms...")
                
                // Wait before retry
                delay(delay)
                
                currentAttempt++
            }
        }
        
        // This should never be reached, but just in case
        throw lastException ?: IllegalStateException("Retry logic failed unexpectedly")
    }
    
    /**
     * Calculate delay for next retry using exponential backoff with jitter
     * 
     * Formula: min(maxDelay, initialDelay * (multiplier ^ attempt)) + jitter
     * Jitter: Random value between 0 and 20% of calculated delay
     * 
     * @param attempt Current attempt number (0-based)
     * @param initialDelayMs Initial delay in milliseconds
     * @param maxDelayMs Maximum delay cap
     * @param backoffMultiplier Exponential multiplier
     * @return Calculated delay in milliseconds
     */
    fun calculateDelay(
        attempt: Int,
        initialDelayMs: Long,
        maxDelayMs: Long,
        backoffMultiplier: Double
    ): Long {
        // Calculate exponential backoff
        val exponentialDelay = (initialDelayMs * backoffMultiplier.pow(attempt.toDouble())).toLong()
        
        // Cap at max delay
        val cappedDelay = min(exponentialDelay, maxDelayMs)
        
        // Add jitter (0-20% of delay)
        val jitter = (cappedDelay * 0.2 * Math.random()).toLong()
        
        return cappedDelay + jitter
    }
    
    /**
     * Determine if an exception is retryable
     * 
     * Retryable errors:
     * - Network timeouts (SocketTimeoutException)
     * - Network unavailable (UnknownHostException)
     * - General IO exceptions (IOException)
     * - HTTP 5xx server errors (detected via message)
     * - HTTP 429 Too Many Requests
     * - HTTP 503 Service Unavailable
     * 
     * Non-retryable errors:
     * - HTTP 4xx client errors (except 429)
     * - Authentication errors
     * - Validation errors
     * - Parsing errors
     * 
     * @param exception The exception to check
     * @return true if the error is retryable, false otherwise
     */
    fun isRetryableError(exception: Exception): Boolean {
        return when (exception) {
            // Network timeouts are retryable
            is SocketTimeoutException -> {
                Logger.d(TAG, "Socket timeout detected - retryable")
                true
            }
            
            // Network unavailable is retryable
            is UnknownHostException -> {
                Logger.d(TAG, "Unknown host detected - retryable")
                true
            }
            
            // General IO exceptions are retryable
            is IOException -> {
                Logger.d(TAG, "IO exception detected - retryable")
                true
            }
            
            // Check exception message for HTTP status codes
            else -> {
                val message = exception.message ?: ""
                when {
                    // HTTP 5xx server errors are retryable
                    message.contains("HTTP 5", ignoreCase = true) -> {
                        Logger.d(TAG, "HTTP 5xx error detected - retryable")
                        true
                    }
                    
                    // HTTP 429 Too Many Requests is retryable
                    message.contains("HTTP 429", ignoreCase = true) -> {
                        Logger.d(TAG, "HTTP 429 Too Many Requests - retryable")
                        true
                    }
                    
                    // HTTP 503 Service Unavailable is retryable
                    message.contains("HTTP 503", ignoreCase = true) -> {
                        Logger.d(TAG, "HTTP 503 Service Unavailable - retryable")
                        true
                    }
                    
                    // HTTP 4xx client errors (except 429) are NOT retryable
                    message.contains("HTTP 4", ignoreCase = true) -> {
                        Logger.d(TAG, "HTTP 4xx client error - not retryable")
                        false
                    }
                    
                    // Unknown errors are NOT retryable by default
                    else -> {
                        Logger.d(TAG, "Unknown error type - not retryable by default")
                        false
                    }
                }
            }
        }
    }
    
    /**
     * Configuration class for retry policy
     * 
     * @param maxRetries Maximum number of retry attempts
     * @param initialDelayMs Initial delay before first retry
     * @param maxDelayMs Maximum delay between retries
     * @param backoffMultiplier Exponential backoff multiplier
     */
    data class Config(
        val maxRetries: Int = DEFAULT_MAX_RETRIES,
        val initialDelayMs: Long = DEFAULT_INITIAL_DELAY_MS,
        val maxDelayMs: Long = DEFAULT_MAX_DELAY_MS,
        val backoffMultiplier: Double = DEFAULT_BACKOFF_MULTIPLIER
    ) {
        init {
            require(maxRetries >= 0) { "maxRetries must be >= 0" }
            require(initialDelayMs > 0) { "initialDelayMs must be > 0" }
            require(maxDelayMs >= initialDelayMs) { "maxDelayMs must be >= initialDelayMs" }
            require(backoffMultiplier >= 1.0) { "backoffMultiplier must be >= 1.0" }
        }
    }
}

