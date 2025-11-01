package com.mobizonetech.aeon_wallet_cursor.data.remote.api

import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.LoginRequestDto
import com.mobizonetech.aeon_wallet_cursor.data.remote.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API Service for Authentication endpoints
 */
interface AuthApiService {
    
    /**
     * Login with email/phone and password
     * 
     * @param request Login credentials
     * @return Response containing user data and token
     */
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>
}


