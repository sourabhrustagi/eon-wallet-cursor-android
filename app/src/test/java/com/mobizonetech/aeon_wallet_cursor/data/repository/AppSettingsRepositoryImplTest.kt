package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.google.common.truth.Truth.assertThat
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.AppSettingsApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.*
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
 * Unit tests for AppSettingsRepositoryImpl
 * 
 * Tests cover:
 * - Successful API calls
 * - Error handling
 * - Data mapping
 * - Retry mechanism (NEW)
 */
class AppSettingsRepositoryImplTest {

    private lateinit var apiService: AppSettingsApiService
    private lateinit var repository: AppSettingsRepositoryImpl

    @Before
    fun setup() {
        apiService = mockk()
        repository = AppSettingsRepositoryImpl(apiService)
    }

    @Test
    fun `getAppSettings returns Success with valid data`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } returns Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data.appVersion).isEqualTo("1.0.0")
        assertThat(successResult.data.maintenanceMode).isFalse()
    }

    @Test
    fun `getAppSettings returns Error on API failure`() = runTest {
        // Given
        coEvery { apiService.getAppSettings() } returns 
            Response.error(500, mockk(relaxed = true))

        // When
        val result = repository.getAppSettings()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("HTTP 500")
    }

    @Test
    fun `getAppSettings returns Error when success flag is false`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse(success = false)
        coEvery { apiService.getAppSettings() } returns Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `getAppSettings returns Error on exception`() = runTest {
        // Given
        coEvery { apiService.getAppSettings() } throws 
            RuntimeException("Network error")

        // When
        val result = repository.getAppSettings()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Network error")
    }

    @Test
    fun `getAppSettings maps welcome screen config correctly`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } returns Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        val config = successResult.data.welcomeScreenConfig
        assertThat(config.autoAdvanceEnabled).isTrue()
        assertThat(config.autoAdvanceDelayMs).isEqualTo(5000L)
        assertThat(config.showSkipButton).isTrue()
    }

    @Test
    fun `getAppSettings maps feature flags correctly`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } returns Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        val flags = successResult.data.featureFlags
        assertThat(flags.cryptoTradingEnabled).isTrue()
        assertThat(flags.biometricAuthEnabled).isTrue()
        assertThat(flags.socialLoginEnabled).isFalse()
    }

    @Test
    fun `getAppSettings maps API endpoints correctly`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } returns Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        val endpoints = successResult.data.apiEndpoints
        assertThat(endpoints.baseUrl).isEqualTo("https://api.aeonwallet.com/")
        assertThat(endpoints.supportUrl).isEqualTo("https://support.aeonwallet.com/")
    }

    // =============================================================================
    // Retry Mechanism Tests (NEW)
    // =============================================================================

    @Test
    fun `getAppSettings retries on SocketTimeoutException and succeeds`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } throws 
            SocketTimeoutException("Timeout") andThen Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then - Should retry and succeed
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 2) { apiService.getAppSettings() } // Initial + 1 retry
    }

    @Test
    fun `getAppSettings retries on UnknownHostException and succeeds`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } throws 
            UnknownHostException("Host not found") andThen Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then - Should retry and succeed
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 2) { apiService.getAppSettings() }
    }

    @Test
    fun `getAppSettings retries on IOException and succeeds`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } throws 
            IOException("Network error") andThen Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then - Should retry and succeed
        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(exactly = 2) { apiService.getAppSettings() }
    }

    @Test
    fun `getAppSettings fails after max retries exhausted`() = runTest {
        // Given - All attempts throw SocketTimeoutException
        coEvery { apiService.getAppSettings() } throws SocketTimeoutException("Persistent timeout")

        // When
        val result = repository.getAppSettings()

        // Then - Should fail after max retries
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Persistent timeout")
        coVerify(exactly = 4) { apiService.getAppSettings() } // Initial + 3 retries
    }

    @Test
    fun `getAppSettings does not retry on HTTP 400 errors`() = runTest {
        // Given - HTTP 400 is not retryable
        coEvery { apiService.getAppSettings() } returns 
            Response.error(400, mockk(relaxed = true))

        // When
        val result = repository.getAppSettings()

        // Then - Should fail immediately
        assertThat(result).isInstanceOf(Result.Error::class.java)
        coVerify(exactly = 1) { apiService.getAppSettings() } // No retries
    }

    @Test
    fun `getAppSettings preserves settings integrity after retry`() = runTest {
        // Given
        val mockResponse = createMockAppSettingsResponse()
        coEvery { apiService.getAppSettings() } throws 
            IOException("Network error") andThen 
            Response.success(mockResponse)

        // When
        val result = repository.getAppSettings()

        // Then - Data should be correct after retry
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data.appVersion).isEqualTo("1.0.0")
        assertThat(successResult.data.maintenanceMode).isFalse()
        assertThat(successResult.data.welcomeScreenConfig.autoAdvanceEnabled).isTrue()
    }

    private fun createMockAppSettingsResponse(success: Boolean = true): AppSettingsResponse {
        return AppSettingsResponse(
            success = success,
            message = if (success) "Success" else "Failed",
            timestamp = System.currentTimeMillis(),
            data = AppSettingsDto(
                appVersion = "1.0.0",
                minimumAppVersion = "1.0.0",
                forceUpdateRequired = false,
                maintenanceMode = false,
                welcomeScreenConfig = WelcomeScreenConfigDto(
                    autoAdvanceEnabled = true,
                    autoAdvanceDelayMs = 5000L,
                    showSkipButton = true,
                    animationEnabled = true,
                    analyticsEnabled = true
                ),
                featureFlags = FeatureFlagsDto(
                    cryptoTradingEnabled = true,
                    biometricAuthEnabled = true,
                    socialLoginEnabled = false,
                    darkModeEnabled = true,
                    notificationsEnabled = true
                ),
                apiEndpoints = ApiEndpointsDto(
                    baseUrl = "https://api.aeonwallet.com/",
                    supportUrl = "https://support.aeonwallet.com/",
                    termsUrl = "https://aeonwallet.com/terms",
                    privacyUrl = "https://aeonwallet.com/privacy"
                )
            )
        )
    }
}

