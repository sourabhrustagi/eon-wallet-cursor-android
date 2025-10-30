package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.data.remote.api.AppSettingsApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.DtoValidator
import com.mobizonetech.aeon_wallet_cursor.data.remote.mapper.AppSettingsMapper
import com.mobizonetech.aeon_wallet_cursor.data.remote.retry.RetryPolicy
import com.mobizonetech.aeon_wallet_cursor.domain.model.AppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.repository.AppSettingsRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import com.mobizonetech.aeon_wallet_cursor.util.PerformanceMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * API-based implementation of AppSettingsRepository
 * Fetches app settings from remote API with automatic retry on failure
 * 
 * Features:
 * - Automatic retry on network errors
 * - Exponential backoff between retries
 * - Configurable retry attempts (default: 3)
 * 
 * @param apiService Retrofit API service
 */
class AppSettingsRepositoryImpl @Inject constructor(
    private val apiService: AppSettingsApiService
) : AppSettingsRepository {

    override suspend fun getAppSettings(): Result<AppSettings> = withContext(Dispatchers.IO) {
        try {
            Logger.d(TAG, "Fetching app settings from API (with retry)")
            
            // Execute API call with retry logic
            val response = RetryPolicy.executeWithRetry(
                maxRetries = 3,
                initialDelayMs = 1000L,
                maxDelayMs = 5000L
            ) {
                PerformanceMonitor.measure("getAppSettings API") {
                    apiService.getAppSettings()
                }
            }
            
            if (response.isSuccessful) {
                val body = response.body()
                
                if (body != null && body.success) {
                    // Validate response data
                    DtoValidator.validateAppSettingsResponse(body)
                    
                    val domainSettings = AppSettingsMapper.mapToDomain(body.data)
                    Logger.d(TAG, "Successfully fetched app settings")
                    Logger.d(TAG, "App version: ${domainSettings.appVersion}")
                    Logger.d(TAG, "Maintenance mode: ${domainSettings.maintenanceMode}")
                    Logger.d(TAG, "Auto-advance enabled: ${domainSettings.welcomeScreenConfig.autoAdvanceEnabled}")
                    Result.Success(domainSettings)
                } else {
                    val errorMessage = body?.message ?: "API returned unsuccessful response"
                    Logger.e(TAG, "API error: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Logger.e(TAG, "API request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Error fetching app settings from API after retries", e)
            Result.Error(
                message = e.message ?: "Unknown error occurred while loading app settings",
                throwable = e
            )
        }
    }

    companion object {
        private const val TAG = "AppSettingsRepository"
    }
}

