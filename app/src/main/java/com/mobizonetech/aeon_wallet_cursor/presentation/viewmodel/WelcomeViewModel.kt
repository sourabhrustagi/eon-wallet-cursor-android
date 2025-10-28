package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    application: Application,
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase
) : AndroidViewModel(application) {

    private val _slides = MutableStateFlow<List<WelcomeSlide>>(emptyList())
    val slides: StateFlow<List<WelcomeSlide>> = _slides.asStateFlow()

    init {
        loadSlides()
    }

    private fun loadSlides() {
        viewModelScope.launch {
            _slides.value = getWelcomeSlidesUseCase()
        }
    }
}

