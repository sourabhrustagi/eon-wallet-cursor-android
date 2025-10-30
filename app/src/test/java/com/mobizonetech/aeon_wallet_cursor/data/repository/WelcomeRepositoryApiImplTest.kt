package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.google.common.truth.Truth.assertThat
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.WelcomeApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.WelcomeSlideDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.WelcomeSlidesResponse
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * Unit tests for WelcomeRepositoryApiImpl
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

