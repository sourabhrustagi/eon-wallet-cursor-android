package com.mobizonetech.aeon_wallet_cursor.domain.model

data class WelcomeSlide(
    val id: Int,
    val title: String,
    val description: String,
    val icon: String,
    val iconBackgroundColor: Long,
    val features: List<String>
)

