package com.mobizonetech.aeon_wallet_cursor.data.remote.api

import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.AppSettingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Service for App Settings and Configuration endpoints
 */
interface AppSettingsApiService {
    
    /**
     * Fetch app settings and configuration
     * 
     * @param appVersion Current app version
     * @param platform Platform identifier (android/ios)
     * @return Response containing app settings
     */
    @GET("api/v1/config/settings")
    suspend fun getAppSettings(
        @Query("app_version") appVersion: String = "1.0.0",
        @Query("platform") platform: String = "android"
    ): Response<AppSettingsResponse>
}

