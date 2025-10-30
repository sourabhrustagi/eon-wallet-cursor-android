package com.mobizonetech.aeon_wallet_cursor.di

import com.mobizonetech.aeon_wallet_cursor.BuildConfig
import com.mobizonetech.aeon_wallet_cursor.data.analytics.Analytics
import com.mobizonetech.aeon_wallet_cursor.data.analytics.MockAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for Analytics dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    /**
     * Bind MockAnalytics as Analytics implementation
     * 
     * In production, this can be replaced with:
     * - Firebase Analytics
     * - Mixpanel
     * - Amplitude
     * - Custom analytics service
     */
    @Binds
    @Singleton
    abstract fun bindAnalytics(
        mockAnalytics: MockAnalytics
    ): Analytics
}

/**
 * Additional provides for analytics configuration
 */
@Module
@InstallIn(SingletonComponent::class)
object AnalyticsConfigModule {
    
    /**
     * Provide analytics enabled flag based on build type
     */
    @Provides
    @Singleton
    fun provideAnalyticsEnabled(): Boolean {
        // Enable analytics in debug builds for development
        // Can be controlled via remote config in production
        return BuildConfig.DEBUG
    }
}

