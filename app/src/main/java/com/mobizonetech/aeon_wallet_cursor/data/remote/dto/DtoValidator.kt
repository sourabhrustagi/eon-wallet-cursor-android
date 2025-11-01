package com.mobizonetech.aeon_wallet_cursor.data.remote.dto

import com.mobizonetech.aeon_wallet_cursor.domain.exception.DataException

/**
 * Validator for DTOs
 * 
 * Validates API response data before mapping to domain models
 */
object DtoValidator {
    
    /**
     * Validate WelcomeSlideDto
     */
    fun validateWelcomeSlide(dto: WelcomeSlideDto) {
        require(dto.title.isNotBlank()) { "Slide title cannot be blank" }
        require(dto.description.isNotBlank()) { "Slide description cannot be blank" }
        require(dto.icon.isNotBlank()) { "Slide icon cannot be blank" }
        require(dto.features.isNotEmpty()) { "Slide must have at least one feature" }
        require(dto.features.all { it.isNotBlank() }) { "Features cannot be blank" }
    }
    
    /**
     * Validate AppSettingsDto
     */
    fun validateAppSettings(dto: AppSettingsDto) {
        require(dto.appVersion.isNotBlank()) { "App version cannot be blank" }
        require(dto.minimumAppVersion.isNotBlank()) { "Minimum app version cannot be blank" }
        
        // Validate welcome screen config
        require(dto.welcomeScreenConfig.autoAdvanceDelayMs > 0) {
            "Auto advance delay must be positive"
        }
        
        // Validate API endpoints
        require(dto.apiEndpoints.baseUrl.isNotBlank()) { "Base URL cannot be blank" }
        require(isValidUrl(dto.apiEndpoints.baseUrl)) { "Invalid base URL" }
    }
    
    /**
     * Validate WelcomeSlidesResponse
     */
    fun validateWelcomeSlidesResponse(response: WelcomeSlidesResponse) {
        if (!response.success) {
            throw DataException.ValidationException(
                response.message ?: "API returned unsuccessful response"
            )
        }
        
        require(response.data.isNotEmpty()) {
            "Welcome slides data cannot be empty"
        }
        
        response.data.forEach { validateWelcomeSlide(it) }
    }
    
    /**
     * Validate AppSettingsResponse
     */
    fun validateAppSettingsResponse(response: AppSettingsResponse) {
        if (!response.success) {
            throw DataException.ValidationException(
                response.message ?: "API returned unsuccessful response"
            )
        }
        
        validateAppSettings(response.data)
    }
    
    /**
     * Validate LoginResponseDto
     */
    fun validateLoginResponse(response: LoginResponseDto) {
        if (!response.success) {
            throw DataException.ValidationException(
                response.message ?: "Login failed"
            )
        }
        
        require(response.data != null) {
            "Login response data cannot be null"
        }
        
        val user = response.data
        require(user.id.isNotBlank()) { "User ID cannot be blank" }
        require(user.name.isNotBlank()) { "User name cannot be blank" }
        require(user.email.isNotBlank()) { "User email cannot be blank" }
        require(user.token.isNotBlank()) { "User token cannot be blank" }
    }
    
    /**
     * Simple URL validation
     */
    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }
}

