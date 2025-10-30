package com.mobizonetech.aeon_wallet_cursor.data.analytics

import com.google.common.truth.Truth.assertThat
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackError
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackScreenView
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackSlideChange
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for AnalyticsHelper extension functions
 */
class AnalyticsHelperTest {

    private lateinit var mockAnalytics: MockAnalytics

    @Before
    fun setup() {
        mockAnalytics = MockAnalytics()
        mockAnalytics.clearAll()
    }

    @Test
    fun `trackScreenView tracks correct event`() {
        // When
        mockAnalytics.trackScreenView("Test Screen")

        // Then
        val events = mockAnalytics.getTrackedEvents()
        assertThat(events).hasSize(1)
        assertThat(events[0].name).isEqualTo(AnalyticsEvent.WELCOME_SCREEN_VIEWED)
        assertThat(events[0].parameters[AnalyticsEvent.PARAM_SCREEN_NAME])
            .isEqualTo("Test Screen")
    }

    @Test
    fun `trackSlideChange tracks correct event with all parameters`() {
        // When
        mockAnalytics.trackSlideChange(
            slideIndex = 2,
            slideTitle = "Test Slide",
            totalSlides = 5
        )

        // Then
        val events = mockAnalytics.getTrackedEvents()
        assertThat(events).hasSize(1)
        
        val event = events[0]
        assertThat(event.name).isEqualTo(AnalyticsEvent.WELCOME_SLIDE_CHANGED)
        assertThat(event.parameters[AnalyticsEvent.PARAM_SLIDE_INDEX]).isEqualTo(2)
        assertThat(event.parameters[AnalyticsEvent.PARAM_SLIDE_TITLE]).isEqualTo("Test Slide")
        assertThat(event.parameters[AnalyticsEvent.PARAM_TOTAL_SLIDES]).isEqualTo(5)
    }

    @Test
    fun `trackError tracks error with all parameters`() {
        // When
        mockAnalytics.trackError(
            errorMessage = "Test error",
            errorCode = "ERR_001",
            source = "TestClass"
        )

        // Then
        val events = mockAnalytics.getTrackedEvents()
        assertThat(events).hasSize(1)
        
        val event = events[0]
        assertThat(event.name).isEqualTo(AnalyticsEvent.ERROR_OCCURRED)
        assertThat(event.parameters[AnalyticsEvent.PARAM_ERROR_MESSAGE])
            .isEqualTo("Test error")
        assertThat(event.parameters[AnalyticsEvent.PARAM_ERROR_CODE])
            .isEqualTo("ERR_001")
        assertThat(event.parameters[AnalyticsEvent.PARAM_SOURCE])
            .isEqualTo("TestClass")
    }

    @Test
    fun `trackError tracks error with minimal parameters`() {
        // When
        mockAnalytics.trackError(errorMessage = "Test error")

        // Then
        val events = mockAnalytics.getTrackedEvents()
        assertThat(events).hasSize(1)
        
        val event = events[0]
        assertThat(event.name).isEqualTo(AnalyticsEvent.ERROR_OCCURRED)
        assertThat(event.parameters[AnalyticsEvent.PARAM_ERROR_MESSAGE])
            .isEqualTo("Test error")
        assertThat(event.parameters).doesNotContainKey(AnalyticsEvent.PARAM_ERROR_CODE)
        assertThat(event.parameters).doesNotContainKey(AnalyticsEvent.PARAM_SOURCE)
    }
}

