package com.mobizonetech.aeon_wallet_cursor.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetWelcomeSlidesUseCase(
        @ApplicationContext context: Context
    ): GetWelcomeSlidesUseCase {
        return GetWelcomeSlidesUseCase(context)
    }
}

