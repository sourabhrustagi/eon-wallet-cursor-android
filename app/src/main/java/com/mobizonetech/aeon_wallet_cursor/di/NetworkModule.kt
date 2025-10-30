package com.mobizonetech.aeon_wallet_cursor.di

import com.mobizonetech.aeon_wallet_cursor.BuildConfig
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.WelcomeApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.interceptor.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger Hilt module for network dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Base URL for API
     * In production, this would come from BuildConfig or environment
     */
    private const val BASE_URL = "https://api.aeonwallet.com/"

    /**
     * Provide MockInterceptor
     * 
     * Enable in debug builds for development
     * Disable in release builds to use real API
     */
    @Provides
    @Singleton
    fun provideMockInterceptor(): MockInterceptor {
        return MockInterceptor(enabled = BuildConfig.DEBUG)
    }

    /**
     * Provide HttpLoggingInterceptor for debugging
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * Provide OkHttpClient with interceptors
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        mockInterceptor: MockInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(mockInterceptor) // Add mock interceptor first
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provide Retrofit instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provide WelcomeApiService
     */
    @Provides
    @Singleton
    fun provideWelcomeApiService(retrofit: Retrofit): WelcomeApiService {
        return retrofit.create(WelcomeApiService::class.java)
    }

    /**
     * Provide AppSettingsApiService
     */
    @Provides
    @Singleton
    fun provideAppSettingsApiService(retrofit: Retrofit): com.mobizonetech.aeon_wallet_cursor.data.remote.api.AppSettingsApiService {
        return retrofit.create(com.mobizonetech.aeon_wallet_cursor.data.remote.api.AppSettingsApiService::class.java)
    }
}

