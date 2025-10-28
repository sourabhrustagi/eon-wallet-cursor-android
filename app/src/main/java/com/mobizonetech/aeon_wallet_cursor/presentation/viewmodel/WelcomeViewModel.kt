package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase
) : ViewModel() {

    private val _slides = MutableStateFlow<List<WelcomeSlide>>(emptyList())
    val slides: StateFlow<List<WelcomeSlide>> = _slides.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    // Computed state for isOnLastPage
    val isOnLastPage: StateFlow<Boolean> = combine(
        _slides,
        _currentPage
    ) { slides, currentPage ->
        slides.isNotEmpty() && currentPage >= slides.size - 1
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    init {
        loadSlides()
    }

    private fun loadSlides() {
        viewModelScope.launch {
            _slides.value = getWelcomeSlidesUseCase()
        }
    }

    fun onPageChanged(page: Int) {
        _currentPage.value = page
    }

    fun onNextClick() {
        val slides = _slides.value
        if (_currentPage.value < slides.size - 1) {
            _currentPage.value = _currentPage.value + 1
        }
    }

    fun onSkipClick() {
        val slides = _slides.value
        if (slides.isNotEmpty()) {
            _currentPage.value = slides.size - 1
        }
    }

    fun onGetStartedClick() {
        // Handle get started logic
    }

    fun onSignInClick() {
        // Handle sign in logic
    }

    fun canNavigateNext(): Boolean {
        return _currentPage.value < _slides.value.size - 1
    }

    fun canNavigateSkip(): Boolean {
        return _currentPage.value < _slides.value.size - 1
    }
}

