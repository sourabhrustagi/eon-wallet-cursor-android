package com.mobizonetech.aeon_wallet_cursor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mobizonetech.aeon_wallet_cursor.presentation.navigation.AppNavigation
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import com.mobizonetech.aeon_wallet_cursor.util.Logger
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity of AEON Wallet application
 * 
 * This activity serves as the entry point for the app.
 * It is annotated with @AndroidEntryPoint to enable Hilt dependency injection.
 * 
 * Features:
 * - Edge-to-edge display support
 * - Jetpack Compose UI
 * - Material Design 3 theming
 * - Hilt dependency injection
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    /**
     * Called when the activity is created
     * Sets up the Compose UI with the app theme
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "MainActivity created")
        
        // Enable edge-to-edge display
        enableEdgeToEdge()
        
        setContent {
            AeonwalletcursorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Logger.d(TAG, "MainActivity started")
    }

    override fun onResume() {
        super.onResume()
        Logger.d(TAG, "MainActivity resumed")
    }

    override fun onPause() {
        super.onPause()
        Logger.d(TAG, "MainActivity paused")
    }

    override fun onStop() {
        super.onStop()
        Logger.d(TAG, "MainActivity stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(TAG, "MainActivity destroyed")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

/**
 * Preview function for MainActivity
 * Used by Android Studio's preview feature
 */
@Preview(
    name = "Main Activity Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MainActivityPreview() {
            AeonwalletcursorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
}
