# API Integration Documentation

## Overview

This document describes the API integration for the EON Wallet Android application, including the implementation of a **Mock Interceptor** for development and testing.

---

## Architecture

### Network Layer Structure

```
app/src/main/java/com/mobizonetech/aeon_wallet_cursor/
├── data/
│   ├── remote/
│   │   ├── api/              # API service interfaces
│   │   │   └── WelcomeApiService.kt
│   │   ├── dto/              # Data Transfer Objects
│   │   │   └── WelcomeSlideDto.kt
│   │   ├── interceptor/      # Network interceptors
│   │   │   └── MockInterceptor.kt
│   │   └── mapper/           # DTO to Domain mappers
│   │       └── WelcomeSlideMapper.kt
│   └── repository/
│       ├── WelcomeRepositoryImpl.kt      # Local implementation
│       └── WelcomeRepositoryApiImpl.kt   # API implementation
├── di/
│   ├── AppModule.kt          # App dependencies
│   └── NetworkModule.kt      # Network dependencies
└── domain/
    ├── model/                # Domain models
    ├── repository/           # Repository interfaces
    └── util/                 # Result wrapper
```

---

## Components

### 1. API Service Interface

**File**: `WelcomeApiService.kt`

```kotlin
interface WelcomeApiService {
    @GET("api/v1/onboarding/slides")
    suspend fun getWelcomeSlides(): Response<WelcomeSlidesResponse>
}
```

- Defines API endpoints using Retrofit annotations
- Returns `Response<T>` for manual response handling
- Uses suspend functions for coroutine support

### 2. Data Transfer Objects (DTOs)

**File**: `WelcomeSlideDto.kt`

```kotlin
data class WelcomeSlideDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("icon_background_color") val iconBackgroundColor: String,
    @SerializedName("features") val features: List<String>
)
```

**Purpose**:
- Represents API response structure
- Uses `@SerializedName` for JSON mapping
- Decouples API structure from domain models

### 3. Mock Interceptor

**File**: `MockInterceptor.kt`

```kotlin
class MockInterceptor(private val enabled: Boolean = true) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!enabled) {
            return chain.proceed(chain.request())
        }
        
        val path = chain.request().url.encodedPath
        
        return when {
            path.contains("onboarding/slides") -> createMockResponse(...)
            else -> chain.proceed(chain.request())
        }
    }
}
```

**Features**:
- ✅ Intercepts network requests during development
- ✅ Returns mock JSON responses
- ✅ Can be enabled/disabled via flag
- ✅ Allows development without backend
- ✅ Automatically disabled in release builds

**Benefits**:
1. **No Backend Required**: Develop frontend independently
2. **Consistent Data**: Predictable responses for testing
3. **Offline Development**: Work without internet
4. **Fast Iteration**: Instant responses, no network latency
5. **Easy Testing**: Control exact response scenarios

### 4. Domain Mapper

**File**: `WelcomeSlideMapper.kt`

```kotlin
object WelcomeSlideMapper {
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
}
```

**Purpose**:
- Converts DTOs to domain models
- Handles data transformation (e.g., color parsing)
- Keeps domain layer clean from API details

### 5. API Repository Implementation

**File**: `WelcomeRepositoryApiImpl.kt`

```kotlin
class WelcomeRepositoryApiImpl @Inject constructor(
    private val apiService: WelcomeApiService
) : WelcomeRepository {
    override suspend fun getWelcomeSlides(): Result<List<WelcomeSlide>> {
        return try {
            val response = apiService.getWelcomeSlides()
            
            if (response.isSuccessful && response.body()?.success == true) {
                val slides = WelcomeSlideMapper.mapToDomainList(response.body()!!.data)
                Result.Success(slides)
            } else {
                Result.Error("API returned unsuccessful response")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}
```

**Features**:
- Error handling with `Result` wrapper
- DTO to domain mapping
- Coroutine support with `withContext(Dispatchers.IO)`
- Comprehensive logging

### 6. Network Module

**File**: `NetworkModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMockInterceptor(): MockInterceptor {
        return MockInterceptor(enabled = BuildConfig.DEBUG)
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        mockInterceptor: MockInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(mockInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```

**Provides**:
- Mock Interceptor (enabled in debug, disabled in release)
- Logging Interceptor (verbose in debug, none in release)
- OkHttp Client with timeouts
- Retrofit instance with Gson converter

---

## Mock API Response Format

### Endpoint
```
GET /api/v1/onboarding/slides
```

### Response
```json
{
  "success": true,
  "message": "Welcome slides fetched successfully",
  "data": [
    {
      "id": 0,
      "title": "Welcome to Aeon Wallet",
      "description": "Your secure gateway to digital finance.",
      "icon": "₿",
      "icon_background_color": "#6200EE",
      "features": [
        "Bank-level security",
        "Instant transactions",
        "24/7 support"
      ]
    }
  ]
}
```

### Fields
- `success`: Boolean indicating request success
- `message`: Optional message from server
- `data`: Array of welcome slide objects
- `icon_background_color`: Hex color string (6 or 8 chars)

---

## Configuration

### Enable/Disable Mock Interceptor

**Development (Debug Build)**:
```kotlin
// Automatically enabled in NetworkModule
MockInterceptor(enabled = BuildConfig.DEBUG) // true in debug
```

**Production (Release Build)**:
```kotlin
// Automatically disabled in NetworkModule
MockInterceptor(enabled = BuildConfig.DEBUG) // false in release
```

### Manual Override
```kotlin
// In NetworkModule.kt
@Provides
fun provideMockInterceptor(): MockInterceptor {
    return MockInterceptor(enabled = false) // Force disable
}
```

---

## Switching to Real API

When your backend is ready:

### Option 1: Disable Mock Interceptor
```kotlin
// NetworkModule.kt
fun provideMockInterceptor(): MockInterceptor {
    return MockInterceptor(enabled = false)
}
```

### Option 2: Use Build Variants
```kotlin
// build.gradle.kts
buildTypes {
    debug {
        buildConfigField("Boolean", "USE_MOCK_API", "true")
    }
    release {
        buildConfigField("Boolean", "USE_MOCK_API", "false")
    }
}

// NetworkModule.kt
fun provideMockInterceptor(): MockInterceptor {
    return MockInterceptor(enabled = BuildConfig.USE_MOCK_API)
}
```

### Option 3: Environment-based
```kotlin
// Use different base URLs
val baseUrl = if (BuildConfig.DEBUG) {
    "https://dev-api.aeonwallet.com/"
} else {
    "https://api.aeonwallet.com/"
}
```

---

## Testing

### Unit Tests

**Test Mock Interceptor**:
```kotlin
@Test
fun `mock interceptor returns successful response`() = runTest {
    // Given
    val interceptor = MockInterceptor(enabled = true)
    
    // When
    val result = repository.getWelcomeSlides()
    
    // Then
    assertThat(result).isInstanceOf(Result.Success::class.java)
}
```

**Test API Repository**:
```kotlin
@Test
fun `api repository maps DTO to domain correctly`() = runTest {
    // Given
    val mockDto = WelcomeSlideDto(...)
    
    // When
    val domain = WelcomeSlideMapper.mapToDomain(mockDto)
    
    // Then
    assertThat(domain.title).isEqualTo(mockDto.title)
}
```

### Integration Tests

Test with mock server:
```kotlin
@Test
fun `fetches slides from api successfully`() = runTest {
    // Given - Repository with API implementation
    val repository = WelcomeRepositoryApiImpl(apiService)
    
    // When
    val result = repository.getWelcomeSlides()
    
    // Then
    assertThat(result).isInstanceOf(Result.Success::class.java)
}
```

---

## Dependencies

### Gradle Dependencies
```kotlin
// Retrofit
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Gson
implementation("com.google.code.gson:gson:2.10.1")
```

### Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## Error Handling

### Repository Level
```kotlin
try {
    val response = apiService.getWelcomeSlides()
    if (response.isSuccessful) {
        Result.Success(data)
    } else {
        Result.Error("HTTP ${response.code()}")
    }
} catch (e: Exception) {
    Result.Error(e.message ?: "Unknown error")
}
```

### ViewModel Level
```kotlin
when (val result = getWelcomeSlidesUseCase()) {
    is Result.Success -> updateUiState(result.data)
    is Result.Error -> showError(result.message)
    is Result.Loading -> showLoading()
}
```

### UI Level
```kotlin
when {
    uiState.isLoading -> LoadingState()
    uiState.error != null -> ErrorState(uiState.error)
    else -> ContentState(uiState.slides)
}
```

---

## Best Practices

### ✅ DO
- Use DTOs to represent API responses
- Map DTOs to domain models in repository
- Handle all error cases with `Result` wrapper
- Log network requests for debugging
- Use mock interceptor for development
- Disable mock interceptor in production
- Add timeout configurations
- Use coroutines for async operations

### ❌ DON'T
- Don't use domain models as API models
- Don't handle UI logic in repository
- Don't expose DTOs to domain/presentation layers
- Don't forget error handling
- Don't hardcode API responses in repository
- Don't skip logging interceptor in debug
- Don't use mock data in production

---

## Future Enhancements

### 1. Multiple Mock Scenarios
```kotlin
class MockInterceptor(
    private val scenario: MockScenario = MockScenario.SUCCESS
) {
    enum class MockScenario { SUCCESS, ERROR, EMPTY, SLOW }
}
```

### 2. Response Delay Simulation
```kotlin
private fun createMockResponse(...): Response {
    if (BuildConfig.DEBUG) {
        Thread.sleep(1000) // Simulate network delay
    }
    // ...
}
```

### 3. Mock Response Variants
```kotlin
fun getMockResponse(scenario: String): String {
    return when(scenario) {
        "empty" -> emptyListJson()
        "error" -> errorJson()
        else -> successJson()
    }
}
```

### 4. API Versioning
```kotlin
@GET("api/v2/onboarding/slides") // Version 2
suspend fun getWelcomeSlidesV2(): Response<...>
```

---

## Troubleshooting

### Issue: Mock interceptor not working
**Solution**: Check if enabled flag is true and interceptor is added first in chain

### Issue: Network call times out
**Solution**: Increase timeout in OkHttpClient configuration

### Issue: JSON parsing fails
**Solution**: Verify DTO matches API response structure exactly

### Issue: Can't disable mock in release
**Solution**: Use BuildConfig.DEBUG flag instead of hardcoded boolean

---

## Summary

✅ **Implemented**:
- Complete network layer architecture
- Mock interceptor for development
- API service with Retrofit
- DTO to domain mapping
- Comprehensive error handling
- Dependency injection with Hilt
- Debug logging
- Production-ready configuration

🚀 **Benefits**:
- Develop without backend
- Fast iteration cycles
- Easy testing
- Clean architecture
- Scalable design
- Production-ready

📚 **Next Steps**:
1. Add more API endpoints as needed
2. Implement authentication interceptor
3. Add retry logic for failed requests
4. Implement caching strategy
5. Add analytics/monitoring

