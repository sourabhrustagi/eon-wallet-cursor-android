package com.mobizonetech.aeon_wallet_cursor.domain.util

/**
 * Sealed class representing the result of an operation
 * Used for consistent error handling across the app
 *
 * @param T The type of data returned on success
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents a failed operation with an error message
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : Result<Nothing>()
    
    /**
     * Represents a loading state
     */
    object Loading : Result<Nothing>()
}

/**
 * Extension function to check if result is success
 */
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

/**
 * Extension function to check if result is error
 */
fun <T> Result<T>.isError(): Boolean = this is Result.Error

/**
 * Extension function to check if result is loading
 */
fun <T> Result<T>.isLoading(): Boolean = this is Result.Loading

/**
 * Extension function to get data from success result
 */
fun <T> Result<T>.getData(): T? = (this as? Result.Success)?.data

/**
 * Extension function to get error message from error result
 */
fun <T> Result<T>.getErrorMessage(): String? = (this as? Result.Error)?.message

