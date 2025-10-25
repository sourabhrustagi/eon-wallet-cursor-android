package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import javax.inject.Inject

data class UtilityBillInfo(
    val id: String,
    val name: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color
)

@HiltViewModel
class UtilityPaymentViewModel @Inject constructor() : ViewModel() {

    private val _billInfo = MutableStateFlow<UtilityBillInfo?>(null)
    val billInfo: StateFlow<UtilityBillInfo?> = _billInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadBillInfo(billId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val bill = getUtilityBillInfo(billId)
                _billInfo.value = bill
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load bill information"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getUtilityBillInfo(billId: String): UtilityBillInfo {
        return when (billId) {
            "electricity" -> UtilityBillInfo(
                id = "electricity",
                name = "Electricity",
                description = "Pay your electricity bill",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF4CAF50)
            )
            "mobile" -> UtilityBillInfo(
                id = "mobile",
                name = "Mobile Recharge",
                description = "Recharge your mobile phone",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
            )
            "water" -> UtilityBillInfo(
                id = "water",
                name = "Water Bill",
                description = "Pay your water bill",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF00BCD4)
            )
            "gas" -> UtilityBillInfo(
                id = "gas",
                name = "Gas Bill",
                description = "Pay your gas bill",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFFFF9800)
            )
            "internet" -> UtilityBillInfo(
                id = "internet",
                name = "Internet",
                description = "Pay your internet bill",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF9C27B0)
            )
            "cable" -> UtilityBillInfo(
                id = "cable",
                name = "Cable TV",
                description = "Pay your cable TV bill",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFFE91E63)
            )
            else -> UtilityBillInfo(
                id = "unknown",
                name = "Unknown Bill",
                description = "Pay your bill",
                icon = Icons.Default.Star,
                backgroundColor = androidx.compose.ui.graphics.Color(0xFF757575)
            )
        }
    }

    fun validateAccountNumber(accountNumber: String): String {
        return when {
            accountNumber.isEmpty() -> "Account number is required"
            accountNumber.length < 8 -> "Account number must be at least 8 digits"
            else -> ""
        }
    }

    fun validateCustomerName(customerName: String): String {
        return when {
            customerName.isEmpty() -> "Customer name is required"
            customerName.length < 2 -> "Name must be at least 2 characters"
            else -> ""
        }
    }

    fun validatePhoneNumber(phoneNumber: String): String {
        return when {
            phoneNumber.isEmpty() -> "Phone number is required"
            phoneNumber.length < 10 -> "Phone number must be at least 10 digits"
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

    fun validateAmount(amount: String): String {
        return when {
            amount.isEmpty() -> "Amount is required"
            amount.toDoubleOrNull() == null -> "Please enter a valid amount"
            amount.toDoubleOrNull()!! < 1.0 -> "Amount must be at least $1.00"
            else -> ""
        }
    }

    fun calculateTotalAmount(amount: String): String {
        val baseAmount = amount.toDoubleOrNull() ?: 0.0
        val processingFee = 2.50
        val totalAmount = baseAmount + processingFee
        return "$${String.format("%.2f", totalAmount)}"
    }

    fun clearError() {
        _error.value = null
    }
}
