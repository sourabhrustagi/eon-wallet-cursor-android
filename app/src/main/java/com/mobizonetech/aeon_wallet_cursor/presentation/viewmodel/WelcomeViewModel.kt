package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.data.analytics.Analytics
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsEvent
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackError
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackScreenView
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackSlideChange
import com.mobizonetech.aeon_wallet_cursor.domain.model.AppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.model.DefaultAppSettings
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetAppSettingsUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
 * @property appSettings App settings and configuration
 * @property isSettingsLoading Whether settings are being loaded
 */
data class WelcomeUiState(
    val slides: List<WelcomeSlide> = emptyList(),
    val currentPage: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null,
    val appSettings: AppSettings = DefaultAppSettings.default,
    val isSettingsLoading: Boolean = true
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
        get() = currentPage < slides.size - 1 && appSettings.welcomeScreenConfig.showSkipButton
    
    /**
     * Check if all data is loaded
     */
    val isAllDataLoaded: Boolean
        get() = !isLoading && !isSettingsLoading
}

/**
 * ViewModel for Welcome/Onboarding Screen
 * Manages UI state and business logic for welcome flow
 * 
 * Follows MVVM pattern with:
 * - Unidirectional data flow
 * - Immutable UI state
 * - StateFlow for state management
 * - Parallel API calls for performance
 * - Analytics tracking for user events
 * 
 * @property getWelcomeSlidesUseCase Use case for retrieving welcome slides
 * @property getAppSettingsUseCase Use case for retrieving app settings
 * @property analytics Analytics service for event tracking
 */
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val analytics: Analytics
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()
    
    private var screenStartTime: Long = 0

    init {
        Logger.d(TAG, "WelcomeViewModel initialized")
        screenStartTime = System.currentTimeMillis()
        
        // Track screen view
        analytics.trackScreenView("Welcome Screen")
        
        loadDataParallel()
    }

    /**
     * Load welcome slides and app settings in parallel
     * Makes both API calls simultaneously for better performance
     */
    private fun loadDataParallel() {
        viewModelScope.launch {
            Logger.d(TAG, "Loading slides and settings in parallel")
            _uiState.update { 
                it.copy(
                    isLoading = true, 
                    isSettingsLoading = true,
                    error = null
                ) 
            }
            
            // Launch both API calls in parallel using async
            val slidesDeferred = async { getWelcomeSlidesUseCase() }
            val settingsDeferred = async { getAppSettingsUseCase() }
            
            // Await both results
            val slidesResult = slidesDeferred.await()
            val settingsResult = settingsDeferred.await()
            
            Logger.d(TAG, "Both API calls completed")
            
            // Process slides result
            when (slidesResult) {
                is Result.Success -> {
                    Logger.d(TAG, "Successfully loaded ${slidesResult.data.size} slides")
                    _uiState.update { 
                        it.copy(
                            slides = slidesResult.data,
                            isLoading = false
                        )
                    }
                    
                    // Track successful data load
                    analytics.trackEvent(
                        "slides_loaded",
                        mapOf("slide_count" to slidesResult.data.size)
                    )
                }
                is Result.Error -> {
                    Logger.e(TAG, "Error loading slides: ${slidesResult.message}", slidesResult.throwable)
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = slidesResult.message
                        )
                    }
                    
                    // Track error
                    analytics.trackError(
                        errorMessage = slidesResult.message,
                        source = "GetWelcomeSlidesUseCase"
                    )
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
            
            // Process settings result
            when (settingsResult) {
                is Result.Success -> {
                    Logger.d(TAG, "Successfully loaded app settings")
                    Logger.d(TAG, "Auto-advance: ${settingsResult.data.welcomeScreenConfig.autoAdvanceEnabled}")
                    Logger.d(TAG, "Show skip: ${settingsResult.data.welcomeScreenConfig.showSkipButton}")
                    _uiState.update { 
                        it.copy(
                            appSettings = settingsResult.data,
                            isSettingsLoading = false
                        )
                    }
                    
                    // Track settings load
                    analytics.trackEvent(
                        AnalyticsEvent.SETTINGS_LOADED,
                        mapOf(
                            "app_version" to settingsResult.data.appVersion,
                            "auto_advance_enabled" to settingsResult.data.welcomeScreenConfig.autoAdvanceEnabled
                        )
                    )
                }
                is Result.Error -> {
                    Logger.e(TAG, "Error loading settings: ${settingsResult.message}", settingsResult.throwable)
                    // Use default settings on error
                    _uiState.update { 
                        it.copy(
                            appSettings = DefaultAppSettings.default,
                            isSettingsLoading = false
                        )
                    }
                    
                    // Track error
                    analytics.trackError(
                        errorMessage = settingsResult.message,
                        source = "GetAppSettingsUseCase"
                    )
                }
                is Result.Loading -> {
                    // Already in loading state
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
        val currentState = _uiState.value
        _uiState.update { it.copy(currentPage = page) }
        
        // Track slide change
        if (currentState.slides.isNotEmpty() && page < currentState.slides.size) {
            val slide = currentState.slides[page]
            analytics.trackSlideChange(
                slideIndex = page,
                slideTitle = slide.title,
                totalSlides = currentState.slides.size
            )
        }
    }

    /**
     * Handle next button click
     * Advances to next page if possible
     */
    fun onNextClick() {
        val currentState = _uiState.value
        if (currentState.canNavigateNext) {
            Logger.d(TAG, "Navigating to next page: ${currentState.currentPage + 1}")
            
            // Track next button click
            analytics.trackEvent(
                AnalyticsEvent.WELCOME_NEXT_CLICKED,
                mapOf(
                    AnalyticsEvent.PARAM_SLIDE_INDEX to currentState.currentPage,
                    AnalyticsEvent.PARAM_TOTAL_SLIDES to currentState.slides.size
                )
            )
            
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
            
            // Track skip button click
            analytics.trackEvent(
                AnalyticsEvent.WELCOME_SKIP_CLICKED,
                mapOf(
                    AnalyticsEvent.PARAM_SLIDE_INDEX to currentState.currentPage,
                    AnalyticsEvent.PARAM_TOTAL_SLIDES to currentState.slides.size,
                    "skipped_slides" to (currentState.slides.size - 1 - currentState.currentPage)
                )
            )
            
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
        
        // Calculate time spent on welcome screen
        val timeSpent = System.currentTimeMillis() - screenStartTime
        
        // Track get started click
        analytics.trackEvent(
            AnalyticsEvent.WELCOME_GET_STARTED_CLICKED,
            mapOf(
                AnalyticsEvent.PARAM_TIME_SPENT to timeSpent,
                "slides_viewed" to (_uiState.value.currentPage + 1)
            )
        )
        
        // Track welcome flow completion
        analytics.trackEvent(
            AnalyticsEvent.WELCOME_COMPLETED,
            mapOf(
                AnalyticsEvent.PARAM_TIME_SPENT to timeSpent,
                "completion_method" to "get_started"
            )
        )
        
        // TODO: Handle get started logic - navigate to main screen
        // Consider using navigation component or callback
    }

    /**
     * Handle sign in button click
     * TODO: Navigate to sign in screen
     */
    fun onSignInClick() {
        Logger.d(TAG, "Sign In clicked")
        
        // Calculate time spent on welcome screen
        val timeSpent = System.currentTimeMillis() - screenStartTime
        
        // Track sign in click
        analytics.trackEvent(
            AnalyticsEvent.WELCOME_SIGN_IN_CLICKED,
            mapOf(
                AnalyticsEvent.PARAM_TIME_SPENT to timeSpent,
                AnalyticsEvent.PARAM_SLIDE_INDEX to _uiState.value.currentPage
            )
        )
        
        // Track welcome flow completion
        analytics.trackEvent(
            AnalyticsEvent.WELCOME_COMPLETED,
            mapOf(
                AnalyticsEvent.PARAM_TIME_SPENT to timeSpent,
                "completion_method" to "sign_in"
            )
        )
        
        // TODO: Handle sign in logic - navigate to sign in screen
        // Consider using navigation component or callback
    }

    companion object {
        private const val TAG = "WelcomeViewModel"
    }
}

