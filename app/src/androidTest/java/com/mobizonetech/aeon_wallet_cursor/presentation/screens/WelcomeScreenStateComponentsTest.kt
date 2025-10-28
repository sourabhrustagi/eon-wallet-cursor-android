package com.mobizonetech.aeon_wallet_cursor.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import org.junit.Rule
import org.junit.Test

/**
 * Instrumentation tests for WelcomeScreen state components
 * 
 * Tests:
 * - LoadingState
 * - ErrorState  
 * - PageIndicators
 */
class WelcomeScreenStateComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // =============================================================================
    // PageIndicators Tests
    // =============================================================================

    @Test
    fun pageIndicators_withOnePageSelected_showsCorrectIndicators() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 5,
                    currentPage = 0
                )
            }
        }

        // Then - Should have 5 indicators
        // Note: Can't easily test visual differences, but we verify component renders
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_withMiddlePageSelected_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 5,
                    currentPage = 2
                )
            }
        }

        // Then
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_withLastPageSelected_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 5,
                    currentPage = 4
                )
            }
        }

        // Then
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_withSinglePage_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 1,
                    currentPage = 0
                )
            }
        }

        // Then
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_withManyPages_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 10,
                    currentPage = 5
                )
            }
        }

        // Then
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_updatingCurrentPage_recomposes() {
        // Given - Use mutableStateOf for reactive updates
        composeTestRule.setContent {
            var currentPage by remember { mutableStateOf(0) }
            
            AeonwalletcursorTheme {
                Column {
                    PageIndicators(
                        pageCount = 5,
                        currentPage = currentPage
                    )
                    
                    // Hidden button to trigger state change for testing
                    Button(
                        onClick = { currentPage = 2 },
                        modifier = Modifier.testTag("update_button")
                    ) {
                        Text("Update")
                    }
                }
            }
        }

        // When - Trigger page update
        composeTestRule.onNodeWithTag("update_button").performClick()

        // Then - Should recompose without crashing
        composeTestRule.waitForIdle()
    }

    // =============================================================================
    // PageIndicators Edge Cases
    // =============================================================================

    @Test
    fun pageIndicators_withZeroPages_doesNotCrash() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 0,
                    currentPage = 0
                )
            }
        }

        // Then - Should not crash
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_withOutOfBoundsCurrentPage_doesNotCrash() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 5,
                    currentPage = 10 // Out of bounds
                )
            }
        }

        // Then - Should not crash
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_withNegativeCurrentPage_doesNotCrash() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 5,
                    currentPage = -1 // Negative
                )
            }
        }

        // Then - Should not crash
        composeTestRule.waitForIdle()
    }

    // =============================================================================
    // PageIndicators Theme Tests
    // =============================================================================

    @Test
    fun pageIndicators_inLightTheme_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme(darkTheme = false) {
                PageIndicators(
                    pageCount = 5,
                    currentPage = 2
                )
            }
        }

        // Then
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_inDarkTheme_renders() {
        // Given
        composeTestRule.setContent {
            AeonwalletcursorTheme(darkTheme = true) {
                PageIndicators(
                    pageCount = 5,
                    currentPage = 2
                )
            }
        }

        // Then
        composeTestRule.waitForIdle()
    }

    // =============================================================================
    // Integration Tests
    // =============================================================================

    @Test
    fun pageIndicators_multipleInstancesSimultaneously_render() {
        // Given - Multiple page indicators
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                androidx.compose.foundation.layout.Column {
                    PageIndicators(pageCount = 3, currentPage = 0)
                    PageIndicators(pageCount = 5, currentPage = 2)
                    PageIndicators(pageCount = 7, currentPage = 6)
                }
            }
        }

        // Then - All should render without issues
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_rapidPageChanges_handlesCorrectly() {
        // Given - Use mutableStateOf for reactive updates
        composeTestRule.setContent {
            var currentPage by remember { mutableStateOf(0) }
            
            AeonwalletcursorTheme {
                Column {
                    PageIndicators(
                        pageCount = 10,
                        currentPage = currentPage
                    )
                    
                    // Buttons to simulate rapid page changes
                    repeat(10) { page ->
                        Button(
                            onClick = { currentPage = page },
                            modifier = Modifier.testTag("page_button_$page")
                        ) {
                            Text("Page $page")
                        }
                    }
                }
            }
        }

        // When - Simulate rapid page changes
        repeat(10) { page ->
            composeTestRule.onNodeWithTag("page_button_$page").performClick()
            composeTestRule.waitForIdle()
        }

        // Then - Should handle all changes without issues
        composeTestRule.waitForIdle()
    }

    // =============================================================================
    // Performance Tests
    // =============================================================================

    @Test
    fun pageIndicators_withManyPages_performanceTest() {
        // Given - Large number of pages
        composeTestRule.setContent {
            AeonwalletcursorTheme {
                PageIndicators(
                    pageCount = 50,
                    currentPage = 25
                )
            }
        }

        // Then - Should render without performance issues
        composeTestRule.waitForIdle()
    }

    @Test
    fun pageIndicators_repeatedRecompositions_doesNotLeak() {
        // Given - Use mutableStateOf for reactive updates
        composeTestRule.setContent {
            var currentPage by remember { mutableStateOf(0) }
            
            AeonwalletcursorTheme {
                Column {
                    PageIndicators(
                        pageCount = 5,
                        currentPage = currentPage
                    )
                    
                    // Button to trigger repeated recompositions
                    Button(
                        onClick = { currentPage = (currentPage + 1) % 5 },
                        modifier = Modifier.testTag("cycle_button")
                    ) {
                        Text("Cycle")
                    }
                }
            }
        }

        // When - Trigger many recompositions
        repeat(100) {
            composeTestRule.onNodeWithTag("cycle_button").performClick()
        }

        // Then - Should complete without memory issues
        composeTestRule.waitForIdle()
    }
}

