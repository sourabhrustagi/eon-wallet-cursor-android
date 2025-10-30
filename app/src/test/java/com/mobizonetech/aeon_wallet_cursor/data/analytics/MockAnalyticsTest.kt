package com.mobizonetech.aeon_wallet_cursor.data.analytics

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for MockAnalytics
 */
class MockAnalyticsTest {

    private lateinit var analytics: MockAnalytics

    @Before
    fun setup() {
        analytics = MockAnalytics()
        analytics.clearAll()
    }

    @Test
    fun `trackEvent stores event`() {
        // Given
        val event = AnalyticsEvent("test_event", mapOf("key" to "value"))

        // When
        analytics.trackEvent(event)

        // Then
        val trackedEvents = analytics.getTrackedEvents()
        assertThat(trackedEvents).hasSize(1)
        assertThat(trackedEvents[0].name).isEqualTo("test_event")
        assertThat(trackedEvents[0].parameters["key"]).isEqualTo("value")
    }

    @Test
    fun `trackEvent with name and parameters stores event`() {
        // When
        analytics.trackEvent("test_event", mapOf("param1" to 123))

        // Then
        val trackedEvents = analytics.getTrackedEvents()
        assertThat(trackedEvents).hasSize(1)
        assertThat(trackedEvents[0].name).isEqualTo("test_event")
        assertThat(trackedEvents[0].parameters["param1"]).isEqualTo(123)
    }

    @Test
    fun `setUserProperty stores property`() {
        // When
        analytics.setUserProperty("user_type", "premium")

        // Then
        val properties = analytics.getUserProperties()
        assertThat(properties).containsEntry("user_type", "premium")
    }

    @Test
    fun `setUserId stores user ID`() {
        // When
        analytics.setUserId("user_12345")

        // Then
        val summary = analytics.getSummary()
        assertThat(summary.userId).isEqualTo("user_12345")
    }

    @Test
    fun `multiple events are tracked in order`() {
        // When
        analytics.trackEvent("event1")
        analytics.trackEvent("event2")
        analytics.trackEvent("event3")

        // Then
        val trackedEvents = analytics.getTrackedEvents()
        assertThat(trackedEvents).hasSize(3)
        assertThat(trackedEvents[0].name).isEqualTo("event1")
        assertThat(trackedEvents[1].name).isEqualTo("event2")
        assertThat(trackedEvents[2].name).isEqualTo("event3")
    }

    @Test
    fun `clearAll removes all events and properties`() {
        // Given
        analytics.trackEvent("event1")
        analytics.setUserProperty("key", "value")
        analytics.setUserId("user_123")

        // When
        analytics.clearAll()

        // Then
        assertThat(analytics.getTrackedEvents()).isEmpty()
        assertThat(analytics.getUserProperties()).isEmpty()
        assertThat(analytics.getSummary().userId).isNull()
    }

    @Test
    fun `disabled analytics does not track events`() {
        // Given
        analytics.setEnabled(false)

        // When
        analytics.trackEvent("test_event")

        // Then
        assertThat(analytics.getTrackedEvents()).isEmpty()
    }

    @Test
    fun `disabled analytics does not set user properties`() {
        // Given
        analytics.setEnabled(false)

        // When
        analytics.setUserProperty("key", "value")

        // Then
        assertThat(analytics.getUserProperties()).isEmpty()
    }

    @Test
    fun `enabling analytics after disabling works`() {
        // Given
        analytics.setEnabled(false)
        analytics.trackEvent("event1")
        
        // When
        analytics.setEnabled(true)
        analytics.trackEvent("event2")

        // Then
        val trackedEvents = analytics.getTrackedEvents()
        assertThat(trackedEvents).hasSize(1)
        assertThat(trackedEvents[0].name).isEqualTo("event2")
    }

    @Test
    fun `getSummary returns correct statistics`() {
        // Given
        analytics.trackEvent("event1")
        analytics.trackEvent("event2")
        analytics.trackEvent("event1") // Duplicate
        analytics.setUserProperty("key1", "value1")
        analytics.setUserProperty("key2", "value2")
        analytics.setUserId("user_123")

        // When
        val summary = analytics.getSummary()

        // Then
        assertThat(summary.totalEvents).isEqualTo(3)
        assertThat(summary.uniqueEventNames).isEqualTo(2)
        assertThat(summary.userPropertiesCount).isEqualTo(2)
        assertThat(summary.userId).isEqualTo("user_123")
    }

    @Test
    fun `getSummary includes most tracked events`() {
        // Given
        repeat(5) { analytics.trackEvent("frequent_event") }
        repeat(3) { analytics.trackEvent("less_frequent") }
        repeat(1) { analytics.trackEvent("rare_event") }

        // When
        val summary = analytics.getSummary()

        // Then
        assertThat(summary.mostTrackedEvents).hasSize(3)
        assertThat(summary.mostTrackedEvents[0].first).isEqualTo("frequent_event")
        assertThat(summary.mostTrackedEvents[0].second).isEqualTo(5)
        assertThat(summary.mostTrackedEvents[1].first).isEqualTo("less_frequent")
        assertThat(summary.mostTrackedEvents[1].second).isEqualTo(3)
    }

    @Test
    fun `event timestamps are recorded`() {
        // Given
        val beforeTime = System.currentTimeMillis()

        // When
        analytics.trackEvent("test_event")

        // Then
        val afterTime = System.currentTimeMillis()
        val trackedEvents = analytics.getTrackedEvents()
        assertThat(trackedEvents[0].timestamp).isAtLeast(beforeTime)
        assertThat(trackedEvents[0].timestamp).isAtMost(afterTime)
    }

    @Test
    fun `isEnabled returns correct state`() {
        // When/Then
        assertThat(analytics.isEnabled()).isTrue()

        analytics.setEnabled(false)
        assertThat(analytics.isEnabled()).isFalse()

        analytics.setEnabled(true)
        assertThat(analytics.isEnabled()).isTrue()
    }
}

