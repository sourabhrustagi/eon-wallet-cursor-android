package com.mobizonetech.aeon_wallet_cursor.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Login request DTO
 * Data Transfer Object for login API request
 */
data class LoginRequestDto(
    @SerializedName("email_or_phone")
    val emailOrPhone: String,
    
    @SerializedName("password")
    val password: String
)

/**
 * User DTO from API response
 */
data class UserDto(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("is_new_user")
    val isNewUser: Boolean = false
)

/**
 * Login response DTO
 */
data class LoginResponseDto(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: UserDto? = null,
    
    @SerializedName("message")
    val message: String? = null
)


