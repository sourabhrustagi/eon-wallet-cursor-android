package com.mobizonetech.aeon_wallet_cursor.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for App Settings from API
 * 
 * Contains configuration and feature flags for the app
 */
data class AppSettingsDto(
    @SerializedName("app_version")
    val appVersion: String,
    
    @SerializedName("minimum_app_version")
    val minimumAppVersion: String,
    
    @SerializedName("force_update_required")
    val forceUpdateRequired: Boolean,
    
    @SerializedName("maintenance_mode")
    val maintenanceMode: Boolean,
    
    @SerializedName("welcome_screen_config")
    val welcomeScreenConfig: WelcomeScreenConfigDto,
    
    @SerializedName("feature_flags")
    val featureFlags: FeatureFlagsDto,
    
    @SerializedName("api_endpoints")
    val apiEndpoints: ApiEndpointsDto
)

/**
 * Welcome screen specific configuration
 */
data class WelcomeScreenConfigDto(
    @SerializedName("auto_advance_enabled")
    val autoAdvanceEnabled: Boolean,
    
    @SerializedName("auto_advance_delay_ms")
    val autoAdvanceDelayMs: Long,
    
    @SerializedName("show_skip_button")
    val showSkipButton: Boolean,
    
    @SerializedName("animation_enabled")
    val animationEnabled: Boolean,
    
    @SerializedName("analytics_enabled")
    val analyticsEnabled: Boolean
)

/**
 * Feature flags for enabling/disabling features
 */
data class FeatureFlagsDto(
    @SerializedName("crypto_trading_enabled")
    val cryptoTradingEnabled: Boolean,
    
    @SerializedName("biometric_auth_enabled")
    val biometricAuthEnabled: Boolean,
    
    @SerializedName("social_login_enabled")
    val socialLoginEnabled: Boolean,
    
    @SerializedName("dark_mode_enabled")
    val darkModeEnabled: Boolean,
    
    @SerializedName("notifications_enabled")
    val notificationsEnabled: Boolean
)

/**
 * API endpoints configuration
 */
data class ApiEndpointsDto(
    @SerializedName("base_url")
    val baseUrl: String,
    
    @SerializedName("support_url")
    val supportUrl: String,
    
    @SerializedName("terms_url")
    val termsUrl: String,
    
    @SerializedName("privacy_url")
    val privacyUrl: String
)

/**
 * API Response wrapper
 */
data class AppSettingsResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: AppSettingsDto,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("timestamp")
    val timestamp: Long
)

