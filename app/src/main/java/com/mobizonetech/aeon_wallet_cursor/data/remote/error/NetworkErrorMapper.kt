package com.mobizonetech.aeon_wallet_cursor.data.remote.error

import com.google.gson.JsonParseException
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface AppError {
    data class Network(val cause: Throwable) : AppError
    data class Http(val code: Int, val message: String?) : AppError
    data class Parsing(val cause: Throwable) : AppError
    data class Unknown(val cause: Throwable) : AppError
}

fun Throwable.toAppError(): AppError = when (this) {
    is SocketTimeoutException, is UnknownHostException, is IOException, is ConnectionShutdownException -> AppError.Network(this)
    is HttpException -> AppError.Http(code(), message())
    is JsonParseException, is com.google.gson.JsonSyntaxException -> AppError.Parsing(this)
    else -> AppError.Unknown(this)
}


