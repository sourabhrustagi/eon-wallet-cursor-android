package com.mobizonetech.aeon_wallet_cursor.domain.model

import androidx.compose.runtime.Immutable

/**
 * Login result model
 * Represents the result of a login attempt
 * 
 * @param user Authenticated user information
 * @param isNewUser Whether this is a new user (first login)
 */
@Immutable
data class LoginResult(
    val user: User,
    val isNewUser: Boolean = false
)


