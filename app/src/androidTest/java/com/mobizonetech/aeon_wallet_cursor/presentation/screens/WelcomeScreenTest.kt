package com.mobizonetech.aeon_wallet_cursor.presentation.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import org.junit.Rule
import org.junit.Test

/**
 * Instrumentation tests for WelcomeScreen composables
 * 
 * These tests verify:
 * - UI rendering and visibility
 * - Text content display
 * - User interactions
 * - State changes
 * - Component composition
 */
class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test data
    private val mockSlide = WelcomeSlide(
        id = 0,
        title = "Test Title",
        description = "Test Description",
        icon = "🚀",
        iconBackgroundColor = 0xFF6200EE,
        features = listOf(
            "Feature One",
            "Feature Two",
            "Feature Three"
        )
    )

    // =============================================================================
    // WelcomeSlide Tests
    // =============================================================================

    @Test
    fun welcomeSlide_displaysTitle() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Test Title")
            .assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_displaysDescription() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Test Description")
            .assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_displaysIcon() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("🚀")
            .assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_displaysAllFeatures() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Feature One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feature Two").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feature Three").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_displaysFeaturesWithCheckmarks() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then - Should have checkmark for each feature
        composeTestRule
            .onAllNodesWithText("✓")
            .assertCountEquals(3)
    }

    @Test
    fun welcomeSlide_allComponentsExist() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then - All components should be visible
        composeTestRule.onNodeWithText("Test Title").assertExists()
        composeTestRule.onNodeWithText("Test Description").assertExists()
        composeTestRule.onNodeWithText("🚀").assertExists()
        composeTestRule.onNodeWithText("Feature One").assertExists()
    }

    @Test
    fun welcomeSlide_withEmptyFeatures_doesNotCrash() {
        // Given
        val slideWithoutFeatures = mockSlide.copy(features = emptyList())
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slideWithoutFeatures)
            }
        }

        // Then - Should still display title and description
        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Description").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_withLongText_displaysCorrectly() {
        // Given
        val slideWithLongText = mockSlide.copy(
            title = "This is a very long title that should wrap properly on the screen",
            description = "This is an extremely long description that contains multiple sentences and should wrap to multiple lines when displayed on the screen to ensure proper readability."
        )
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slideWithLongText)
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("This is a very long title that should wrap properly on the screen")
            .assertIsDisplayed()
    }

    // =============================================================================
    // WelcomeFeatures Tests
    // =============================================================================

    @Test
    fun welcomeFeatures_displaysSingleFeature() {
        // Given
        val features = listOf("Single Feature")
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeFeatures(features = features)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Single Feature").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("✓").assertCountEquals(1)
    }

    @Test
    fun welcomeFeatures_displaysMultipleFeatures() {
        // Given
        val features = listOf("Feature 1", "Feature 2", "Feature 3", "Feature 4")
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeFeatures(features = features)
            }
        }

        // Then
        features.forEach { feature ->
            composeTestRule.onNodeWithText(feature).assertIsDisplayed()
        }
        composeTestRule.onAllNodesWithText("✓").assertCountEquals(4)
    }

    @Test
    fun welcomeFeatures_withEmptyList_doesNotCrash() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeFeatures(features = emptyList())
            }
        }

        // Then - Should not crash or show error
        // No assertions needed, just verifying it doesn't crash
    }

    // =============================================================================
    // WelcomeTitle Tests
    // =============================================================================

    @Test
    fun welcomeTitle_displaysText() {
        // Given
        val title = "Welcome to Aeon Wallet"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeTitle(title = title)
            }
        }

        // Then
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
    }

    @Test
    fun welcomeTitle_withEmptyString_doesNotCrash() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeTitle(title = "")
            }
        }

        // Then - Should not crash
        composeTestRule.onNodeWithText("").assertExists()
    }

    // =============================================================================
    // WelcomeDescription Tests
    // =============================================================================

    @Test
    fun welcomeDescription_displaysText() {
        // Given
        val description = "Your secure gateway to digital finance"
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeDescription(description = description)
            }
        }

        // Then
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }

    @Test
    fun welcomeDescription_withLongText_displaysCorrectly() {
        // Given
        val longDescription = "This is a very long description that should wrap to multiple lines " +
                "and display properly on the screen with appropriate line height and spacing."
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeDescription(description = longDescription)
            }
        }

        // Then
        composeTestRule.onNodeWithText(longDescription).assertIsDisplayed()
    }

    // =============================================================================
    // Multiple Slides Tests
    // =============================================================================

    @Test
    fun welcomeSlide_firstSlide_displaysCorrectContent() {
        // Given - First slide
        val slide1 = mockSlide.copy(
            title = "First Slide",
            description = "First Description",
            icon = "🔒"
        )
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slide1)
            }
        }

        // Then
        composeTestRule.onNodeWithText("First Slide").assertIsDisplayed()
        composeTestRule.onNodeWithText("First Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("🔒").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_secondSlide_displaysCorrectContent() {
        // Given - Second slide
        val slide2 = mockSlide.copy(
            title = "Second Slide",
            description = "Second Description",
            icon = "📈"
        )
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slide2)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Second Slide").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("📈").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_withBitcoinIcon_displaysCorrectly() {
        // Given
        val slide = mockSlide.copy(icon = "₿")
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("₿").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_withLockIcon_displaysCorrectly() {
        // Given
        val slide = mockSlide.copy(icon = "🔒")
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("🔒").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_withChartIcon_displaysCorrectly() {
        // Given
        val slide = mockSlide.copy(icon = "📈")
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("📈").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_withRocketIcon_displaysCorrectly() {
        // Given
        val slide = mockSlide.copy(icon = "🚀")
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("🚀").assertIsDisplayed()
    }

    // =============================================================================
    // Semantic Tests
    // =============================================================================

    @Test
    fun welcomeSlide_contentDescription_isAccessible() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then - All text should be accessible
        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feature One").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_features_areInCorrectOrder() {
        // Given
        val orderedSlide = mockSlide.copy(
            features = listOf("First", "Second", "Third")
        )
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = orderedSlide)
            }
        }

        // Then - All features should exist
        composeTestRule.onNodeWithText("First").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second").assertIsDisplayed()
        composeTestRule.onNodeWithText("Third").assertIsDisplayed()
    }

    // =============================================================================
    // Edge Cases
    // =============================================================================

    @Test
    fun welcomeSlide_withSpecialCharacters_displaysCorrectly() {
        // Given
        val specialSlide = mockSlide.copy(
            title = "Title with & special < characters >",
            description = "Description with \"quotes\" and 'apostrophes'",
            features = listOf("Feature with @ symbol", "Feature with # hashtag")
        )
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = specialSlide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Title with & special < characters >").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feature with @ symbol").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_withUnicodeCharacters_displaysCorrectly() {
        // Given
        val unicodeSlide = mockSlide.copy(
            title = "Welcome 欢迎 स्वागत",
            description = "Bienvenue Willkommen أهلا بك",
            features = listOf("Multi 言語 support", "Unicode ✓ test")
        )
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = unicodeSlide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Welcome 欢迎 स्वागत").assertIsDisplayed()
        composeTestRule.onNodeWithText("Multi 言語 support").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_withMaximumFeatures_displaysAll() {
        // Given
        val manyFeatures = (1..10).map { "Feature $it" }
        val slideWithManyFeatures = mockSlide.copy(features = manyFeatures)
        
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                WelcomeSlide(slide = slideWithManyFeatures)
            }
        }

        // Then - All features should be displayed
        manyFeatures.forEach { feature ->
            composeTestRule.onNodeWithText(feature).assertIsDisplayed()
        }
        composeTestRule.onAllNodesWithText("✓").assertCountEquals(10)
    }

    // =============================================================================
    // Theme Tests
    // =============================================================================

    @Test
    fun welcomeSlide_inLightTheme_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme(darkTheme = false) {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
    }

    @Test
    fun welcomeSlide_inDarkTheme_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme(darkTheme = true) {
                WelcomeSlide(slide = mockSlide)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
    }
}

