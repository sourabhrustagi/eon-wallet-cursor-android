package com.mobizonetech.aeon_wallet_cursor.util

/**
 * Application-wide constants
 * 
 * Best Practices:
 * - Group related constants together
 * - Use descriptive names
 * - Document non-obvious values
 * - Consider using @Dimension and @ColorInt annotations where appropriate
 */
object Constants {
    
    // UI Dimensions (in dp unless noted otherwise)
    const val BUTTON_HEIGHT = 56
    const val ELEVATION_DEFAULT = 4
    const val ICON_SIZE = 120
    const val PAGE_INDICATOR_SELECTED_SIZE = 12
    const val PAGE_INDICATOR_UNSELECTED_SIZE = 8
    
    // UI Spacing (in dp)
    const val SPACING_SMALL = 4
    const val SPACING_MEDIUM = 16
    const val SPACING_LARGE = 24
    const val SPACING_EXTRA_LARGE = 32
    
    // UI Borders (in dp)
    const val BUTTON_CORNER_RADIUS = 16
    const val CARD_CORNER_RADIUS = 24
    const val BUTTON_BORDER_WIDTH = 2
    
    // UI Text Sizes (in sp)
    const val TEXT_SIZE_LARGE = 28
    const val TEXT_SIZE_MEDIUM = 18
    const val TEXT_SIZE_NORMAL = 16
    const val TEXT_SIZE_SMALL = 12
    const val TEXT_SIZE_ICON = 64
    const val TEXT_SIZE_CHECK = 18
    
    // UI Transparency (alpha values 0.0f - 1.0f)
    const val ALPHA_OPAQUE = 1.0f
    const val ALPHA_SEMI_TRANSPARENT = 0.9f
    const val ALPHA_TRANSPARENT = 0.3f
    const val ALPHA_BACKGROUND = 0.2f
    
    // Brand Colors (ARGB format)
    const val GRADIENT_START = 0xFF6200EE
    const val GRADIENT_END = 0xFF3700B3
    const val PRIMARY_COLOR = 0xFF6200EE
    const val SECONDARY_COLOR = 0xFFB00020
    const val TERTIARY_COLOR = 0xFF03DAC6
    const val QUATERNARY_COLOR = 0xFF018786
    
    // Animation and Timing
    const val VIEWMODEL_SUBSCRIPTION_TIMEOUT = 5000L
    const val ANIMATION_DURATION_SHORT = 300L
    const val ANIMATION_DURATION_MEDIUM = 500L
    const val ANIMATION_DURATION_LONG = 1000L
    
    // Network and API
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val MAX_RETRY_ATTEMPTS = 3
    
    // Validation
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_PASSWORD_LENGTH = 20
    const val PHONE_NUMBER_LENGTH = 10
}

