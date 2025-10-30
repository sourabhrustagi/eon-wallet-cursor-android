package com.mobizonetech.aeon_wallet_cursor.presentation.common

sealed interface WelcomeEvent {
    data class PageChanged(val index: Int) : WelcomeEvent
    object Next : WelcomeEvent
    object Skip : WelcomeEvent
    object GetStarted : WelcomeEvent
    object SignIn : WelcomeEvent
    object Retry : WelcomeEvent
}


