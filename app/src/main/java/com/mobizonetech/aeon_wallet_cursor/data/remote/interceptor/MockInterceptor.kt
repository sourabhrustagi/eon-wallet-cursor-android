package com.mobizonetech.aeon_wallet_cursor.data.remote.interceptor

import com.mobizonetech.aeon_wallet_cursor.util.Logger
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Mock Interceptor for development and testing
 * 
 * Intercepts network requests and returns mock data
 * Useful when backend API is not available
 * 
 * @param enabled Whether mock interception is enabled
 */
class MockInterceptor(private val enabled: Boolean = true) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!enabled) {
            return chain.proceed(chain.request())
        }

        val request = chain.request()
        val path = request.url.encodedPath
        
        Logger.d(TAG, "Intercepting request: $path")

        return when {
            path.contains("onboarding/slides") -> {
                createMockResponse(chain, getMockWelcomeSlidesJson())
            }
            else -> {
                // Pass through for unknown endpoints
                chain.proceed(request)
            }
        }
    }

    /**
     * Create a mock response
     */
    private fun createMockResponse(
        chain: Interceptor.Chain,
        jsonResponse: String
    ): Response {
        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .code(200)
            .message("OK")
            .body(
                jsonResponse.toResponseBody(
                    "application/json".toMediaTypeOrNull()
                )
            )
            .build()
    }

    /**
     * Mock JSON response for welcome slides
     */
    private fun getMockWelcomeSlidesJson(): String {
        return """
            {
              "success": true,
              "message": "Welcome slides fetched successfully",
              "data": [
                {
                  "id": 0,
                  "title": "Welcome to Aeon Wallet",
                  "description": "Your secure gateway to digital finance. Manage your money with confidence and ease.",
                  "icon": "₿",
                  "icon_background_color": "#6200EE",
                  "features": [
                    "Bank-level security",
                    "Instant transactions",
                    "24/7 support"
                  ]
                },
                {
                  "id": 1,
                  "title": "Secure & Fast",
                  "description": "Send and receive money instantly with military-grade encryption protecting your transactions.",
                  "icon": "🔒",
                  "icon_background_color": "#B00020",
                  "features": [
                    "End-to-end encryption",
                    "Instant transfers",
                    "Fraud protection"
                  ]
                },
                {
                  "id": 2,
                  "title": "Crypto Trading",
                  "description": "Trade cryptocurrencies with zero fees and real-time market data at your fingertips.",
                  "icon": "📈",
                  "icon_background_color": "#03DAC6",
                  "features": [
                    "Zero trading fees",
                    "Real-time prices",
                    "Multiple cryptocurrencies"
                  ]
                },
                {
                  "id": 3,
                  "title": "Smart Analytics",
                  "description": "Track your spending, set budgets, and get insights to make smarter financial decisions.",
                  "icon": "📊",
                  "icon_background_color": "#018786",
                  "features": [
                    "Spending insights",
                    "Budget tracking",
                    "Financial reports"
                  ]
                },
                {
                  "id": 4,
                  "title": "Ready to Start?",
                  "description": "Join thousands of users who trust Aeon Wallet for their digital finance needs.",
                  "icon": "🚀",
                  "icon_background_color": "#6200EE",
                  "features": [
                    "Quick setup in minutes",
                    "No hidden fees",
                    "Global accessibility"
                  ]
                }
              ]
            }
        """.trimIndent()
    }

    companion object {
        private const val TAG = "MockInterceptor"
    }
}

