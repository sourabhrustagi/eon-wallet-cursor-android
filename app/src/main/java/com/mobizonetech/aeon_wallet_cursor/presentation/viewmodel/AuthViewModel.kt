package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        observeLoginState()
    }

    private fun observeLoginState() {
        viewModelScope.launch {
            userRepository.isUserLoggedIn().collect { loggedIn ->
                _isLoggedIn.value = loggedIn
            }
        }
    }

    fun validateId(id: String): String {
        return when {
            id.isEmpty() -> "ID is required"
            id.length < 5 -> "ID must be at least 5 characters"
            else -> ""
        }
    }

    fun validateCardNumber(cardNumber: String): String {
        return when {
            cardNumber.isEmpty() -> "Card number is required"
            cardNumber.length < 16 -> "Card number must be 16 digits"
            !cardNumber.all { it.isDigit() } -> "Card number must contain only digits"
            else -> ""
        }
    }

    fun validateOtp(otp: String): String {
        return when {
            otp.isEmpty() -> "OTP is required"
            otp.length != 6 -> "OTP must be 6 digits"
            !otp.all { it.isDigit() } -> "OTP must contain only digits"
            else -> ""
        }
    }

    fun validatePasscode(passcode: String): String {
        return when {
            passcode.isEmpty() -> "Passcode is required"
            passcode.length != 6 -> "Passcode must be 6 digits"
            !passcode.all { it.isDigit() } -> "Passcode must contain only digits"
            else -> ""
        }
    }

    fun validateSecurityPasscode(securityPasscode: String): String {
        return when {
            securityPasscode.isEmpty() -> "Security passcode is required"
            securityPasscode.length != 6 -> "Security passcode must be 6 digits"
            !securityPasscode.all { it.isDigit() } -> "Security passcode must contain only digits"
            else -> ""
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.logout()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to logout"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
