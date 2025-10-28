package com.mobizonetech.aeon_wallet_cursor.domain.repository

import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result

/**
 * Repository interface for welcome screen data
 * Provides abstraction layer between data sources and business logic
 */
interface WelcomeRepository {
    /**
     * Get welcome slides for onboarding
     * @return Result containing list of welcome slides or error
     */
    suspend fun getWelcomeSlides(): Result<List<WelcomeSlide>>
}

