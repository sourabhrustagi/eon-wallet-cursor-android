package com.mobizonetech.aeon_wallet_cursor.data.remote.retry

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Comprehensive unit tests for RetryPolicy
 * 
 * Tests cover:
 * - Successful execution (no retry needed)
 * - Retry on retryable errors
 * - No retry on non-retryable errors
 * - Max retries exhausted
 * - Delay calculation with exponential backoff
 * - Retryable error detection
 * - Custom retry predicates
 * - Configuration validation
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RetryPolicyTest {

    // =============================================================================
    // Successful Execution Tests (No Retry Needed)
    // =============================================================================

    @Test
    fun `executeWithRetry succeeds on first attempt`() = runTest {
        // Given
        var attemptCount = 0
        val expectedResult = "Success"
        
        // When
        val result = RetryPolicy.executeWithRetry {
            attemptCount++
            expectedResult
        }
        
        // Then
        assertThat(result).isEqualTo(expectedResult)
        assertThat(attemptCount).isEqualTo(1)
    }

    @Test
    fun `executeWithRetry returns correct value on success`() = runTest {
        // Given
        val expectedValue = 42
        
        // When
        val result = RetryPolicy.executeWithRetry {
            expectedValue
        }
        
        // Then
        assertThat(result).isEqualTo(expectedValue)
    }

    @Test
    fun `executeWithRetry handles complex return types`() = runTest {
        // Given
        data class ComplexResult(val id: Int, val name: String)
        val expected = ComplexResult(1, "Test")
        
        // When
        val result = RetryPolicy.executeWithRetry {
            expected
        }
        
        // Then
        assertThat(result).isEqualTo(expected)
    }

    // =============================================================================
    // Retry on Retryable Errors Tests
    // =============================================================================

    @Test
    fun `executeWithRetry retries on SocketTimeoutException`() = runTest {
        // Given
        var attemptCount = 0
        
        // When
        val result = RetryPolicy.executeWithRetry(
            maxRetries = 2,
            initialDelayMs = 10L,
            maxDelayMs = 50L
        ) {
            attemptCount++
            if (attemptCount < 3) {
                throw SocketTimeoutException("Timeout")
            }
            "Success"
        }
        
        // Then
        assertThat(result).isEqualTo("Success")
        assertThat(attemptCount).isEqualTo(3) // Initial + 2 retries
    }

    @Test
    fun `executeWithRetry retries on UnknownHostException`() = runTest {
        // Given
        var attemptCount = 0
        
        // When
        val result = RetryPolicy.executeWithRetry(
            maxRetries = 1,
            initialDelayMs = 10L
        ) {
            attemptCount++
            if (attemptCount < 2) {
                throw UnknownHostException("Host not found")
            }
            "Success"
        }
        
        // Then
        assertThat(result).isEqualTo("Success")
        assertThat(attemptCount).isEqualTo(2)
    }

    @Test
    fun `executeWithRetry retries on IOException`() = runTest {
        // Given
        var attemptCount = 0
        
        // When
        val result = RetryPolicy.executeWithRetry(
            maxRetries = 2,
            initialDelayMs = 10L
        ) {
            attemptCount++
            if (attemptCount < 3) {
                throw IOException("Network error")
            }
            "Success"
        }
        
        // Then
        assertThat(result).isEqualTo("Success")
        assertThat(attemptCount).isEqualTo(3)
    }

    @Test
    fun `executeWithRetry retries on HTTP 5xx errors`() = runTest {
        // Given
        var attemptCount = 0
        
        // When
        val result = RetryPolicy.executeWithRetry(
            maxRetries = 1,
            initialDelayMs = 10L
        ) {
            attemptCount++
            if (attemptCount < 2) {
                throw RuntimeException("HTTP 500: Internal Server Error")
            }
            "Success"
        }
        
        // Then
        assertThat(result).isEqualTo("Success")
        assertThat(attemptCount).isEqualTo(2)
    }

    @Test
    fun `executeWithRetry retries on HTTP 503 Service Unavailable`() = runTest {
        // Given
        var attemptCount = 0
        
        // When
        val result = RetryPolicy.executeWithRetry(
            maxRetries = 1,
            initialDelayMs = 10L
        ) {
            attemptCount++
            if (attemptCount < 2) {
                throw RuntimeException("HTTP 503: Service Unavailable")
            }
            "Success"
        }
        
        // Then
        assertThat(result).isEqualTo("Success")
        assertThat(attemptCount).isEqualTo(2)
    }

    @Test
    fun `executeWithRetry retries on HTTP 429 Too Many Requests`() = runTest {
        // Given
        var attemptCount = 0
        
        // When
        val result = RetryPolicy.executeWithRetry(
            maxRetries = 1,
            initialDelayMs = 10L
        ) {
            attemptCount++
            if (attemptCount < 2) {
                throw RuntimeException("HTTP 429: Too Many Requests")
            }
            "Success"
        }
        
        // Then
        assertThat(result).isEqualTo("Success")
        assertThat(attemptCount).isEqualTo(2)
    }

    // =============================================================================
    // No Retry on Non-Retryable Errors Tests
    // =============================================================================

    @Test
    fun `executeWithRetry does not retry on HTTP 4xx client errors`() = runTest {
        // Given
        var attemptCount = 0
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = 3,
                initialDelayMs = 10L
            ) {
                attemptCount++
                throw RuntimeException("HTTP 400: Bad Request")
            }
            error("Should have thrown exception")
        } catch (e: Exception) {
            assertThat(e.message).contains("HTTP 400")
            assertThat(attemptCount).isEqualTo(1) // No retries
        }
    }

    @Test
    fun `executeWithRetry does not retry on HTTP 401 Unauthorized`() = runTest {
        // Given
        var attemptCount = 0
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = 3,
                initialDelayMs = 10L
            ) {
                attemptCount++
                throw RuntimeException("HTTP 401: Unauthorized")
            }
            error("Should have thrown exception")
        } catch (e: Exception) {
            assertThat(e.message).contains("HTTP 401")
            assertThat(attemptCount).isEqualTo(1)
        }
    }

    @Test
    fun `executeWithRetry does not retry on HTTP 404 Not Found`() = runTest {
        // Given
        var attemptCount = 0
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = 3,
                initialDelayMs = 10L
            ) {
                attemptCount++
                throw RuntimeException("HTTP 404: Not Found")
            }
            error("Should have thrown exception")
        } catch (e: Exception) {
            assertThat(e.message).contains("HTTP 404")
            assertThat(attemptCount).isEqualTo(1)
        }
    }

    @Test
    fun `executeWithRetry does not retry on unknown errors by default`() = runTest {
        // Given
        var attemptCount = 0
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = 3,
                initialDelayMs = 10L
            ) {
                attemptCount++
                throw IllegalArgumentException("Invalid argument")
            }
            error("Should have thrown exception")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
            assertThat(attemptCount).isEqualTo(1)
        }
    }

    // =============================================================================
    // Max Retries Exhausted Tests
    // =============================================================================

    @Test
    fun `executeWithRetry throws exception when max retries exhausted`() = runTest {
        // Given
        var attemptCount = 0
        val maxRetries = 2
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = maxRetries,
                initialDelayMs = 10L
            ) {
                attemptCount++
                throw SocketTimeoutException("Persistent timeout")
            }
            error("Should have thrown exception")
        } catch (e: SocketTimeoutException) {
            assertThat(e.message).isEqualTo("Persistent timeout")
            assertThat(attemptCount).isEqualTo(maxRetries + 1) // Initial + retries
        }
    }

    @Test
    fun `executeWithRetry respects maxRetries parameter`() = runTest {
        // Given
        var attemptCount = 0
        val maxRetries = 5
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = maxRetries,
                initialDelayMs = 5L
            ) {
                attemptCount++
                throw IOException("Persistent error")
            }
            error("Should have thrown exception")
        } catch (e: IOException) {
            assertThat(attemptCount).isEqualTo(maxRetries + 1)
        }
    }

    @Test
    fun `executeWithRetry with zero retries fails immediately`() = runTest {
        // Given
        var attemptCount = 0
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = 0,
                initialDelayMs = 10L
            ) {
                attemptCount++
                throw IOException("Error")
            }
            error("Should have thrown exception")
        } catch (e: IOException) {
            assertThat(attemptCount).isEqualTo(1)
        }
    }

    // =============================================================================
    // Delay Calculation Tests
    // =============================================================================

    @Test
    fun `calculateDelay returns correct value for first retry`() {
        // Given
        val attempt = 0
        val initialDelayMs = 1000L
        val maxDelayMs = 10000L
        val backoffMultiplier = 2.0
        
        // When
        val delay = RetryPolicy.calculateDelay(attempt, initialDelayMs, maxDelayMs, backoffMultiplier)
        
        // Then - Should be initialDelay + jitter (0-20%)
        assertThat(delay).isAtLeast(initialDelayMs)
        assertThat(delay).isAtMost((initialDelayMs * 1.2).toLong())
    }

    @Test
    fun `calculateDelay uses exponential backoff`() {
        // Given
        val initialDelayMs = 1000L
        val maxDelayMs = 100000L
        val backoffMultiplier = 2.0
        
        // When
        val delay1 = RetryPolicy.calculateDelay(0, initialDelayMs, maxDelayMs, backoffMultiplier)
        val delay2 = RetryPolicy.calculateDelay(1, initialDelayMs, maxDelayMs, backoffMultiplier)
        val delay3 = RetryPolicy.calculateDelay(2, initialDelayMs, maxDelayMs, backoffMultiplier)
        
        // Then - Each delay should be roughly double the previous (with jitter)
        assertThat(delay2).isGreaterThan(delay1)
        assertThat(delay3).isGreaterThan(delay2)
    }

    @Test
    fun `calculateDelay respects max delay cap`() {
        // Given
        val attempt = 10
        val initialDelayMs = 1000L
        val maxDelayMs = 5000L
        val backoffMultiplier = 2.0
        
        // When
        val delay = RetryPolicy.calculateDelay(attempt, initialDelayMs, maxDelayMs, backoffMultiplier)
        
        // Then - Should not exceed maxDelay + 20% jitter
        assertThat(delay).isAtMost((maxDelayMs * 1.2).toLong())
    }

    @Test
    fun `calculateDelay adds jitter`() {
        // Given
        val attempt = 0
        val initialDelayMs = 1000L
        val maxDelayMs = 10000L
        val backoffMultiplier = 2.0
        
        // When - Calculate multiple times
        val delays = (0..100).map {
            RetryPolicy.calculateDelay(attempt, initialDelayMs, maxDelayMs, backoffMultiplier)
        }
        
        // Then - Should have variation (jitter)
        val uniqueDelays = delays.toSet()
        assertThat(uniqueDelays.size).isGreaterThan(1)
    }

    // =============================================================================
    // Retryable Error Detection Tests
    // =============================================================================

    @Test
    fun `isRetryableError returns true for SocketTimeoutException`() {
        // Given
        val exception = SocketTimeoutException("Timeout")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isTrue()
    }

    @Test
    fun `isRetryableError returns true for UnknownHostException`() {
        // Given
        val exception = UnknownHostException("Unknown host")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isTrue()
    }

    @Test
    fun `isRetryableError returns true for IOException`() {
        // Given
        val exception = IOException("IO Error")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isTrue()
    }

    @Test
    fun `isRetryableError returns true for HTTP 500 errors`() {
        // Given
        val exception = RuntimeException("HTTP 500: Internal Server Error")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isTrue()
    }

    @Test
    fun `isRetryableError returns true for HTTP 503 errors`() {
        // Given
        val exception = RuntimeException("HTTP 503: Service Unavailable")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isTrue()
    }

    @Test
    fun `isRetryableError returns true for HTTP 429 errors`() {
        // Given
        val exception = RuntimeException("HTTP 429: Too Many Requests")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isTrue()
    }

    @Test
    fun `isRetryableError returns false for HTTP 400 errors`() {
        // Given
        val exception = RuntimeException("HTTP 400: Bad Request")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isFalse()
    }

    @Test
    fun `isRetryableError returns false for HTTP 404 errors`() {
        // Given
        val exception = RuntimeException("HTTP 404: Not Found")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isFalse()
    }

    @Test
    fun `isRetryableError returns false for unknown exceptions`() {
        // Given
        val exception = IllegalArgumentException("Invalid argument")
        
        // When
        val isRetryable = RetryPolicy.isRetryableError(exception)
        
        // Then
        assertThat(isRetryable).isFalse()
    }

    // =============================================================================
    // Custom Retry Predicate Tests
    // =============================================================================

    @Test
    fun `executeWithRetry uses custom shouldRetry predicate`() = runTest {
        // Given
        var attemptCount = 0
        val customPredicate: (Exception) -> Boolean = { true } // Always retry
        
        // When
        val result = RetryPolicy.executeWithRetry(
            maxRetries = 2,
            initialDelayMs = 10L,
            shouldRetry = customPredicate
        ) {
            attemptCount++
            if (attemptCount < 3) {
                throw IllegalArgumentException("Custom error")
            }
            "Success"
        }
        
        // Then
        assertThat(result).isEqualTo("Success")
        assertThat(attemptCount).isEqualTo(3)
    }

    @Test
    fun `executeWithRetry respects custom non-retryable predicate`() = runTest {
        // Given
        var attemptCount = 0
        val customPredicate: (Exception) -> Boolean = { false } // Never retry
        
        // When/Then
        try {
            RetryPolicy.executeWithRetry(
                maxRetries = 3,
                initialDelayMs = 10L,
                shouldRetry = customPredicate
            ) {
                attemptCount++
                throw IOException("Error")
            }
            error("Should have thrown exception")
        } catch (e: IOException) {
            assertThat(attemptCount).isEqualTo(1)
        }
    }

    // =============================================================================
    // Configuration Tests
    // =============================================================================

    @Test
    fun `Config validates maxRetries is non-negative`() {
        // When/Then
        try {
            RetryPolicy.Config(maxRetries = -1)
            error("Should have thrown exception")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).contains("maxRetries must be >= 0")
        }
    }

    @Test
    fun `Config validates initialDelayMs is positive`() {
        // When/Then
        try {
            RetryPolicy.Config(initialDelayMs = 0)
            error("Should have thrown exception")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).contains("initialDelayMs must be > 0")
        }
    }

    @Test
    fun `Config validates maxDelayMs is greater than initialDelayMs`() {
        // When/Then
        try {
            RetryPolicy.Config(initialDelayMs = 1000L, maxDelayMs = 500L)
            error("Should have thrown exception")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).contains("maxDelayMs must be >= initialDelayMs")
        }
    }

    @Test
    fun `Config validates backoffMultiplier is at least 1_0`() {
        // When/Then
        try {
            RetryPolicy.Config(backoffMultiplier = 0.5)
            error("Should have thrown exception")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).contains("backoffMultiplier must be >= 1.0")
        }
    }

    @Test
    fun `Config creates valid instance with default values`() {
        // When
        val config = RetryPolicy.Config()
        
        // Then
        assertThat(config.maxRetries).isEqualTo(RetryPolicy.DEFAULT_MAX_RETRIES)
        assertThat(config.initialDelayMs).isEqualTo(RetryPolicy.DEFAULT_INITIAL_DELAY_MS)
        assertThat(config.maxDelayMs).isEqualTo(RetryPolicy.DEFAULT_MAX_DELAY_MS)
        assertThat(config.backoffMultiplier).isEqualTo(RetryPolicy.DEFAULT_BACKOFF_MULTIPLIER)
    }

    @Test
    fun `Config creates valid instance with custom values`() {
        // When
        val config = RetryPolicy.Config(
            maxRetries = 5,
            initialDelayMs = 2000L,
            maxDelayMs = 20000L,
            backoffMultiplier = 3.0
        )
        
        // Then
        assertThat(config.maxRetries).isEqualTo(5)
        assertThat(config.initialDelayMs).isEqualTo(2000L)
        assertThat(config.maxDelayMs).isEqualTo(20000L)
        assertThat(config.backoffMultiplier).isEqualTo(3.0)
    }
}

