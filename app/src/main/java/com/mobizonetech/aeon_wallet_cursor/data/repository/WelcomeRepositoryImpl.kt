package com.mobizonetech.aeon_wallet_cursor.data.repository

import android.content.Context
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.domain.repository.WelcomeRepository
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of WelcomeRepository
 * Retrieves welcome slides from local resources
 */
class WelcomeRepositoryImpl @Inject constructor(
    private val context: Context
) : WelcomeRepository {

    override suspend fun getWelcomeSlides(): Result<List<WelcomeSlide>> = withContext(Dispatchers.IO) {
        try {
            Logger.d(TAG, "Fetching welcome slides")
            val resources = context.resources
            
            val slides = listOf(
                WelcomeSlide(
                    id = 0,
                    title = resources.getString(R.string.slide_0_title),
                    description = resources.getString(R.string.slide_0_description),
                    icon = resources.getString(R.string.slide_0_icon),
                    iconBackgroundColor = 0xFF6200EE,
                    features = listOf(
                        resources.getString(R.string.slide_0_feature_0),
                        resources.getString(R.string.slide_0_feature_1),
                        resources.getString(R.string.slide_0_feature_2)
                    )
                ),
                WelcomeSlide(
                    id = 1,
                    title = resources.getString(R.string.slide_1_title),
                    description = resources.getString(R.string.slide_1_description),
                    icon = resources.getString(R.string.slide_1_icon),
                    iconBackgroundColor = 0xFFB00020,
                    features = listOf(
                        resources.getString(R.string.slide_1_feature_0),
                        resources.getString(R.string.slide_1_feature_1),
                        resources.getString(R.string.slide_1_feature_2)
                    )
                ),
                WelcomeSlide(
                    id = 2,
                    title = resources.getString(R.string.slide_2_title),
                    description = resources.getString(R.string.slide_2_description),
                    icon = resources.getString(R.string.slide_2_icon),
                    iconBackgroundColor = 0xFF03DAC6,
                    features = listOf(
                        resources.getString(R.string.slide_2_feature_0),
                        resources.getString(R.string.slide_2_feature_1),
                        resources.getString(R.string.slide_2_feature_2)
                    )
                ),
                WelcomeSlide(
                    id = 3,
                    title = resources.getString(R.string.slide_3_title),
                    description = resources.getString(R.string.slide_3_description),
                    icon = resources.getString(R.string.slide_3_icon),
                    iconBackgroundColor = 0xFF018786,
                    features = listOf(
                        resources.getString(R.string.slide_3_feature_0),
                        resources.getString(R.string.slide_3_feature_1),
                        resources.getString(R.string.slide_3_feature_2)
                    )
                ),
                WelcomeSlide(
                    id = 4,
                    title = resources.getString(R.string.slide_4_title),
                    description = resources.getString(R.string.slide_4_description),
                    icon = resources.getString(R.string.slide_4_icon),
                    iconBackgroundColor = 0xFF6200EE,
                    features = listOf(
                        resources.getString(R.string.slide_4_feature_0),
                        resources.getString(R.string.slide_4_feature_1),
                        resources.getString(R.string.slide_4_feature_2)
                    )
                )
            )
            
            Logger.d(TAG, "Successfully fetched ${slides.size} welcome slides")
            Result.Success(slides)
        } catch (e: Exception) {
            Logger.e(TAG, "Error fetching welcome slides", e)
            Result.Error(
                message = e.message ?: "Unknown error occurred while loading slides",
                throwable = e
            )
        }
    }

    companion object {
        private const val TAG = "WelcomeRepository"
    }
}

