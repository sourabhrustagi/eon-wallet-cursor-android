package com.mobizonetech.aeon_wallet_cursor.domain.repository

import com.mobizonetech.aeon_wallet_cursor.domain.util.Result

interface PromotionsRepository {
    suspend fun syncPromotions(): Result<Unit>
}


