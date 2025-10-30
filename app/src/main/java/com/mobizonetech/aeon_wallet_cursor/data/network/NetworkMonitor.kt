package com.mobizonetech.aeon_wallet_cursor.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkMonitor {
    val isOnline: StateFlow<Boolean>
    fun isConnected(): Boolean
}

@Singleton
class NetworkMonitorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkMonitor {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOnline = MutableStateFlow(checkConnection())
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) { _isOnline.value = true }
        override fun onLost(network: Network) { _isOnline.value = checkConnection() }
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            _isOnline.value = hasInternet(networkCapabilities)
        }
    }

    init {
        runCatching { connectivityManager.registerDefaultNetworkCallback(callback) }
    }

    override fun isConnected(): Boolean = checkConnection()

    private fun checkConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
        return hasInternet(caps)
    }

    private fun hasInternet(caps: NetworkCapabilities): Boolean {
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}


