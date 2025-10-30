package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.mobizonetech.aeon_wallet_cursor.data.remote.api.PromotionsApiService
import com.mobizonetech.aeon_wallet_cursor.data.remote.retry.RetryPolicy
import com.mobizonetech.aeon_wallet_cursor.domain.repository.PromotionsRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PromotionsRepositoryImpl @Inject constructor(
    private val api: PromotionsApiService
) : PromotionsRepository {
    override suspend fun syncPromotions(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Logger.d(TAG, "Syncing promotions")
            val response = RetryPolicy.executeWithRetry {
                api.getPromotions()
            }
            if (response.isSuccessful) {
                // In a real app, store to DB/cache here
                Result.Success(Unit)
            } else {
                Result.Error("HTTP ${'$'}{response.code()}")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", e)
        }
    }

    companion object { private const val TAG = "PromotionsRepo" }
}


