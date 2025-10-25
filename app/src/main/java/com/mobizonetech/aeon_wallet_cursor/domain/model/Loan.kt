package com.mobizonetech.aeon_wallet_cursor.domain.model

data class Loan(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val backgroundColor: Long,
    val textColor: Long,
    val isLocked: Boolean = false
)
