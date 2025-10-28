package com.mobizonetech.aeon_wallet_cursor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobizonetech.aeon_wallet_cursor.domain.usecase.GetWelcomeSlidesUseCase
import com.mobizonetech.aeon_wallet_cursor.presentation.screens.WelcomeScreen
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.WelcomeViewModel
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.WelcomeViewModelFactory
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AeonwalletcursorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialize ViewModel with dependencies
                    val viewModel: WelcomeViewModel = viewModel(
                        factory = WelcomeViewModelFactory(
                            application = application,
                            getWelcomeSlidesUseCase = GetWelcomeSlidesUseCase(
                                context = this@MainActivity
                            )
                        )
                    )
                    WelcomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}
