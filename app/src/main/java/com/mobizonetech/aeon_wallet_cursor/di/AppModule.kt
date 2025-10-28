package com.mobizonetech.aeon_wallet_cursor.di

import android.content.Context
import com.mobizonetech.aeon_wallet_cursor.data.repository.WelcomeRepositoryImpl
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
     * @param context Application context
     * @return WelcomeRepository instance
     */
    @Provides
    @Singleton
    fun provideWelcomeRepository(
        @ApplicationContext context: Context
    ): WelcomeRepository {
        return WelcomeRepositoryImpl(context)
    }
}