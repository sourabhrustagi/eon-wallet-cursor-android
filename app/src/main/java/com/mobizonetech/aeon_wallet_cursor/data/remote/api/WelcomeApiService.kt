package com.mobizonetech.aeon_wallet_cursor.data.remote.api

import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.WelcomeSlidesResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * API Service for Welcome/Onboarding endpoints
 */
interface WelcomeApiService {
    
    /**
     * Fetch welcome slides from API
     * 
     * @return Response containing list of welcome slides
     */
    @GET("api/v1/onboarding/slides")
    suspend fun getWelcomeSlides(): Response<WelcomeSlidesResponse>
}

