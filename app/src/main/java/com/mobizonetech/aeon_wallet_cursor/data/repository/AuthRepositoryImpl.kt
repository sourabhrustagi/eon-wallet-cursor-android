package com.mobizonetech.aeon_wallet_cursor.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.AuthApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.DtoValidator
import com.mobizonetech.aeon_wallet_cursor.data.remote.mapper.LoginMapper
import com.mobizonetech.aeon_wallet_cursor.data.remote.retry.RetryPolicy
import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginRequest
import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginResult
import com.mobizonetech.aeon_wallet_cursor.domain.model.User
import com.mobizonetech.aeon_wallet_cursor.domain.repository.AuthRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import com.mobizonetech.aeon_wallet_cursor.util.PerformanceMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

/**
 * API-based implementation of AuthRepository
 * Handles user authentication with remote API and local storage
 * 
 * Features:
 * - Automatic retry on network errors
 * - Exponential backoff between retries
 * - Secure token storage in DataStore
 * - User session management
 * 
 * @param apiService Retrofit API service for authentication
 * @param context Application context for DataStore
 */
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val dataStore = context.authDataStore
    
    companion object {
        private const val TAG = "AuthRepository"
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_PHONE = stringPreferencesKey("user_phone")
        private val KEY_USER_TOKEN = stringPreferencesKey("user_token")
    }

    override suspend fun login(request: LoginRequest): Result<LoginResult> = withContext(Dispatchers.IO) {
        try {
            Logger.d(TAG, "Attempting login for: ${request.emailOrPhone.take(3)}***")
            
            // Execute API call with retry logic
            val response = RetryPolicy.executeWithRetry(
                maxRetries = 3,
                initialDelayMs = 1000L,
                maxDelayMs = 5000L
            ) {
                PerformanceMonitor.measure("login API") {
                    apiService.login(LoginMapper.toDto(request))
                }
            }
            
            if (response.isSuccessful) {
                val body = response.body()
                
                if (body != null && body.success && body.data != null) {
                    // Validate response data
                    DtoValidator.validateLoginResponse(body)
                    
                    val loginResult = LoginMapper.toDomain(body)
                    if (loginResult != null) {
                        // Save user data to DataStore
                        saveUser(loginResult.user)
                        
                        Logger.d(TAG, "Login successful for user: ${loginResult.user.name}")
                        Result.Success(loginResult)
                    } else {
                        Logger.e(TAG, "Failed to map login response to domain model")
                        Result.Error("Invalid response format")
                    }
                } else {
                    val errorMessage = body?.message ?: "Login failed"
                    Logger.e(TAG, "API error: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Invalid email or password"
                    403 -> "Account is locked. Please contact support"
                    404 -> "User not found"
                    else -> "HTTP ${response.code()}: ${response.message()}"
                }
                Logger.e(TAG, "Login request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Error during login after retries", e)
            Result.Error(
                message = e.message ?: "Unknown error occurred during login",
                throwable = e
            )
        }
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        try {
            Logger.d(TAG, "Logging out user")
            dataStore.edit { preferences ->
                preferences.clear()
            }
            Logger.d(TAG, "User logged out successfully")
        } catch (e: Exception) {
            Logger.e(TAG, "Error during logout", e)
        }
    }

    override suspend fun isLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        try {
            val token = dataStore.data.first()[KEY_USER_TOKEN]
            token != null && token.isNotBlank()
        } catch (e: Exception) {
            Logger.e(TAG, "Error checking login status", e)
            false
        }
    }

    override suspend fun getCurrentUser(): User? = withContext(Dispatchers.IO) {
        try {
            val preferences = dataStore.data.first()
            val id = preferences[KEY_USER_ID]
            val name = preferences[KEY_USER_NAME]
            val email = preferences[KEY_USER_EMAIL]
            val phone = preferences[KEY_USER_PHONE]
            val token = preferences[KEY_USER_TOKEN]
            
            if (id != null && name != null && email != null && token != null) {
                User(
                    id = id,
                    name = name,
                    email = email,
                    phone = phone,
                    token = token
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting current user", e)
            null
        }
    }
    
    /**
     * Save user data to DataStore
     */
    private suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = user.id
            preferences[KEY_USER_NAME] = user.name
            preferences[KEY_USER_EMAIL] = user.email
            preferences[KEY_USER_PHONE] = user.phone ?: ""
            preferences[KEY_USER_TOKEN] = user.token
        }
    }
}

