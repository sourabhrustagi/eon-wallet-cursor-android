package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase
) : ViewModel() {

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

