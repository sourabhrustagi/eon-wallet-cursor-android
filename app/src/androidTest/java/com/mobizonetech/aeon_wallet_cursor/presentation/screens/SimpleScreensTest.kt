package com.mobizonetech.aeon_wallet_cursor.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class SimpleScreensTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun home_and_login_render() {
        composeRule.setContent { HomeScreen() }
        composeRule.onNodeWithText("Home").assertIsDisplayed()

        composeRule.setContent { LoginScreen() }
        composeRule.onNodeWithText("Login").assertIsDisplayed()
    }
}


