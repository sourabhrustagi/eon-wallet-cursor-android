package com.mobizonetech.aeon_wallet_cursor.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for WelcomeSlide from API
 * 
 * Maps JSON response to Kotlin data class
 */
data class WelcomeSlideDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("icon")
    val icon: String,
    
    @SerializedName("icon_background_color")
    val iconBackgroundColor: String, // Hex color string from API
    
    @SerializedName("features")
    val features: List<String>
)

/**
 * API Response wrapper
 */
data class WelcomeSlidesResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: List<WelcomeSlideDto>,
    
    @SerializedName("message")
    val message: String? = null
)

