package com.mobizonetech.aeon_wallet_cursor.data.remote.api

import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.PromotionsResponse
import retrofit2.Response
import retrofit2.http.GET

interface PromotionsApiService {
    @GET("promotions")
    suspend fun getPromotions(): Response<PromotionsResponse>
}


