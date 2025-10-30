package com.mobizonetech.aeon_wallet_cursor.data.analytics

/**
 * Helper functions for common analytics tracking patterns
 */
object AnalyticsHelper {
    
    /**
     * Track screen view event
     */
    fun Analytics.trackScreenView(screenName: String) {
        trackEvent(
            AnalyticsEvent.WELCOME_SCREEN_VIEWED,
            mapOf(AnalyticsEvent.PARAM_SCREEN_NAME to screenName)
        )
    }
    
    /**
     * Track slide change event
     */
    fun Analytics.trackSlideChange(
        slideIndex: Int,
        slideTitle: String,
        totalSlides: Int
    ) {
        trackEvent(
            AnalyticsEvent.WELCOME_SLIDE_CHANGED,
            mapOf(
                AnalyticsEvent.PARAM_SLIDE_INDEX to slideIndex,
                AnalyticsEvent.PARAM_SLIDE_TITLE to slideTitle,
                AnalyticsEvent.PARAM_TOTAL_SLIDES to totalSlides
            )
        )
    }
    
    /**
     * Track button click event
     */
    fun Analytics.trackButtonClick(
        buttonName: String,
        screenName: String,
        additionalParams: Map<String, Any> = emptyMap()
    ) {
        val params = mutableMapOf<String, Any>(
            "button_name" to buttonName,
            AnalyticsEvent.PARAM_SCREEN_NAME to screenName
        )
        params.putAll(additionalParams)
        
        trackEvent("button_clicked", params)
    }
    
    /**
     * Track error event
     */
    fun Analytics.trackError(
        errorMessage: String,
        errorCode: String? = null,
        source: String? = null
    ) {
        val params = mutableMapOf<String, Any>(
            AnalyticsEvent.PARAM_ERROR_MESSAGE to errorMessage
        )
        
        errorCode?.let { params[AnalyticsEvent.PARAM_ERROR_CODE] = it }
        source?.let { params[AnalyticsEvent.PARAM_SOURCE] = it }
        
        trackEvent(AnalyticsEvent.ERROR_OCCURRED, params)
    }
    
    /**
     * Track API call event
     */
    fun Analytics.trackApiCall(
        endpoint: String,
        success: Boolean,
        duration: Long? = null,
        errorMessage: String? = null
    ) {
        val params = mutableMapOf<String, Any>(
            AnalyticsEvent.PARAM_API_ENDPOINT to endpoint,
            "success" to success
        )
        
        duration?.let { params["duration_ms"] = it }
        errorMessage?.let { params[AnalyticsEvent.PARAM_ERROR_MESSAGE] = it }
        
        trackEvent(if (success) "api_success" else AnalyticsEvent.API_ERROR, params)
    }
    
    /**
     * Track feature flag evaluation
     */
    fun Analytics.trackFeatureFlag(
        featureName: String,
        enabled: Boolean
    ) {
        trackEvent(
            AnalyticsEvent.FEATURE_FLAG_EVALUATED,
            mapOf(
                AnalyticsEvent.PARAM_FEATURE_NAME to featureName,
                AnalyticsEvent.PARAM_FEATURE_ENABLED to enabled
            )
        )
    }
    
    /**
     * Track time spent on screen
     */
    fun Analytics.trackTimeSpent(
        screenName: String,
        timeSpentMs: Long
    ) {
        trackEvent(
            "time_spent",
            mapOf(
                AnalyticsEvent.PARAM_SCREEN_NAME to screenName,
                AnalyticsEvent.PARAM_TIME_SPENT to timeSpentMs
            )
        )
    }
}

