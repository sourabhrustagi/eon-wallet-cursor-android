package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.mobizonetech.aeon_wallet_cursor.domain.model.DefaultAppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.repository.AppSettingsRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GetAppSettingsUseCase
 */
class GetAppSettingsUseCaseTest {

    private lateinit var repository: AppSettingsRepository
    private lateinit var useCase: GetAppSettingsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAppSettingsUseCase(repository)
    }

    @Test
    fun `invoke returns Success from repository`() = runTest {
        // Given
        val mockSettings = DefaultAppSettings.default
        coEvery { repository.getAppSettings() } returns Result.Success(mockSettings)

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data).isEqualTo(mockSettings)
    }

    @Test
    fun `invoke returns Error from repository`() = runTest {
        // Given
        val errorMessage = "Failed to fetch settings"
        coEvery { repository.getAppSettings() } returns Result.Error(errorMessage)

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).isEqualTo(errorMessage)
    }

    @Test
    fun `invoke calls repository exactly once`() = runTest {
        // Given
        coEvery { repository.getAppSettings() } returns Result.Success(DefaultAppSettings.default)

        // When
        useCase()

        // Then
        coVerify(exactly = 1) { repository.getAppSettings() }
    }

    @Test
    fun `invoke propagates Loading state from repository`() = runTest {
        // Given
        coEvery { repository.getAppSettings() } returns Result.Loading

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Loading::class.java)
    }

    @Test
    fun `invoke handles exception from repository`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getAppSettings() } returns Result.Error(
            message = exception.message ?: "Unknown error",
            throwable = exception
        )

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Network error")
        assertThat(errorResult.throwable).isEqualTo(exception)
    }

    @Test
    fun `invoke returns complete app settings data`() = runTest {
        // Given
        val mockSettings = DefaultAppSettings.default
        coEvery { repository.getAppSettings() } returns Result.Success(mockSettings)

        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data.appVersion).isNotEmpty()
        assertThat(successResult.data.welcomeScreenConfig).isNotNull()
        assertThat(successResult.data.featureFlags).isNotNull()
        assertThat(successResult.data.apiEndpoints).isNotNull()
    }
}

