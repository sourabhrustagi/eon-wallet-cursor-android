package com.mobizonetech.aeon_wallet_cursor

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import javax.inject.Inject
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for AEON Wallet
 * 
 * This class is the entry point for the entire application.
 * It's annotated with @HiltAndroidApp to set up Hilt dependency injection.
 * 
 * Responsibilities:
 * - Initialize Hilt for dependency injection
 * - Perform one-time app initialization
 * - Configure app-wide settings
 * 
 * Best Practices:
 * - Keep onCreate() lightweight to avoid app startup delays
 * - Use background threads for heavy initialization
 * - Avoid storing mutable global state here
 */
@HiltAndroidApp
class AeonWalletApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory
    
    /**
     * Called when the application is created
     * Initializes app-wide configurations
     */
    override fun onCreate() {
        super.onCreate()
        Logger.i(TAG, "AEON Wallet Application initialized")
        
        // Additional initialization can be added here:
        // - Crash reporting setup (e.g., Firebase Crashlytics)
        // - Analytics initialization
        // - WorkManager setup
        // - Notification channels
        // - App preferences
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    companion object {
        private const val TAG = "AeonWalletApp"
    }
}

