package com.mobizonetech.aeon_wallet_cursor.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ViewModel.launchSafe(
    dispatcherProvider: DispatcherProvider,
    block: suspend CoroutineScope.() -> Unit,
    onError: (Throwable) -> Unit = {}
) {
    viewModelScope.launch(dispatcherProvider.io) {
        try {
            block()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Coroutine", "Error in coroutine", e)
            onError(e)
        }
    }
}


