package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.data.analytics.Analytics
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsEvent
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackError
import com.mobizonetech.aeon_wallet_cursor.data.analytics.AnalyticsHelper.trackScreenView
import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginResult
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.LoginUseCase
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
 * UI State for Login Screen
 * Immutable data class representing the complete UI state
 * 
 * @property emailOrPhone Email or phone number input
 * @property password Password input
 * @property isLoading Whether login is in progress
 * @property error Error message if any error occurred
 * @property isLoginSuccessful Whether login was successful
 * @property loginResult Login result containing user data
 * @property emailOrPhoneError Validation error for email/phone field
 * @property passwordError Validation error for password field
 */
data class LoginUiState(
    val emailOrPhone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false,
    val loginResult: LoginResult? = null,
    val emailOrPhoneError: String? = null,
    val passwordError: String? = null
) {
    /**
     * Check if form is valid
     */
    val isFormValid: Boolean
        get() = emailOrPhone.isNotBlank() && 
                password.isNotBlank() && 
                emailOrPhoneError == null && 
                passwordError == null
    
    /**
     * Check if login button should be enabled
     */
    val isLoginButtonEnabled: Boolean
        get() = isFormValid && !isLoading
}

/**
 * ViewModel for Login Screen
 * Manages UI state and business logic for user authentication
 * 
 * Follows MVVM pattern with:
 * - Unidirectional data flow
 * - Immutable UI state
 * - StateFlow for state management
 * - Analytics tracking for user events
 * 
 * @property loginUseCase Use case for user login
 * @property analytics Analytics service for event tracking
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val analytics: Analytics
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    private var screenStartTime: Long = 0

    init {
        Logger.d(TAG, "LoginViewModel initialized")
        screenStartTime = System.currentTimeMillis()
        
        // Track screen view
        analytics.trackScreenView("Login Screen")
    }

    /**
     * Update email or phone number
     */
    fun updateEmailOrPhone(value: String) {
        _uiState.update { state ->
            state.copy(
                emailOrPhone = value,
                emailOrPhoneError = null,
                error = null
            )
        }
    }

    /**
     * Update password
     */
    fun updatePassword(value: String) {
        _uiState.update { state ->
            state.copy(
                password = value,
                passwordError = null,
                error = null
            )
        }
    }

    /**
     * Validate email or phone number
     */
    private fun validateEmailOrPhone(value: String): String? {
        return when {
            value.isBlank() -> "Email or phone number is required"
            value.contains("@") && !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() -> 
                "Please enter a valid email address"
            !value.contains("@") && value.length < 10 -> 
                "Please enter a valid phone number"
            else -> null
        }
    }

    /**
     * Validate password
     */
    private fun validatePassword(value: String): String? {
        return when {
            value.isBlank() -> "Password is required"
            value.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    /**
     * Perform login
     */
    fun login() {
        val currentState = _uiState.value
        
        // Validate inputs
        val emailOrPhoneError = validateEmailOrPhone(currentState.emailOrPhone)
        val passwordError = validatePassword(currentState.password)
        
        if (emailOrPhoneError != null || passwordError != null) {
            _uiState.update { state ->
                state.copy(
                    emailOrPhoneError = emailOrPhoneError,
                    passwordError = passwordError
                )
            }
            return
        }
        
        // Clear previous errors
        _uiState.update { state ->
            state.copy(
                isLoading = true,
                error = null,
                emailOrPhoneError = null,
                passwordError = null
            )
        }
        
        viewModelScope.launch {
            Logger.d(TAG, "Attempting login for: ${currentState.emailOrPhone.take(3)}***")
            
            // Track login attempt
            analytics.trackEvent(
                AnalyticsEvent.LOGIN_ATTEMPTED,
                mapOf(
                    "identifier_type" to if (currentState.emailOrPhone.contains("@")) "email" else "phone"
                )
            )
            
            when (val result = loginUseCase(currentState.emailOrPhone, currentState.password)) {
                is Result.Success -> {
                    Logger.d(TAG, "Login successful for user: ${result.data.user.name}")
                    
                    // Calculate time spent on login screen
                    val timeSpent = System.currentTimeMillis() - screenStartTime
                    
                    // Track successful login
                    analytics.trackEvent(
                        AnalyticsEvent.LOGIN_SUCCESS,
                        mapOf(
                            "time_spent_ms" to timeSpent,
                            "is_new_user" to result.data.isNewUser
                        )
                    )
                    
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            loginResult = result.data,
                            error = null
                        )
                    }
                }
                is Result.Error -> {
                    Logger.e(TAG, "Login failed: ${result.message}")
                    
                    // Track login failure
                    analytics.trackError(
                        errorMessage = result.message,
                        source = "LoginUseCase"
                    )
                    
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.message,
                            isLoginSuccessful = false
                        )
                    }
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        _uiState.update { state ->
            state.copy(error = null)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

