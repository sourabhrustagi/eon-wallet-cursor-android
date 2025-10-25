package com.mobizonetech.aeon_wallet_cursor.di

import android.content.Context
import com.mobizonetech.aeon_wallet_cursor.data.UnlockPreferences
import com.mobizonetech.aeon_wallet_cursor.data.UserPreferences
import com.mobizonetech.aeon_wallet_cursor.data.CardDataStore
import com.mobizonetech.aeon_wallet_cursor.data.LoanDataStore
import com.mobizonetech.aeon_wallet_cursor.data.repository.CardRepositoryImpl
import com.mobizonetech.aeon_wallet_cursor.data.repository.LoanRepositoryImpl
import com.mobizonetech.aeon_wallet_cursor.data.repository.UserRepositoryImpl
import com.mobizonetech.aeon_wallet_cursor.domain.repository.CardRepository
import com.mobizonetech.aeon_wallet_cursor.domain.repository.LoanRepository
import com.mobizonetech.aeon_wallet_cursor.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUnlockPreferences(@ApplicationContext context: Context): UnlockPreferences {
        return UnlockPreferences(context)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    fun provideCardDataStore(@ApplicationContext context: Context): CardDataStore {
        return CardDataStore(context)
    }

    @Provides
    @Singleton
    fun provideLoanDataStore(@ApplicationContext context: Context): LoanDataStore {
        return LoanDataStore(context)
    }

    @Provides
    @Singleton
    fun provideCardRepository(
        unlockPreferences: UnlockPreferences,
        cardDataStore: CardDataStore
    ): CardRepository {
        return CardRepositoryImpl(unlockPreferences, cardDataStore)
    }

    @Provides
    @Singleton
    fun provideLoanRepository(
        unlockPreferences: UnlockPreferences,
        loanDataStore: LoanDataStore
    ): LoanRepository {
        return LoanRepositoryImpl(unlockPreferences, loanDataStore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userPreferences: UserPreferences): UserRepository {
        return UserRepositoryImpl(userPreferences)
    }
}
