package com.mobizonetech.aeon_wallet_cursor.presentation.navigation

import androidx.compose.ui.test.*
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mobizonetech.aeon_wallet_cursor.AeonWalletApplication
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.common.MainScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Navigation testing utilities for AEON Wallet
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AeonWalletNavigationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    private lateinit var navController: TestNavHostController
    
    @Before
    fun setup() {
        hiltRule.inject()
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    }
    
    @Test
    fun testNavigationToHomeScreen() {
        // Test navigation to home screen
        navController.setCurrentDestination(AeonWalletDestinations.Home.route)
        assert(navController.currentDestination?.route == AeonWalletDestinations.Home.route)
    }
    
    @Test
    fun testNavigationToCardDetail() {
        // Test navigation to card detail with parameter
        val cardId = "card_1"
        navController.navigate(AeonWalletDestinations.CardDetail.createRoute(cardId))
        
        assert(navController.currentDestination?.route == AeonWalletDestinations.CardDetail.route)
        assert(navController.currentBackStackEntry?.arguments?.getString("cardId") == cardId)
    }
    
    @Test
    fun testNavigationToLoanApplication() {
        // Test navigation to loan application with parameter
        val loanId = "personal_loan"
        navController.navigate(AeonWalletDestinations.LoanApplication.createRoute(loanId))
        
        assert(navController.currentDestination?.route == AeonWalletDestinations.LoanApplication.route)
        assert(navController.currentBackStackEntry?.arguments?.getString("loanId") == loanId)
    }
    
    @Test
    fun testBackNavigation() {
        // Test back navigation
        navController.navigate(AeonWalletDestinations.Home.route)
        navController.navigate(AeonWalletDestinations.Loans.route)
        
        navController.popBackStack()
        assert(navController.currentDestination?.route == AeonWalletDestinations.Home.route)
    }
    
    @Test
    fun testNavigationWithArguments() {
        // Test navigation with multiple arguments
        val testCases = listOf(
            Triple(AeonWalletDestinations.CardDetail, "cardId", "card_1"),
            Triple(AeonWalletDestinations.LoanApplication, "loanId", "personal_loan"),
            Triple(AeonWalletDestinations.UtilityPayment, "billId", "electricity"),
            Triple(AeonWalletDestinations.Donation, "organizationId", "charity_1")
        )
        
        testCases.forEach { (destination, argName, argValue) ->
            navController.navigate(destination.createRoute(argValue))
            assert(navController.currentBackStackEntry?.arguments?.getString(argName) == argValue)
        }
    }
}

/**
 * Navigation test utilities for Compose UI tests
 */
object NavigationTestUtils {
    
    /**
     * Creates a test nav controller for UI tests
     */
    fun createTestNavController(): TestNavHostController {
        return TestNavHostController(ApplicationProvider.getApplicationContext())
    }
    
    /**
     * Verifies that the current destination matches the expected route
     */
    fun verifyCurrentDestination(
        navController: TestNavHostController,
        expectedRoute: String
    ): Boolean {
        return navController.currentDestination?.route == expectedRoute
    }
    
    /**
     * Verifies that a navigation argument has the expected value
     */
    fun verifyNavigationArgument(
        navController: TestNavHostController,
        argumentName: String,
        expectedValue: String
    ): Boolean {
        return navController.currentBackStackEntry?.arguments?.getString(argumentName) == expectedValue
    }
    
    /**
     * Simulates navigation to a destination
     */
    fun navigateToDestination(
        navController: TestNavHostController,
        destination: AeonWalletDestinations,
        vararg arguments: Pair<String, String>
    ) {
        val route = when (destination) {
            is AeonWalletDestinations.CardDetail -> destination.createRoute(arguments.find { it.first == "cardId" }?.second ?: "")
            is AeonWalletDestinations.LoanApplication -> destination.createRoute(arguments.find { it.first == "loanId" }?.second ?: "")
            is AeonWalletDestinations.UtilityPayment -> destination.createRoute(arguments.find { it.first == "billId" }?.second ?: "")
            is AeonWalletDestinations.CardRepayment -> destination.createRoute(arguments.find { it.first == "cardId" }?.second ?: "")
            is AeonWalletDestinations.LoanRepayment -> destination.createRoute(arguments.find { it.first == "loanId" }?.second ?: "")
            is AeonWalletDestinations.Donation -> destination.createRoute(arguments.find { it.first == "organizationId" }?.second ?: "")
            else -> destination.route
        }
        navController.navigate(route)
    }
}

/**
 * Navigation state for testing
 */
data class NavigationState(
    val currentRoute: String?,
    val backStack: List<String>,
    val arguments: Map<String, String>
) {
    companion object {
        fun fromNavController(navController: TestNavHostController): NavigationState {
            return NavigationState(
                currentRoute = navController.currentDestination?.route,
                backStack = navController.backQueue.map { it.destination.route ?: "" },
                arguments = navController.currentBackStackEntry?.arguments?.let { args ->
                    args.keySet().associateWith { key -> args.getString(key) ?: "" }
                } ?: emptyMap()
            )
        }
    }
}
