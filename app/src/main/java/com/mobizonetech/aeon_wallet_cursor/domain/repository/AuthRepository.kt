package com.mobizonetech.aeon_wallet_cursor.domain.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginRequest
import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginResult
import com.mobizonetech.aeon_wallet_cursor.domain.model.User
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result

/**
 * Authentication repository interface
 * Handles user authentication operations
 */
interface AuthRepository {
    /**
     * Login with email/phone and password
     * @param request Login credentials
     * @return Result containing LoginResult on success or error message
     */
    suspend fun login(request: LoginRequest): Result<LoginResult>
    
    /**
     * Logout current user
     * Clears authentication state
     */
    suspend fun logout()
    
    /**
     * Check if user is currently logged in
     * @return true if user is authenticated, false otherwise
     */
    suspend fun isLoggedIn(): Boolean
    
    /**
     * Get current authenticated user
     * @return User if logged in, null otherwise
     */
    suspend fun getCurrentUser(): User?
}

