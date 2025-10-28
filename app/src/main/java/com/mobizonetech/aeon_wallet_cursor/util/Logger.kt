package com.mobizonetech.aeon_wallet_cursor.util

import android.util.Log
import com.mobizonetech.aeon_wallet_cursor.BuildConfig

/**
 * Centralized logging utility for the app
 * Automatically disabled in release builds for security and performance
 */
object Logger {
    
    private const val DEFAULT_TAG = "AeonWallet"
    private val isLoggingEnabled = BuildConfig.DEBUG

    /**
     * Log a debug message
     * @param tag Tag for the log message
     * @param message Message to log
     */
    fun d(tag: String = DEFAULT_TAG, message: String) {
        if (isLoggingEnabled) {
            Log.d(tag, message)
        }
    }

    /**
     * Log an info message
     * @param tag Tag for the log message
     * @param message Message to log
     */
    fun i(tag: String = DEFAULT_TAG, message: String) {
        if (isLoggingEnabled) {
            Log.i(tag, message)
        }
    }

    /**
     * Log a warning message
     * @param tag Tag for the log message
     * @param message Message to log
     * @param throwable Optional throwable to log
     */
    fun w(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }

    /**
     * Log an error message
     * @param tag Tag for the log message
     * @param message Message to log
     * @param throwable Optional throwable to log
     */
    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    /**
     * Log a verbose message
     * @param tag Tag for the log message
     * @param message Message to log
     */
    fun v(tag: String = DEFAULT_TAG, message: String) {
        if (isLoggingEnabled) {
            Log.v(tag, message)
        }
    }

    /**
     * Log a WTF (What a Terrible Failure) message
     * @param tag Tag for the log message
     * @param message Message to log
     * @param throwable Optional throwable to log
     */
    fun wtf(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isLoggingEnabled) {
            if (throwable != null) {
                Log.wtf(tag, message, throwable)
            } else {
                Log.wtf(tag, message)
            }
        }
    }
}

