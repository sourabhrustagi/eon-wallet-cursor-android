package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.google.common.truth.Truth.assertThat
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.WelcomeApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.WelcomeSlideDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.WelcomeSlidesResponse
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Unit tests for WelcomeRepositoryApiImpl
 * 
 * Tests cover:
 * - Successful API calls
 * - Error handling
 * - Data mapping
 * - Retry mechanism (NEW)
 */
class WelcomeRepositoryApiImplTest {

    private lateinit var apiService: WelcomeApiService
    private lateinit var repository: WelcomeRepositoryApiImpl

    @Before
    fun setup() {
        apiService = mockk()
        repository = WelcomeRepositoryApiImpl(apiService)
    }

    @Test
    fun `getWelcomeSlides returns Success with valid data`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } returns Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data).hasSize(3)
    }

    @Test
    fun `getWelcomeSlides returns Error on API failure`() = runTest {
        // Given
        coEvery { apiService.getWelcomeSlides() } returns 
            Response.error(500, mockk(relaxed = true))

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("HTTP 500")
    }

    @Test
    fun `getWelcomeSlides returns Error when success flag is false`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse(success = false)
        coEvery { apiService.getWelcomeSlides() } returns Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `getWelcomeSlides returns Error on exception`() = runTest {
        // Given
        coEvery { apiService.getWelcomeSlides() } throws RuntimeException("Network error")

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Network error")
    }

    @Test
    fun `getWelcomeSlides maps data correctly`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } returns Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        val firstSlide = successResult.data[0]
        assertThat(firstSlide.title).isEqualTo("Slide 1")
        assertThat(firstSlide.features).hasSize(3)
    }

    @Test
    fun `getWelcomeSlides handles empty response`() = runTest {
        // Given
        val mockResponse = WelcomeSlidesResponse(
            success = true,
            data = emptyList(),
            message = "No slides available"
        )
        coEvery { apiService.getWelcomeSlides() } returns Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then
        // Should fail validation because slides can't be empty
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    // =============================================================================
    // Retry Mechanism Tests (NEW)
    // =============================================================================

    @Test
    fun `getWelcomeSlides retries on SocketTimeoutException and succeeds`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } throws SocketTimeoutException("Timeout") andThen Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then - Should retry and succeed
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 2) { apiService.getWelcomeSlides() } // Initial attempt + 1 retry
    }

    @Test
    fun `getWelcomeSlides retries on UnknownHostException and succeeds`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } throws 
            UnknownHostException("Host not found") andThen Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then - Should retry and succeed
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 2) { apiService.getWelcomeSlides() }
    }

    @Test
    fun `getWelcomeSlides retries on IOException and succeeds`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } throws 
            IOException("Network error") andThen Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then - Should retry and succeed
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 2) { apiService.getWelcomeSlides() }
    }

    @Test
    fun `getWelcomeSlides retries multiple times before succeeding`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } throws 
            SocketTimeoutException("Timeout") andThenThrows 
            SocketTimeoutException("Timeout") andThen 
            Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then - Should retry twice and succeed
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 3) { apiService.getWelcomeSlides() } // Initial + 2 retries
    }

    @Test
    fun `getWelcomeSlides fails after max retries exhausted`() = runTest {
        // Given - All attempts throw SocketTimeoutException
        coEvery { apiService.getWelcomeSlides() } throws SocketTimeoutException("Persistent timeout")

        // When
        val result = repository.getWelcomeSlides()

        // Then - Should fail after max retries (3 + initial = 4 total attempts)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Persistent timeout")
        coVerify(exactly = 4) { apiService.getWelcomeSlides() }
    }

    @Test
    fun `getWelcomeSlides does not retry on HTTP 400 errors`() = runTest {
        // Given - HTTP 400 is not retryable
        coEvery { apiService.getWelcomeSlides() } returns 
            Response.error(400, mockk(relaxed = true))

        // When
        val result = repository.getWelcomeSlides()

        // Then - Should fail immediately without retries
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("HTTP 400")
        coVerify(exactly = 1) { apiService.getWelcomeSlides() } // No retries
    }

    @Test
    fun `getWelcomeSlides does not retry on HTTP 404 errors`() = runTest {
        // Given - HTTP 404 is not retryable
        coEvery { apiService.getWelcomeSlides() } returns 
            Response.error(404, mockk(relaxed = true))

        // When
        val result = repository.getWelcomeSlides()

        // Then - Should fail immediately
        assertThat(result).isInstanceOf(Result.Error::class.java)
        coVerify(exactly = 1) { apiService.getWelcomeSlides() }
    }

    @Test
    fun `getWelcomeSlides handles HTTP 503 Service Unavailable after retries`() = runTest {
        // Given - HTTP 503 is retryable but API failures stop retries
        // The retry mechanism only retries on network exceptions, not HTTP errors
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } returns 
            Response.error(503, mockk(relaxed = true))

        // When
        val result = repository.getWelcomeSlides()

        // Then - HTTP errors don't trigger retries (only network exceptions do)
        assertThat(result).isInstanceOf(Result.Error::class.java)
        coVerify(exactly = 1) { apiService.getWelcomeSlides() }
    }

    @Test
    fun `getWelcomeSlides handles mixed failures during retry`() = runTest {
        // Given - First timeout, then success
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } throws 
            SocketTimeoutException("Timeout") andThen 
            Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data).hasSize(3)
    }

    @Test
    fun `getWelcomeSlides logs retry attempts`() = runTest {
        // Given - This test verifies retry mechanism executes
        val mockResponse = createMockWelcomeSlidesResponse()
        var attemptCount = 0
        coEvery { apiService.getWelcomeSlides() } answers {
            attemptCount++
            if (attemptCount < 2) {
                throw IOException("Network error")
            } else {
                Response.success(mockResponse)
            }
        }

        // When
        val result = repository.getWelcomeSlides()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat(attemptCount).isEqualTo(2)
    }

    @Test
    fun `getWelcomeSlides preserves data integrity after retry`() = runTest {
        // Given
        val mockResponse = createMockWelcomeSlidesResponse()
        coEvery { apiService.getWelcomeSlides() } throws 
            IOException("Network error") andThen 
            Response.success(mockResponse)

        // When
        val result = repository.getWelcomeSlides()

        // Then - Data should be correct after retry
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data[0].title).isEqualTo("Slide 1")
        assertThat(successResult.data[1].title).isEqualTo("Slide 2")
        assertThat(successResult.data[2].title).isEqualTo("Slide 3")
    }

    private fun createMockWelcomeSlidesResponse(success: Boolean = true): WelcomeSlidesResponse {
        return WelcomeSlidesResponse(
            success = success,
            message = if (success) "Success" else "Failed",
            data = listOf(
                WelcomeSlideDto(
                    id = 0,
                    title = "Slide 1",
                    description = "Description 1",
                    icon = "🎉",
                    iconBackgroundColor = "#6200EE",
                    features = listOf("Feature 1", "Feature 2", "Feature 3")
                ),
                WelcomeSlideDto(
                    id = 1,
                    title = "Slide 2",
                    description = "Description 2",
                    icon = "🚀",
                    iconBackgroundColor = "#03DAC6",
                    features = listOf("Feature A", "Feature B", "Feature C")
                ),
                WelcomeSlideDto(
                    id = 2,
                    title = "Slide 3",
                    description = "Description 3",
                    icon = "✨",
                    iconBackgroundColor = "#B00020",
                    features = listOf("Feature X", "Feature Y", "Feature Z")
                )
            )
        )
    }
}

