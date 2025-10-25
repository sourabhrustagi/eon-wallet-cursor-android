package com.mobizonetech.aeon_wallet_cursor.domain.model

data class Card(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val cardNumber: String,
    val maskedCardNumber: String,
    val cardHolderName: String,
    val expiryDate: String,
    val cvv: String,
    val cardBrand: CardBrand,
    val backgroundColor: Long,
    val textColor: Long,
    val isLocked: Boolean = false
)

enum class CardBrand {
    VISA,
    MASTERCARD,
    AMEX,
    DISCOVER
}
