package com.mobizonetech.aeon_wallet_cursor.domain.model

import androidx.compose.runtime.Immutable

/**
 * Data class representing a welcome/onboarding slide
 * @param id Unique identifier for the slide
 * @param title Slide title
 * @param description Slide description
 * @param icon Emoji or icon representation
 * @param iconBackgroundColor Color of the icon background (hex color as Long)
 * @param features List of feature bullet points
 */
@Immutable
data class WelcomeSlide(
    val id: Int,
    val title: String,
    val description: String,
    val icon: String,
    val iconBackgroundColor: Long,
    val features: List<String>
)

