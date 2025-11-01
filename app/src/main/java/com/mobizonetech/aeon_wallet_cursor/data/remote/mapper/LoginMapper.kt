package com.mobizonetech.aeon_wallet_cursor.data.remote.mapper

import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.LoginRequestDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.LoginResponseDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.UserDto
import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginRequest
import com.mobizonetech.aeon_wallet_cursor.domain.model.LoginResult
import com.mobizonetech.aeon_wallet_cursor.domain.model.User

/**
 * Mapper for Login-related DTOs to Domain models
 */
object LoginMapper {
    
    /**
     * Map LoginRequest domain model to DTO
     */
    fun toDto(request: LoginRequest): LoginRequestDto {
        return LoginRequestDto(
            emailOrPhone = request.emailOrPhone,
            password = request.password
        )
    }
    
    /**
     * Map UserDto to User domain model
     */
    fun UserDto.toDomain(): User {
        return User(
            id = id,
            name = name,
            email = email,
            phone = phone,
            token = token
        )
    }
    
    /**
     * Map LoginResponseDto to LoginResult domain model
     */
    fun toDomain(response: LoginResponseDto): LoginResult? {
        return response.data?.let { userDto ->
            LoginResult(
                user = userDto.toDomain(),
                isNewUser = userDto.isNewUser
            )
        }
    }
}

