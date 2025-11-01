package com.mobizonetech.aeon_wallet_cursor.domain.model

import androidx.compose.runtime.Immutable

/**
 * Login request model
 * Represents user credentials for authentication
 * 
 * @param emailOrPhone Email address or phone number
 * @param password User password
 */
@Immutable
data class LoginRequest(
    val emailOrPhone: String,
    val password: String
) {
    /**
     * Validates the login request
     * @return true if valid, false otherwise
     */
    fun isValid(): Boolean {
        return emailOrPhone.isNotBlank() && password.isNotBlank()
    }
    
    /**
     * Checks if the identifier is an email
     * Simple email validation
     */
    fun isEmail(): Boolean {
        return emailOrPhone.contains("@")
    }
}


