package com.mobizonetech.aeon_wallet_cursor.data.remote.mapper

import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.WelcomeSlideDto
import com.mobizonetech.aeon_wallet_cursor.domain.model.WelcomeSlide

/**
 * Mapper to convert API DTOs to domain models
 */
object WelcomeSlideMapper {
    
    /**
     * Convert WelcomeSlideDto to WelcomeSlide domain model
     * 
     * @param dto Data transfer object from API
     * @return Domain model
     */
    fun mapToDomain(dto: WelcomeSlideDto): WelcomeSlide {
        return WelcomeSlide(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            icon = dto.icon,
            iconBackgroundColor = parseColorString(dto.iconBackgroundColor),
            features = dto.features
        )
    }
    
    /**
     * Convert list of DTOs to list of domain models
     */
    fun mapToDomainList(dtoList: List<WelcomeSlideDto>): List<WelcomeSlide> {
        return dtoList.map { mapToDomain(it) }
    }
    
    /**
     * Parse hex color string to Long
     * 
     * @param colorString Hex color string (e.g., "#6200EE" or "6200EE")
     * @return Color as Long with alpha channel
     */
    private fun parseColorString(colorString: String): Long {
        return try {
            // Remove # if present
            val cleanColor = colorString.removePrefix("#")
            
            // If no alpha channel, add FF for full opacity
            val fullColor = if (cleanColor.length == 6) {
                "FF$cleanColor"
            } else {
                cleanColor
            }
            
            // Convert to Long with 0xFF prefix for Compose Color
            fullColor.toLong(16) or 0xFF000000
        } catch (e: Exception) {
            // Default color if parsing fails
            0xFF6200EE
        }
    }
}

