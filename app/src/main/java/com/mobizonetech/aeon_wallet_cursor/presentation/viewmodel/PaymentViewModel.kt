package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentData(
    val recipient: String,
    val amount: String,
    val note: String,
    val paymentMethod: String
)

data class RequestData(
    val requester: String,
    val amount: String,
    val note: String,
    val requestMethod: String
)

@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _paymentSuccess = MutableStateFlow(false)
    val paymentSuccess: StateFlow<Boolean> = _paymentSuccess.asStateFlow()

    private val _requestSuccess = MutableStateFlow(false)
    val requestSuccess: StateFlow<Boolean> = _requestSuccess.asStateFlow()

    fun validateRecipient(recipient: String): String {
        return when {
            recipient.isEmpty() -> "Recipient is required"
            recipient.length < 2 -> "Recipient name must be at least 2 characters"
            else -> ""
        }
    }

    fun validateAmount(amount: String): String {
        return when {
            amount.isEmpty() -> "Amount is required"
            amount.toDoubleOrNull() == null -> "Please enter a valid amount"
            amount.toDoubleOrNull()!! <= 0 -> "Amount must be greater than 0"
            amount.toDoubleOrNull()!! > 10000 -> "Amount cannot exceed $10,000"
            else -> ""
        }
    }

    fun validateNote(note: String): String {
        return when {
            note.length > 100 -> "Note cannot exceed 100 characters"
            else -> ""
        }
    }

    fun validatePhoneNumber(phoneNumber: String): String {
        return when {
            phoneNumber.isEmpty() -> "Phone number is required"
            phoneNumber.length < 10 -> "Phone number must be at least 10 digits"
            !phoneNumber.all { it.isDigit() } -> "Phone number must contain only digits"
            else -> ""
        }
    }

    fun validateEmail(email: String): String {
        return when {
            email.isEmpty() -> "Email is required"
            !email.contains("@") -> "Please enter a valid email"
            else -> ""
        }
    }

    fun sendMoney(paymentData: PaymentData) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _paymentSuccess.value = false
                
                // Simulate API call
                kotlinx.coroutines.delay(2000)
                
                // Mock validation
                if (paymentData.amount.toDoubleOrNull()!! > 5000) {
                    throw Exception("Transaction limit exceeded")
                }
                
                _paymentSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to send money"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun requestMoney(requestData: RequestData) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _requestSuccess.value = false
                
                // Simulate API call
                kotlinx.coroutines.delay(2000)
                
                _requestSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to request money"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPaymentSuccess() {
        _paymentSuccess.value = false
    }

    fun resetRequestSuccess() {
        _requestSuccess.value = false
    }

    fun clearError() {
        _error.value = null
    }
}
