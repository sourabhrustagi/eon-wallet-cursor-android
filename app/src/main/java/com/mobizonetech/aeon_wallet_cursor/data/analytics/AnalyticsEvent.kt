package com.mobizonetech.aeon_wallet_cursor.data.analytics

/**
 * Analytics Event representation
 * 
 * Represents a trackable event with name and parameters
 */
data class AnalyticsEvent(
    val name: String,
    val parameters: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        // Welcome Screen Events
        const val WELCOME_SCREEN_VIEWED = "welcome_screen_viewed"
        const val WELCOME_SLIDE_CHANGED = "welcome_slide_changed"
        const val WELCOME_NEXT_CLICKED = "welcome_next_clicked"
        const val WELCOME_SKIP_CLICKED = "welcome_skip_clicked"
        const val WELCOME_GET_STARTED_CLICKED = "welcome_get_started_clicked"
        const val WELCOME_SIGN_IN_CLICKED = "welcome_sign_in_clicked"
        const val WELCOME_COMPLETED = "welcome_completed"
        
        // App Lifecycle Events
        const val APP_OPENED = "app_opened"
        const val APP_BACKGROUNDED = "app_backgrounded"
        const val APP_FOREGROUNDED = "app_foregrounded"
        
        // Error Events
        const val ERROR_OCCURRED = "error_occurred"
        const val API_ERROR = "api_error"
        const val NETWORK_ERROR = "network_error"
        
        // Settings Events
        const val SETTINGS_LOADED = "settings_loaded"
        const val FEATURE_FLAG_EVALUATED = "feature_flag_evaluated"
        
        // Login Events
        const val LOGIN_ATTEMPTED = "login_attempted"
        const val LOGIN_SUCCESS = "login_success"
        const val LOGIN_FAILED = "login_failed"
        
        // Parameter Keys
        const val PARAM_SCREEN_NAME = "screen_name"
        const val PARAM_SLIDE_INDEX = "slide_index"
        const val PARAM_SLIDE_TITLE = "slide_title"
        const val PARAM_TOTAL_SLIDES = "total_slides"
        const val PARAM_TIME_SPENT = "time_spent_ms"
        const val PARAM_ERROR_MESSAGE = "error_message"
        const val PARAM_ERROR_CODE = "error_code"
        const val PARAM_API_ENDPOINT = "api_endpoint"
        const val PARAM_FEATURE_NAME = "feature_name"
        const val PARAM_FEATURE_ENABLED = "feature_enabled"
        const val PARAM_SOURCE = "source"
    }
}

/**
 * User Properties for analytics
 */
data class UserProperty(
    val key: String,
    val value: String
) {
    companion object {
        const val USER_ID = "user_id"
        const val APP_VERSION = "app_version"
        const val DEVICE_MODEL = "device_model"
        const val OS_VERSION = "os_version"
        const val FIRST_OPEN_TIME = "first_open_time"
        const val ONBOARDING_COMPLETED = "onboarding_completed"
    }
}

