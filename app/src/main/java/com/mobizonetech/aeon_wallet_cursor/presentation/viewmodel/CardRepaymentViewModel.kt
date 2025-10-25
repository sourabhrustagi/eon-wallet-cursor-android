package com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.aeon_wallet_cursor.domain.model.Card
import com.mobizonetech.aeon_wallet_cursor.domain.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CardRepaymentData(
    val cardId: String,
    val cardName: String,
    val currentBalance: String,
    val minimumPayment: String,
    val fullBalance: String,
    val dueDate: String
)

class CardRepaymentViewModel(
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _cardData = MutableStateFlow<CardRepaymentData?>(null)
    val cardData: StateFlow<CardRepaymentData?> = _cardData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCardData(cardId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val card = cardRepository.getCardById(cardId)
                if (card != null) {
                    _cardData.value = CardRepaymentData(
                        cardId = card.id,
                        cardName = card.title,
                        currentBalance = card.amount,
                        minimumPayment = calculateMinimumPayment(card.amount),
                        fullBalance = card.amount,
                        dueDate = "Dec 25, 2024" // This could come from card data or separate API
                    )
                } else {
                    _error.value = "Card not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load card data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateMinimumPayment(balance: String): String {
        // Extract numeric value from balance string (e.g., "$2,500.00" -> 2500.0)
        val numericValue = balance.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        val minimumPayment = numericValue * 0.03 // 3% minimum payment
        return "$${String.format("%.2f", minimumPayment)}"
    }

    fun calculateTotalAmount(paymentType: String, customAmount: String? = null): String {
        val cardData = _cardData.value ?: return "$0.00"
        val processingFee = 2.50
        
        val baseAmount = when (paymentType) {
            "minimum" -> cardData.minimumPayment.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            "full" -> cardData.fullBalance.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
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
        
        val cardData = _cardData.value
        val minimumPayment = cardData?.minimumPayment?.replace("[^0-9.]".toRegex(), "")?.toDoubleOrNull() ?: 0.0
        
        if (numericValue < minimumPayment) {
            return "Amount must be at least $${String.format("%.2f", minimumPayment)}"
        }
        
        return ""
    }

    fun clearError() {
        _error.value = null
    }
}
