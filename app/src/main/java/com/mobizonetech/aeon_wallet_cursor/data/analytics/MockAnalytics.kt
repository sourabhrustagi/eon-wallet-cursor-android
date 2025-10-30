package com.mobizonetech.aeon_wallet_cursor.data.analytics

import com.mobizonetech.aeon_wallet_cursor.util.Logger
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock Analytics implementation for development and testing
 * 
 * Features:
 * - Logs all events to console
 * - Stores events in memory for testing
 * - Thread-safe implementation
 * - No external dependencies
 * - Can be easily swapped with real analytics
 * 
 * Usage:
 * - Use in debug builds for development
 * - Useful for unit/integration testing
 * - No data sent to external services
 */
@Singleton
class MockAnalytics @Inject constructor() : Analytics {

    private val events = mutableListOf<AnalyticsEvent>()
    private val userProperties = ConcurrentHashMap<String, String>()
    private var userId: String? = null
    private var enabled: Boolean = true
    
    // Session tracking
    private var sessionStartTime: Long = System.currentTimeMillis()
    private var eventCount: Int = 0

    override fun trackEvent(event: AnalyticsEvent) {
        if (!enabled) {
            Logger.d(TAG, "Analytics disabled, skipping event: ${event.name}")
            return
        }
        
        synchronized(events) {
            events.add(event)
            eventCount++
        }
        
        // Log event details
        Logger.i(TAG, "📊 Analytics Event: ${event.name}")
        
        if (event.parameters.isNotEmpty()) {
            Logger.d(TAG, "  Parameters:")
            event.parameters.forEach { (key, value) ->
                Logger.d(TAG, "    • $key: $value")
            }
        }
        
        Logger.d(TAG, "  Timestamp: ${event.timestamp}")
        Logger.d(TAG, "  Total Events: $eventCount")
        
        // Log session info
        val sessionDuration = System.currentTimeMillis() - sessionStartTime
        Logger.d(TAG, "  Session Duration: ${sessionDuration}ms")
    }

    override fun setUserProperty(property: UserProperty) {
        if (!enabled) {
            Logger.d(TAG, "Analytics disabled, skipping user property: ${property.key}")
            return
        }
        
        userProperties[property.key] = property.value
        Logger.i(TAG, "👤 User Property Set: ${property.key} = ${property.value}")
    }

    override fun setUserId(userId: String?) {
        if (!enabled) {
            Logger.d(TAG, "Analytics disabled, skipping user ID")
            return
        }
        
        this.userId = userId
        Logger.i(TAG, "🆔 User ID Set: $userId")
    }

    override fun getTrackedEvents(): List<AnalyticsEvent> {
        return synchronized(events) {
            events.toList()
        }
    }

    override fun getUserProperties(): Map<String, String> {
        return userProperties.toMap()
    }

    override fun clearAll() {
        synchronized(events) {
            events.clear()
        }
        userProperties.clear()
        userId = null
        eventCount = 0
        sessionStartTime = System.currentTimeMillis()
        Logger.i(TAG, "🧹 Analytics data cleared")
    }

    override fun isEnabled(): Boolean = enabled

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        Logger.i(TAG, "Analytics ${if (enabled) "enabled" else "disabled"}")
    }

    /**
     * Get analytics summary for debugging
     */
    fun getSummary(): AnalyticsSummary {
        return AnalyticsSummary(
            totalEvents = eventCount,
            uniqueEventNames = events.map { it.name }.distinct().size,
            sessionDuration = System.currentTimeMillis() - sessionStartTime,
            userPropertiesCount = userProperties.size,
            userId = userId,
            mostTrackedEvents = getMostTrackedEvents(5)
        )
    }

    /**
     * Get most tracked events
     */
    private fun getMostTrackedEvents(limit: Int): List<Pair<String, Int>> {
        return synchronized(events) {
            events.groupBy { it.name }
                .mapValues { it.value.size }
                .toList()
                .sortedByDescending { it.second }
                .take(limit)
        }
    }

    /**
     * Print analytics summary to console
     */
    fun printSummary() {
        val summary = getSummary()
        Logger.i(TAG, "")
        Logger.i(TAG, "═══════════════════════════════════════")
        Logger.i(TAG, "📊 Analytics Summary")
        Logger.i(TAG, "═══════════════════════════════════════")
        Logger.i(TAG, "Total Events: ${summary.totalEvents}")
        Logger.i(TAG, "Unique Events: ${summary.uniqueEventNames}")
        Logger.i(TAG, "Session Duration: ${summary.sessionDuration}ms")
        Logger.i(TAG, "User Properties: ${summary.userPropertiesCount}")
        Logger.i(TAG, "User ID: ${summary.userId ?: "Not Set"}")
        Logger.i(TAG, "")
        Logger.i(TAG, "Top Events:")
        summary.mostTrackedEvents.forEachIndexed { index, (name, count) ->
            Logger.i(TAG, "  ${index + 1}. $name: $count times")
        }
        Logger.i(TAG, "═══════════════════════════════════════")
        Logger.i(TAG, "")
    }

    companion object {
        private const val TAG = "MockAnalytics"
    }
}

/**
 * Analytics summary data class
 */
data class AnalyticsSummary(
    val totalEvents: Int,
    val uniqueEventNames: Int,
    val sessionDuration: Long,
    val userPropertiesCount: Int,
    val userId: String?,
    val mostTrackedEvents: List<Pair<String, Int>>
)

