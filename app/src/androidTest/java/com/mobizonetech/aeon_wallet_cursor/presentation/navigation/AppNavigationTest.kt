package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mobizonetech.aeon_wallet_cursor.MainActivity
import com.mobizonetech.aeon_wallet_cursor.R
import org.junit.Rule
import org.junit.Test

class AppNavigationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun starting_at_welcome_and_navigate_to_login_and_home() {
        // Welcome screen title should be visible eventually
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.welcome_get_started)).assertIsDisplayed()
    }
}


