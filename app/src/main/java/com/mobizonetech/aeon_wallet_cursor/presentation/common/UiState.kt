package com.mobizonetech.aeon_wallet_cursor.presentation.common

sealed interface UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val throwable: Throwable? = null, val canRetry: Boolean = true) : UiState<Nothing>
    object Loading : UiState<Nothing>
    object Empty : UiState<Nothing>
}


