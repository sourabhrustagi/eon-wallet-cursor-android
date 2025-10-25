package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.Loan
import com.mobizonetech.aeon_wallet_cursor.domain.repository.LoanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoanRepaymentData(
    val loanId: String,
    val loanName: String,
    val currentBalance: String,
    val minimumPayment: String,
    val fullBalance: String,
    val dueDate: String
)

@HiltViewModel
class LoanRepaymentViewModel @Inject constructor(
    private val loanRepository: LoanRepository
) : ViewModel() {

    private val _loanData = MutableStateFlow<LoanRepaymentData?>(null)
    val loanData: StateFlow<LoanRepaymentData?> = _loanData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadLoanData(loanId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val loan = loanRepository.getLoanById(loanId)
                if (loan != null) {
                    _loanData.value = LoanRepaymentData(
                        loanId = loan.id,
                        loanName = loan.title,
                        currentBalance = loan.amount,
                        minimumPayment = calculateMinimumPayment(loan.amount),
                        fullBalance = loan.amount,
                        dueDate = "Dec 25, 2024" // This could come from loan data or separate API
                    )
                } else {
                    _error.value = "Loan not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load loan data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateMinimumPayment(balance: String): String {
        // Extract numeric value from balance string (e.g., "$15,000" -> 15000.0)
        val numericValue = balance.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        val minimumPayment = numericValue * 0.05 // 5% minimum payment for loans
        return "$${String.format("%.2f", minimumPayment)}"
    }

    fun calculateTotalAmount(paymentType: String, customAmount: String? = null): String {
        val loanData = _loanData.value ?: return "$0.00"
        val processingFee = 2.50
        
        val baseAmount = when (paymentType) {
            "minimum" -> loanData.minimumPayment.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            "full" -> loanData.fullBalance.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            "partial" -> customAmount?.replace("[^0-9.]".toRegex(), "")?.toDoubleOrNull() ?: 0.0
            else -> 0.0
        }
        
        val totalAmount = baseAmount + processingFee
        return "$${String.format("%.2f", totalAmount)}"
    }

    fun validateCustomAmount(amount: String): String {
        if (amount.isEmpty()) return ""
        
        val numericValue = amount.toDoubleOrNull()
        if (numericValue == null) return "Please enter a valid amount"
        
        val loanData = _loanData.value
        val minimumPayment = loanData?.minimumPayment?.replace("[^0-9.]".toRegex(), "")?.toDoubleOrNull() ?: 0.0
        
        if (numericValue < minimumPayment) {
            return "Amount must be at least $${String.format("%.2f", minimumPayment)}"
        }
        
        return ""
    }

    fun clearError() {
        _error.value = null
    }
}
