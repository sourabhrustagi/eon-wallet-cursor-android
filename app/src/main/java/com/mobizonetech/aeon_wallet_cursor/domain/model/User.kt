package com.mobizonetech.aeon_wallet_cursor.domain.model

import androidx.compose.runtime.Immutable

/**
 * User domain model
 * Represents an authenticated user in the system
 * 
 * @param id Unique user identifier
 * @param name User's full name
 * @param email User's email address
 * @param phone User's phone number (optional)
 * @param token Authentication token for API requests
 */
@Immutable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val token: String
)


