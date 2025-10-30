package com.mobizonetech.aeon_wallet_cursor.data.remote.dto

data class PromotionDto(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val validUntil: String?
)

data class PromotionsResponse(
    val success: Boolean,
    val message: String?,
    val data: List<PromotionDto>
)


