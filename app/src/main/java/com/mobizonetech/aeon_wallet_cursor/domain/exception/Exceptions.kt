package com.mobizonetech.aeon_wallet_cursor.domain.exception

/**
 * Base exception for the application
 * All custom exceptions should extend this
 */
sealed class AppException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Network related exceptions
 */
sealed class NetworkException(
    message: String,
    cause: Throwable? = null
) : AppException(message, cause) {
    
    class NoInternetException(
        message: String = "No internet connection available"
    ) : NetworkException(message)
    
    class TimeoutException(
        message: String = "Request timed out"
    ) : NetworkException(message)
    
    class ServerException(
        val code: Int,
        message: String = "Server error: $code"
    ) : NetworkException(message)
    
    class UnknownNetworkException(
        message: String = "Unknown network error",
        cause: Throwable? = null
    ) : NetworkException(message, cause)
}

/**
 * API related exceptions
 */
sealed class ApiException(
    message: String,
    cause: Throwable? = null
) : AppException(message, cause) {
    
    class InvalidResponseException(
        message: String = "Invalid API response"
    ) : ApiException(message)
    
    class UnauthorizedException(
        message: String = "Unauthorized access"
    ) : ApiException(message)
    
    class NotFoundException(
        message: String = "Resource not found"
    ) : ApiException(message)
    
    class RateLimitException(
        message: String = "Rate limit exceeded"
    ) : ApiException(message)
}

/**
 * Data related exceptions
 */
sealed class DataException(
    message: String,
    cause: Throwable? = null
) : AppException(message, cause) {
    
    class ValidationException(
        message: String = "Data validation failed"
    ) : DataException(message)
    
    class ParseException(
        message: String = "Failed to parse data",
        cause: Throwable? = null
    ) : DataException(message, cause)
    
    class CacheException(
        message: String = "Cache operation failed",
        cause: Throwable? = null
    ) : DataException(message, cause)
}

/**
 * Business logic exceptions
 */
sealed class BusinessException(
    message: String,
    cause: Throwable? = null
) : AppException(message, cause) {
    
    class InvalidStateException(
        message: String = "Invalid application state"
    ) : BusinessException(message)
    
    class FeatureDisabledException(
        val featureName: String,
        message: String = "Feature '$featureName' is disabled"
    ) : BusinessException(message)
}

