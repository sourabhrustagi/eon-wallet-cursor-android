package com.mobizonetech.aeon_wallet_cursor.data.remote.mapper

import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.ApiEndpointsDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.AppSettingsDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.FeatureFlagsDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.WelcomeScreenConfigDto
import com.mobizonetech.aeon_wallet_cursor.domain.model.ApiEndpoints
import com.mobizonetech.aeon_wallet_cursor.domain.model.AppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.model.FeatureFlags
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeScreenConfig

/**
 * Mapper to convert App Settings DTOs to domain models
 */
object AppSettingsMapper {
    
    /**
     * Convert AppSettingsDto to AppSettings domain model
     */
    fun mapToDomain(dto: AppSettingsDto): AppSettings {
        return AppSettings(
            appVersion = dto.appVersion,
            minimumAppVersion = dto.minimumAppVersion,
            forceUpdateRequired = dto.forceUpdateRequired,
            maintenanceMode = dto.maintenanceMode,
            welcomeScreenConfig = mapWelcomeScreenConfig(dto.welcomeScreenConfig),
            featureFlags = mapFeatureFlags(dto.featureFlags),
            apiEndpoints = mapApiEndpoints(dto.apiEndpoints)
        )
    }
    
    /**
     * Map welcome screen configuration
     */
    private fun mapWelcomeScreenConfig(dto: WelcomeScreenConfigDto): WelcomeScreenConfig {
        return WelcomeScreenConfig(
            autoAdvanceEnabled = dto.autoAdvanceEnabled,
            autoAdvanceDelayMs = dto.autoAdvanceDelayMs,
            showSkipButton = dto.showSkipButton,
            animationEnabled = dto.animationEnabled,
            analyticsEnabled = dto.analyticsEnabled
        )
    }
    
    /**
     * Map feature flags
     */
    private fun mapFeatureFlags(dto: FeatureFlagsDto): FeatureFlags {
        return FeatureFlags(
            cryptoTradingEnabled = dto.cryptoTradingEnabled,
            biometricAuthEnabled = dto.biometricAuthEnabled,
            socialLoginEnabled = dto.socialLoginEnabled,
            darkModeEnabled = dto.darkModeEnabled,
            notificationsEnabled = dto.notificationsEnabled
        )
    }
    
    /**
     * Map API endpoints
     */
    private fun mapApiEndpoints(dto: ApiEndpointsDto): ApiEndpoints {
        return ApiEndpoints(
            baseUrl = dto.baseUrl,
            supportUrl = dto.supportUrl,
            termsUrl = dto.termsUrl,
            privacyUrl = dto.privacyUrl
        )
    }
}

