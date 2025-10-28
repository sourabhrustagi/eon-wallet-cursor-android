package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.repository.WelcomeRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import javax.inject.Inject

/**
 * Use case for retrieving welcome slides data
 * This use case provides the initial onboarding slides for the app
 * 
 * Follows Clean Architecture principles by:
 * - Separating business logic from data sources
 * - Using repository pattern for data access
 * - Returning Result type for consistent error handling
 * 
 * @param repository Repository for accessing welcome slide data
 */
class GetWelcomeSlidesUseCase @Inject constructor(
    private val repository: WelcomeRepository
) {
    /**
     * Executes the use case to retrieve welcome slides
     * @return Result containing list of welcome slides or error
     */
    suspend operator fun invoke(): Result<List<WelcomeSlide>> {
        Logger.d(TAG, "Executing GetWelcomeSlidesUseCase")
        return repository.getWelcomeSlides()
    }

    companion object {
        private const val TAG = "GetWelcomeSlidesUseCase"
    }
}

