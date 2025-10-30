package com.mobizonetech.aeon_wallet_cursor.di

import android.content.Context
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.AppSettingsApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.WelcomeApiService
import com.mobizonetech.aeon_wallet_cursor.data.repository.AppSettingsRepositoryImpl
import com.mobizonetech.aeon_wallet_cursor.data.repository.WelcomeRepositoryApiImpl
import com.mobizonetech.aeon_wallet_cursor.domain.repository.AppSettingsRepository
import com.mobizonetech.aeon_wallet_cursor.domain.repository.WelcomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing app-wide dependencies
 * Installed in SingletonComponent for application-scoped dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides application context
     * @param context Application context injected by Hilt
     * @return Application context
     */
    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ): Context = context

    /**
     * Provides WelcomeRepository implementation
     * 
     * Now using API-based implementation with mock interceptor
     * 
     * @param apiService API service for fetching slides
     * @return WelcomeRepository instance
     */
    @Provides
    @Singleton
    fun provideWelcomeRepository(
        apiService: WelcomeApiService
    ): WelcomeRepository {
        return WelcomeRepositoryApiImpl(apiService)
    }

    /**
     * Provides AppSettingsRepository implementation
     * 
     * API-based implementation with mock interceptor
     * 
     * @param apiService API service for fetching app settings
     * @return AppSettingsRepository instance
     */
    @Provides
    @Singleton
    fun provideAppSettingsRepository(
        apiService: AppSettingsApiService
    ): AppSettingsRepository {
        return AppSettingsRepositoryImpl(apiService)
    }
}