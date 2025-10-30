package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import com.google.common.truth.Truth.assertThat
import com.mobizonetech.aeon_wallet_cursor.data.analytics.MockAnalytics
import com.mobizonetech.aeon_wallet_cursor.domain.model.DefaultAppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetAppSettingsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.presentation.common.WelcomeEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelEventTest {

    @Test
    fun onEvent_routes_to_handlers_and_updates_state() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val slides = listOf(
            WelcomeSlide(0, "t", "d", "i", 0L, emptyList()),
            WelcomeSlide(1, "t2", "d2", "i2", 0L, emptyList())
        )
        val getSlides: GetWelcomeSlidesUseCase = mockk()
        val getSettings: GetAppSettingsUseCase = mockk()
        coEvery { getSlides.invoke() } returns Result.Success(slides)
        coEvery { getSettings.invoke() } returns Result.Success(DefaultAppSettings.default)

        val vm = WelcomeViewModel(getSlides, getSettings, MockAnalytics())

        // Advance initial load
        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
        dispatcher.scheduler.advanceUntilIdle()

        // Next event moves page
        vm.onEvent(WelcomeEvent.Next)
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(vm.uiState.value.currentPage).isEqualTo(1)

        // Skip goes to last
        vm.onEvent(WelcomeEvent.Skip)
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(vm.uiState.value.isOnLastPage).isTrue()

        // PageChanged explicit
        vm.onEvent(WelcomeEvent.PageChanged(0))
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(vm.uiState.value.currentPage).isEqualTo(0)
    }
}


