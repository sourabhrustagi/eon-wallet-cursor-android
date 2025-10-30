package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.data.remote.api.WelcomeApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.mapper.WelcomeSlideMapper
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.repository.WelcomeRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * API-based implementation of WelcomeRepository
 * Fetches welcome slides from remote API
 * 
 * @param apiService Retrofit API service
 */
class WelcomeRepositoryApiImpl @Inject constructor(
    private val apiService: WelcomeApiService
) : WelcomeRepository {

    override suspend fun getWelcomeSlides(): Result<List<WelcomeSlide>> = withContext(Dispatchers.IO) {
        try {
            Logger.d(TAG, "Fetching welcome slides from API")
            
            val response = apiService.getWelcomeSlides()
            
            if (response.isSuccessful) {
                val body = response.body()
                
                if (body != null && body.success) {
                    val domainSlides = WelcomeSlideMapper.mapToDomainList(body.data)
                    Logger.d(TAG, "Successfully fetched ${domainSlides.size} slides from API")
                    Result.Success(domainSlides)
                } else {
                    val errorMessage = body?.message ?: "API returned unsuccessful response"
                    Logger.e(TAG, "API error: $errorMessage")
                    Result.Error(errorMessage)
                }
            } else {
                val errorMessage = "HTTP ${response.code()}: ${response.message()}"
                Logger.e(TAG, "API request failed: $errorMessage")
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Error fetching slides from API", e)
            Result.Error(
                message = e.message ?: "Unknown error occurred while loading slides from API",
                throwable = e
            )
        }
    }

    companion object {
        private const val TAG = "WelcomeRepositoryApi"
    }
}

