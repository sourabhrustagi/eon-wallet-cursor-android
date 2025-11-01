package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginRequest
import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginResult
import com.mobizonetech.aeon_wallet_cursor.domain.repository.AuthRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import javax.inject.Inject

/**
 * Use case for user login
 * 
 * Handles the business logic for authenticating users
 * Validates input and delegates to repository
 * 
 * @param authRepository Repository for authentication operations
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    companion object {
        private const val TAG = "LoginUseCase"
    }
    
    /**
     * Execute login with credentials
     * @param emailOrPhone Email or phone number
     * @param password User password
     * @return Result containing LoginResult on success or error message
     */
    suspend operator fun invoke(
        emailOrPhone: String,
        password: String
    ): Result<LoginResult> {
        Logger.d(TAG, "Login attempt for: ${emailOrPhone.take(3)}***")
        
        // Validate input
        if (emailOrPhone.isBlank()) {
            Logger.w(TAG, "Email/Phone is blank")
            return Result.Error("Email or phone number is required")
        }
        
        if (password.isBlank()) {
            Logger.w(TAG, "Password is blank")
            return Result.Error("Password is required")
        }
        
        if (password.length < 6) {
            Logger.w(TAG, "Password too short")
            return Result.Error("Password must be at least 6 characters")
        }
        
        // Create login request
        val request = LoginRequest(
            emailOrPhone = emailOrPhone.trim(),
            password = password
        )
        
        // Delegate to repository
        return authRepository.login(request)
    }
}


