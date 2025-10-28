package com.mobizonetech.aeon_wallet_cursor.domain.usecase

import android.content.Context
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide

class GetWelcomeSlidesUseCase(
    private val context: Context
) {
    operator fun invoke(): List<WelcomeSlide> {
        val strings = context.resources
        
        return listOf(
            WelcomeSlide(
                id = 0,
                title = strings.getString(R.string.slide_0_title),
                description = strings.getString(R.string.slide_0_description),
                icon = strings.getString(R.string.slide_0_icon),
                iconBackgroundColor = 0xFF6200EE,
                features = listOf(
                    strings.getString(R.string.slide_0_feature_0),
                    strings.getString(R.string.slide_0_feature_1),
                    strings.getString(R.string.slide_0_feature_2)
                )
            ),
            WelcomeSlide(
                id = 1,
                title = strings.getString(R.string.slide_1_title),
                description = strings.getString(R.string.slide_1_description),
                icon = strings.getString(R.string.slide_1_icon),
                iconBackgroundColor = 0xFFB00020,
                features = listOf(
                    strings.getString(R.string.slide_1_feature_0),
                    strings.getString(R.string.slide_1_feature_1),
                    strings.getString(R.string.slide_1_feature_2)
                )
            ),
            WelcomeSlide(
                id = 2,
                title = strings.getString(R.string.slide_2_title),
                description = strings.getString(R.string.slide_2_description),
                icon = strings.getString(R.string.slide_2_icon),
                iconBackgroundColor = 0xFF03DAC6,
                features = listOf(
                    strings.getString(R.string.slide_2_feature_0),
                    strings.getString(R.string.slide_2_feature_1),
                    strings.getString(R.string.slide_2_feature_2)
                )
            ),
            WelcomeSlide(
                id = 3,
                title = strings.getString(R.string.slide_3_title),
                description = strings.getString(R.string.slide_3_description),
                icon = strings.getString(R.string.slide_3_icon),
                iconBackgroundColor = 0xFF018786,
                features = listOf(
                    strings.getString(R.string.slide_3_feature_0),
                    strings.getString(R.string.slide_3_feature_1),
                    strings.getString(R.string.slide_3_feature_2)
                )
            ),
            WelcomeSlide(
                id = 4,
                title = strings.getString(R.string.slide_4_title),
                description = strings.getString(R.string.slide_4_description),
                icon = strings.getString(R.string.slide_4_icon),
                iconBackgroundColor = 0xFF6200EE,
                features = listOf(
                    strings.getString(R.string.slide_4_feature_0),
                    strings.getString(R.string.slide_4_feature_1),
                    strings.getString(R.string.slide_4_feature_2)
                )
            )
        )
    }
}

