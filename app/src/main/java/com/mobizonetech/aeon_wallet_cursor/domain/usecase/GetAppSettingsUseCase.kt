package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import com.mobizonetech.aeon_wallet_cursor.domain.model.AppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.repository.AppSettingsRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import javax.inject.Inject

/**
 * Use case for fetching app settings
 * 
 * Encapsulates business logic for retrieving app configuration
 * 
 * @param repository Repository for app settings data
 */
class GetAppSettingsUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    /**
     * Execute the use case
     * 
     * @return Result containing AppSettings or error
     */
    suspend operator fun invoke(): Result<AppSettings> {
        Logger.d(TAG, "Executing GetAppSettingsUseCase")
        return repository.getAppSettings()
    }

    companion object {
        private const val TAG = "GetAppSettingsUseCase"
    }
}

