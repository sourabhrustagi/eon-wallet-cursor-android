package com.mobizonetech.aeon_wallet_cursor.domain.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.AppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result

/**
 * Repository interface for App Settings
 * 
 * Defines contract for fetching app configuration
 */
interface AppSettingsRepository {
    
    /**
     * Get app settings and configuration
     * 
     * @return Result containing AppSettings or error
     */
    suspend fun getAppSettings(): Result<AppSettings>
}

