package com.mobizonetech.aeon_wallet_cursor.domain.model

import androidx.compose.runtime.Immutable

/**
 * Domain model for App Settings
 * 
 * Contains app configuration and feature flags
 */
@Immutable
data class AppSettings(
    val appVersion: String,
    val minimumAppVersion: String,
    val forceUpdateRequired: Boolean,
    val maintenanceMode: Boolean,
    val welcomeScreenConfig: WelcomeScreenConfig,
    val featureFlags: FeatureFlags,
    val apiEndpoints: ApiEndpoints
)

/**
 * Welcome screen configuration
 */
@Immutable
data class WelcomeScreenConfig(
    val autoAdvanceEnabled: Boolean = false,
    val autoAdvanceDelayMs: Long = 3000L,
    val showSkipButton: Boolean = true,
    val animationEnabled: Boolean = true,
    val analyticsEnabled: Boolean = false
)

/**
 * Feature flags for the app
 */
@Immutable
data class FeatureFlags(
    val cryptoTradingEnabled: Boolean = true,
    val biometricAuthEnabled: Boolean = true,
    val socialLoginEnabled: Boolean = false,
    val darkModeEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true
)

/**
 * API endpoints configuration
 */
@Immutable
data class ApiEndpoints(
    val baseUrl: String,
    val supportUrl: String,
    val termsUrl: String,
    val privacyUrl: String
)

/**
 * Default app settings for fallback
 */
object DefaultAppSettings {
    val default = AppSettings(
        appVersion = "1.0.0",
        minimumAppVersion = "1.0.0",
        forceUpdateRequired = false,
        maintenanceMode = false,
        welcomeScreenConfig = WelcomeScreenConfig(),
        featureFlags = FeatureFlags(),
        apiEndpoints = ApiEndpoints(
            baseUrl = "https://api.aeonwallet.com/",
            supportUrl = "https://support.aeonwallet.com/",
            termsUrl = "https://aeonwallet.com/terms",
            privacyUrl = "https://aeonwallet.com/privacy"
        )
    )
}

