package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State for Welcome Screen
 * Immutable data class representing the complete UI state
 * 
 * @property slides List of welcome slides to display
 * @property currentPage Current page index in the pager
 * @property isLoading Whether data is being loaded
 * @property error Error message if any error occurred
 */
data class WelcomeUiState(
    val slides: List<WelcomeSlide> = emptyList(),
    val currentPage: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    /**
     * Check if user is on the last page
     */
    val isOnLastPage: Boolean
        get() = slides.isNotEmpty() && currentPage >= slides.size - 1
    
    /**
     * Check if user can navigate to next page
     */
    val canNavigateNext: Boolean
        get() = currentPage < slides.size - 1
    
    /**
     * Check if skip button should be shown
     */
    val canNavigateSkip: Boolean
        get() = currentPage < slides.size - 1
}

/**
 * ViewModel for Welcome/Onboarding Screen
 * Manages UI state and business logic for welcome flow
 * 
 * Follows MVVM pattern with:
 * - Unidirectional data flow
 * - Immutable UI state
 * - StateFlow for state management
 * 
 * @property getWelcomeSlidesUseCase Use case for retrieving welcome slides
 */
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    init {
        Logger.d(TAG, "WelcomeViewModel initialized")
        loadSlides()
    }

    /**
     * Load welcome slides from use case
     * Updates UI state based on result
     */
    private fun loadSlides() {
        viewModelScope.launch {
            Logger.d(TAG, "Loading welcome slides")
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getWelcomeSlidesUseCase()) {
                is Result.Success -> {
                    Logger.d(TAG, "Successfully loaded ${result.data.size} slides")
                    _uiState.update { 
                        it.copy(
                            slides = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    Logger.e(TAG, "Error loading slides: ${result.message}", result.throwable)
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Result.Loading -> {
                    // Loading state already set
                }
            }
        }
    }

    /**
     * Handle page change event from pager
     * @param page New page index
     */
    fun onPageChanged(page: Int) {
        Logger.d(TAG, "Page changed to: $page")
        _uiState.update { it.copy(currentPage = page) }
    }

    /**
     * Handle next button click
     * Advances to next page if possible
     */
    fun onNextClick() {
        val currentState = _uiState.value
        if (currentState.canNavigateNext) {
            Logger.d(TAG, "Navigating to next page: ${currentState.currentPage + 1}")
            _uiState.update { 
                it.copy(currentPage = it.currentPage + 1)
            }
        }
    }

    /**
     * Handle skip button click
     * Jumps to last page
     */
    fun onSkipClick() {
        val currentState = _uiState.value
        if (currentState.slides.isNotEmpty()) {
            Logger.d(TAG, "Skipping to last page: ${currentState.slides.size - 1}")
            _uiState.update { 
                it.copy(currentPage = currentState.slides.size - 1)
            }
        }
    }

    /**
     * Handle get started button click
     * TODO: Navigate to main screen after onboarding
     */
    fun onGetStartedClick() {
        Logger.d(TAG, "Get Started clicked")
        // TODO: Handle get started logic - navigate to main screen
        // Consider using navigation component or callback
    }

    /**
     * Handle sign in button click
     * TODO: Navigate to sign in screen
     */
    fun onSignInClick() {
        Logger.d(TAG, "Sign In clicked")
        // TODO: Handle sign in logic - navigate to sign in screen
        // Consider using navigation component or callback
    }

    companion object {
        private const val TAG = "WelcomeViewModel"
    }
}

