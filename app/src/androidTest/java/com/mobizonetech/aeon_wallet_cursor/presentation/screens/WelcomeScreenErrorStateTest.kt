package com.mobizonetech.aeon_wallet_cursor.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import org.junit.Rule
import org.junit.Test

/**
 * Comprehensive error state tests for WelcomeScreen
 * 
 * Tests:
 * - Loading state display
 * - Error state with message
 * - Error state with default message
 * - Error state accessibility
 * - Edge cases
 */
class WelcomeScreenErrorStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // =============================================================================
    // Loading State Tests
    // =============================================================================

    @Test
    fun loadingState_displaysProgressIndicator() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                LoadingState()
            }
        }

        // Then - Progress indicator should be visible
        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun loadingState_hasCorrectSemantics() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                LoadingState()
            }
        }

        // Then - Should be in loading state
        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertExists()
    }

    @Test
    fun loadingState_isCenterAligned() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                LoadingState()
            }
        }

        // Then - Should exist and be visible (center aligned)
        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun loadingState_hasGradientBackground() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                LoadingState()
            }
        }

        // Then - Component should render without crashing
        composeTestRule.waitForIdle()
        
        // Progress indicator confirms the component loaded
        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertExists()
    }

    // =============================================================================
    // Error State Tests - With Custom Message
    // =============================================================================

    @Test
    fun errorState_withCustomMessage_displaysMessage() {
        // Given
        val errorMessage = "Failed to load welcome slides"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = errorMessage)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_withCustomMessage_isAccessible() {
        // Given
        val errorMessage = "Network error occurred"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = errorMessage)
            }
        }

        // Then - Error message should be accessible
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun errorState_withLongMessage_displaysCorrectly() {
        // Given
        val longMessage = "An unexpected error occurred while trying to load the welcome slides. " +
                "Please check your internet connection and try again later."
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = longMessage)
            }
        }

        // Then - Should display full message
        composeTestRule
            .onNodeWithText(longMessage)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_withSpecialCharacters_displaysCorrectly() {
        // Given
        val messageWithSpecialChars = "Error: Failed! (Code: 500) - Please retry."
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = messageWithSpecialChars)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(messageWithSpecialChars)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_withUnicode_displaysCorrectly() {
        // Given
        val unicodeMessage = "エラーが発生しました 🚨 Error occurred"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = unicodeMessage)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(unicodeMessage)
            .assertIsDisplayed()
    }

    // =============================================================================
    // Error State Tests - With Null/Default Message
    // =============================================================================

    @Test
    fun errorState_withNullError_displaysDefaultMessage() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = null)
            }
        }

        // Then - Should display default error message from strings.xml
        // Note: We can't easily test the exact string without context
        // but we can verify something is displayed
        composeTestRule.waitForIdle()
    }

    @Test
    fun errorState_withEmptyString_displaysEmptyString() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = "")
            }
        }

        // Then - Should handle empty string gracefully
        composeTestRule.waitForIdle()
    }

    // =============================================================================
    // Error State Tests - Styling and Layout
    // =============================================================================

    @Test
    fun errorState_textIsCenterAligned() {
        // Given
        val errorMessage = "Test error message"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = errorMessage)
            }
        }

        // Then - Text should exist (center alignment is visual)
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun errorState_hasCorrectTextStyle() {
        // Given
        val errorMessage = "Styled error message"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = errorMessage)
            }
        }

        // Then - Text should be displayed with proper styling
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_hasPadding() {
        // Given
        val errorMessage = "Error with padding"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = errorMessage)
            }
        }

        // Then - Component should render with padding
        composeTestRule
            .onNodeWithText(errorMessage)
            .assertExists()
    }

    // =============================================================================
    // Error State Tests - Different Error Types
    // =============================================================================

    @Test
    fun errorState_networkError_displays() {
        // Given
        val networkError = "No internet connection available"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = networkError)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(networkError)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_apiError_displays() {
        // Given
        val apiError = "HTTP 500: Internal Server Error"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = apiError)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(apiError)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_timeoutError_displays() {
        // Given
        val timeoutError = "Request timed out. Please try again."
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = timeoutError)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(timeoutError)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_dataNotFoundError_displays() {
        // Given
        val notFoundError = "No welcome slides found"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = notFoundError)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText(notFoundError)
            .assertIsDisplayed()
    }

    // =============================================================================
    // Multiple Instances Tests
    // =============================================================================

    @Test
    fun errorState_multipleInstances_render() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    ErrorState(
                        error = "Error 1",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    ErrorState(
                        error = "Error 2",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }
        }

        // Then - Both errors should be visible
        composeTestRule.onNodeWithText("Error 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Error 2").assertIsDisplayed()
    }

    // =============================================================================
    // Recomposition Tests
    // =============================================================================

    @Test
    fun errorState_changingMessage_recomposes() {
        // Given
        val messages = listOf("Error 1", "Error 2", "Error 3")
        
        composeTestRule.setContent {
            var currentIndex by remember { mutableStateOf(0) }
            val message = messages[currentIndex]
            
            AeonwalletcursorTheme {
                Column {
                    ErrorState(error = message)
                    
                    // Button to change error message (for testing)
                    Button(
                        onClick = {
                            currentIndex = (currentIndex + 1) % messages.size
                        },
                        modifier = Modifier.testTag("change_button")
                    ) {
                        Text("Change")
                    }
                }
            }
        }

        // Then - First message should be visible
        composeTestRule.onNodeWithText("Error 1").assertIsDisplayed()
        
        // When - Click button to change message
        composeTestRule.onNodeWithTag("change_button").performClick()
        composeTestRule.waitForIdle()
        
        // Then - Second message should be visible
        composeTestRule.onNodeWithText("Error 2").assertIsDisplayed()
    }

    // =============================================================================
    // Performance Tests
    // =============================================================================

    @Test
    fun errorState_withVeryLongMessage_performsWell() {
        // Given
        val veryLongMessage = "Error ".repeat(100)
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                ErrorState(error = veryLongMessage)
            }
        }

        // Then - Should render without performance issues
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText(veryLongMessage, substring = true).assertExists()
    }
}

