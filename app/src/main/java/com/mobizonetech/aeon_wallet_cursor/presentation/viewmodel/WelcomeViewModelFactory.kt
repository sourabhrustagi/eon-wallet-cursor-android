package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase

class WelcomeViewModelFactory(
    private val application: Application,
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WelcomeViewModel(application, getWelcomeSlidesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

