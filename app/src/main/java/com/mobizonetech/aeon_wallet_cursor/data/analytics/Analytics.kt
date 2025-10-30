package com.mobizonetech.aeon_wallet_cursor.data.analytics

/**
 * Analytics interface for tracking events and user properties
 * 
 * Provides abstraction layer for analytics implementations
 * Can be swapped with Firebase Analytics, Mixpanel, etc.
 */
interface Analytics {
    
    /**
     * Track an analytics event
     * 
     * @param event The event to track
     */
    fun trackEvent(event: AnalyticsEvent)
    
    /**
     * Track an event with name and parameters
     * 
     * @param eventName Name of the event
     * @param parameters Event parameters
     */
    fun trackEvent(eventName: String, parameters: Map<String, Any> = emptyMap()) {
        trackEvent(AnalyticsEvent(eventName, parameters))
    }
    
    /**
     * Set user property
     * 
     * @param property User property to set
     */
    fun setUserProperty(property: UserProperty)
    
    /**
     * Set user property with key and value
     * 
     * @param key Property key
     * @param value Property value
     */
    fun setUserProperty(key: String, value: String) {
        setUserProperty(UserProperty(key, value))
    }
    
    /**
     * Set user ID for tracking
     * 
     * @param userId User identifier
     */
    fun setUserId(userId: String?)
    
    /**
     * Get all tracked events (for testing/debugging)
     * 
     * @return List of tracked events
     */
    fun getTrackedEvents(): List<AnalyticsEvent>
    
    /**
     * Get user properties (for testing/debugging)
     * 
     * @return Map of user properties
     */
    fun getUserProperties(): Map<String, String>
    
    /**
     * Clear all tracked data (for testing)
     */
    fun clearAll()
    
    /**
     * Check if analytics is enabled
     */
    fun isEnabled(): Boolean
    
    /**
     * Enable or disable analytics tracking
     * 
     * @param enabled Whether to enable tracking
     */
    fun setEnabled(enabled: Boolean)
}

