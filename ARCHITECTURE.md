# EON Wallet - Architecture Documentation

**Version**: 1.0.0  
**Last Updated**: October 30, 2025  
**Status**: Production Ready

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture Patterns](#architecture-patterns)
- [Layer Architecture](#layer-architecture)
- [Data Flow](#data-flow)
- [Dependency Graph](#dependency-graph)
- [Module Structure](#module-structure)
- [Key Components](#key-components)
- [Design Patterns](#design-patterns)
- [Best Practices](#best-practices)

---

## 🎯 Overview

EON Wallet follows **Clean Architecture** principles with **MVVM** pattern for presentation layer, ensuring:

- ✅ **Separation of Concerns** - Each layer has specific responsibilities
- ✅ **Testability** - Independent testing of business logic
- ✅ **Maintainability** - Easy to understand and modify
- ✅ **Scalability** - Simple to add new features
- ✅ **Framework Independence** - Core logic independent of Android

### Architectural Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  ┌───────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │   Compose UI  │←→│  ViewModel   │←→│   UI State      │  │
│  └───────────────┘  └──────────────┘  └─────────────────┘  │
└─────────────────────────────┬───────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────┐
│                      Domain Layer                            │
│  ┌───────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │   Use Cases   │  │    Models    │  │   Repository    │  │
│  │               │  │              │  │   Interfaces    │  │
│  └───────────────┘  └──────────────┘  └─────────────────┘  │
└─────────────────────────────┬───────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────┐
│                       Data Layer                             │
│  ┌───────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │  Repository   │←→│  API Service │  │   DTOs/Mapper   │  │
│  │     Impl      │  │              │  │                 │  │
│  └───────────────┘  └──────────────┘  └─────────────────┘  │
│                                                              │
│  ┌───────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │    Retry      │  │ Interceptor  │  │   Analytics     │  │
│  │    Policy     │  │              │  │                 │  │
│  └───────────────┘  └──────────────┘  └─────────────────┘  │
└──────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────┐
│                   External Services                          │
│                  (Backend API, Analytics)                    │
└──────────────────────────────────────────────────────────────┘
```

---

## 🏗️ Architecture Patterns

### 1. Clean Architecture

**Core Principle**: Dependency Rule - Dependencies point inward

```
External Frameworks (UI, Network)
           ↓
     Presentation Layer
           ↓
      Domain Layer (Core)
           ↓
       Data Layer
           ↓
   External Services
```

**Benefits:**
- Business logic independent of UI framework
- Testable without Android dependencies
- Easy to swap implementations
- Clear separation of concerns

### 2. MVVM (Model-View-ViewModel)

**Components:**
- **Model**: Domain models and business logic
- **View**: Jetpack Compose UI
- **ViewModel**: State management and UI logic

```
View (Compose) ←→ ViewModel ←→ UseCase ←→ Repository
                    │
                    ↓
                 UI State
```

**Benefits:**
- Reactive UI with StateFlow
- Lifecycle-aware components
- Testable presentation logic
- Clear data flow

### 3. Repository Pattern

**Purpose**: Abstract data sources from business logic

```
ViewModel → Repository Interface → Repository Implementation → Data Source
                                                            ├→ API Service
                                                            ├→ Local DB
                                                            └→ Cache
```

**Benefits:**
- Single source of truth
- Easy to swap data sources
- Centralized data access
- Testable data layer

---

## 📦 Layer Architecture

### 1. Presentation Layer

**Location**: `app/src/main/java/.../presentation/`

**Purpose**: UI and user interactions

**Components:**

```
presentation/
├── screens/
│   └── WelcomeScreen.kt          # Compose UI screens
└── viewmodel/
    └── WelcomeViewModel.kt       # State management
```

**Responsibilities:**
- ✅ Render UI using Jetpack Compose
- ✅ Handle user interactions
- ✅ Manage UI state with ViewModels
- ✅ Observe data from domain layer
- ✅ Display loading/error states

**Key Files:**

**WelcomeScreen.kt**
```kotlin
@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    // UI rendering based on state
    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(uiState.error)
        else -> ContentState(uiState.slides)
    }
}
```

**WelcomeViewModel.kt**
```kotlin
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase,
    private val analytics: Analytics
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()
    
    init {
        loadSlides()
    }
    
    private fun loadSlides() {
        viewModelScope.launch {
            val result = getWelcomeSlidesUseCase()
            // Update UI state based on result
        }
    }
}
```

**Design Patterns:**
- ✅ MVVM for presentation logic
- ✅ Observer pattern with StateFlow
- ✅ Single source of truth
- ✅ Unidirectional data flow

---

### 2. Domain Layer

**Location**: `app/src/main/java/.../domain/`

**Purpose**: Business logic and rules

**Components:**

```
domain/
├── model/
│   ├── WelcomeSlide.kt          # Domain models
│   └── AppSettings.kt
├── repository/
│   ├── WelcomeRepository.kt     # Repository interfaces
│   └── AppSettingsRepository.kt
├── usecase/
│   ├── GetWelcomeSlidesUseCase.kt   # Use cases
│   └── GetAppSettingsUseCase.kt
├── util/
│   └── Result.kt                # Result wrapper
└── exception/
    └── Exceptions.kt            # Custom exceptions
```

**Responsibilities:**
- ✅ Define business entities
- ✅ Implement business rules
- ✅ Define repository contracts
- ✅ Provide use cases for features
- ✅ No dependencies on Android/UI

**Key Files:**

**Domain Models** (`model/WelcomeSlide.kt`)
```kotlin
data class WelcomeSlide(
    val id: Int,
    val title: String,
    val description: String,
    val icon: String,
    val iconBackgroundColor: Long,
    val features: List<String>
)
```

**Repository Interface** (`repository/WelcomeRepository.kt`)
```kotlin
interface WelcomeRepository {
    suspend fun getWelcomeSlides(): Result<List<WelcomeSlide>>
}
```

**Use Case** (`usecase/GetWelcomeSlidesUseCase.kt`)
```kotlin
class GetWelcomeSlidesUseCase @Inject constructor(
    private val repository: WelcomeRepository
) {
    suspend operator fun invoke(): Result<List<WelcomeSlide>> {
        Logger.d(TAG, "Executing GetWelcomeSlidesUseCase")
        return repository.getWelcomeSlides()
    }
}
```

**Result Wrapper** (`util/Result.kt`)
```kotlin
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

**Design Patterns:**
- ✅ Use Case pattern for business operations
- ✅ Repository pattern for data abstraction
- ✅ Result wrapper for error handling
- ✅ Sealed classes for type safety

---

### 3. Data Layer

**Location**: `app/src/main/java/.../data/`

**Purpose**: Data access and manipulation

**Components:**

```
data/
├── remote/
│   ├── api/
│   │   ├── WelcomeApiService.kt         # Retrofit services
│   │   └── AppSettingsApiService.kt
│   ├── dto/
│   │   ├── WelcomeSlideDto.kt           # Data Transfer Objects
│   │   ├── AppSettingsDto.kt
│   │   └── DtoValidator.kt              # DTO validation
│   ├── mapper/
│   │   ├── WelcomeSlideMapper.kt        # DTO to Domain mappers
│   │   └── AppSettingsMapper.kt
│   ├── retry/
│   │   └── RetryPolicy.kt               # Retry mechanism
│   └── interceptor/
│       └── MockInterceptor.kt           # Mock API interceptor
├── repository/
│   ├── WelcomeRepositoryImpl.kt         # Local implementation
│   ├── WelcomeRepositoryApiImpl.kt      # API implementation
│   └── AppSettingsRepositoryImpl.kt
└── analytics/
    ├── Analytics.kt                     # Analytics interface
    ├── AnalyticsEvent.kt                # Event definitions
    ├── AnalyticsHelper.kt               # Helper extensions
    └── MockAnalytics.kt                 # Mock implementation
```

**Responsibilities:**
- ✅ Implement repository interfaces
- ✅ Make network requests
- ✅ Transform DTOs to domain models
- ✅ Handle data caching
- ✅ Manage API communication
- ✅ Implement retry logic

**Key Files:**

**Repository Implementation** (`repository/WelcomeRepositoryApiImpl.kt`)
```kotlin
class WelcomeRepositoryApiImpl @Inject constructor(
    private val apiService: WelcomeApiService
) : WelcomeRepository {
    
    override suspend fun getWelcomeSlides(): Result<List<WelcomeSlide>> = 
        withContext(Dispatchers.IO) {
            try {
                // API call with retry
                val response = RetryPolicy.executeWithRetry {
                    apiService.getWelcomeSlides()
                }
                
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success) {
                        // Validate and map
                        DtoValidator.validateWelcomeSlidesResponse(body)
                        val domainSlides = WelcomeSlideMapper.mapToDomainList(body.data)
                        Result.Success(domainSlides)
                    } else {
                        Result.Error(body?.message ?: "API error")
                    }
                } else {
                    Result.Error("HTTP ${response.code()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error", e)
            }
        }
}
```

**API Service** (`remote/api/WelcomeApiService.kt`)
```kotlin
interface WelcomeApiService {
    @GET("onboarding/slides")
    suspend fun getWelcomeSlides(): Response<WelcomeSlidesResponse>
}
```

**Mapper** (`remote/mapper/WelcomeSlideMapper.kt`)
```kotlin
object WelcomeSlideMapper {
    fun mapToDomain(dto: WelcomeSlideDto): WelcomeSlide {
        return WelcomeSlide(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            icon = dto.icon,
            iconBackgroundColor = parseColor(dto.iconBackgroundColor),
            features = dto.features
        )
    }
    
    fun mapToDomainList(dtos: List<WelcomeSlideDto>): List<WelcomeSlide> {
        return dtos.map { mapToDomain(it) }
    }
}
```

**Retry Policy** (`remote/retry/RetryPolicy.kt`)
```kotlin
object RetryPolicy {
    suspend fun <T> executeWithRetry(
        maxRetries: Int = 3,
        initialDelayMs: Long = 1000L,
        maxDelayMs: Long = 5000L,
        backoffMultiplier: Double = 2.0,
        shouldRetry: (Exception) -> Boolean = ::isRetryableError,
        block: suspend () -> T
    ): T {
        // Exponential backoff retry logic
    }
}
```

**Design Patterns:**
- ✅ Repository pattern implementation
- ✅ Mapper pattern for data transformation
- ✅ DTO pattern for API communication
- ✅ Retry pattern with exponential backoff
- ✅ Interceptor pattern for mock data

---

### 4. Dependency Injection Layer

**Location**: `app/src/main/java/.../di/`

**Purpose**: Manage dependencies with Hilt

**Components:**

```
di/
├── AppModule.kt            # App-level dependencies
├── NetworkModule.kt        # Network dependencies
└── AnalyticsModule.kt      # Analytics dependencies
```

**AppModule.kt**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideWelcomeRepository(
        apiService: WelcomeApiService
    ): WelcomeRepository {
        return WelcomeRepositoryApiImpl(apiService)
    }
    
    @Provides
    @Singleton
    fun provideGetWelcomeSlidesUseCase(
        repository: WelcomeRepository
    ): GetWelcomeSlidesUseCase {
        return GetWelcomeSlidesUseCase(repository)
    }
}
```

**NetworkModule.kt**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(MockInterceptor(BuildConfig.DEBUG))
            .addInterceptor(HttpLoggingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
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
    
    @Provides
    @Singleton
    fun provideWelcomeApiService(retrofit: Retrofit): WelcomeApiService {
        return retrofit.create(WelcomeApiService::class.java)
    }
}
```

**Design Patterns:**
- ✅ Dependency Injection with Hilt
- ✅ Module pattern for organization
- ✅ Singleton pattern for shared instances
- ✅ Factory pattern via Hilt

---

### 5. UI Theme Layer

**Location**: `app/src/main/java/.../ui/theme/`

**Purpose**: App styling and theming

**Components:**

```
ui/theme/
├── Color.kt               # Color palette
├── Theme.kt               # Material Design 3 theme
└── Type.kt                # Typography
```

**Responsibilities:**
- ✅ Define app colors
- ✅ Configure Material Design 3
- ✅ Manage dark/light themes
- ✅ Define typography

---

### 6. Utility Layer

**Location**: `app/src/main/java/.../util/`

**Purpose**: Common utilities

**Components:**

```
util/
├── Logger.kt              # Centralized logging
├── Constants.kt           # App constants
└── PerformanceMonitor.kt  # Performance measurement
```

---

## 🔄 Data Flow

### Complete Data Flow Example

```
User Action (Click Welcome Button)
        │
        ▼
┌───────────────────────┐
│   WelcomeScreen.kt    │  Compose UI captures event
│   (Presentation)      │
└───────────┬───────────┘
            │ viewModel.onButtonClick()
            ▼
┌───────────────────────┐
│  WelcomeViewModel.kt  │  Updates UI state
│   (Presentation)      │  Triggers use case
└───────────┬───────────┘
            │ getWelcomeSlidesUseCase()
            ▼
┌─────────────────────────┐
│ GetWelcomeSlidesUseCase │  Business logic
│      (Domain)           │  Calls repository
└───────────┬─────────────┘
            │ repository.getWelcomeSlides()
            ▼
┌────────────────────────────┐
│ WelcomeRepositoryApiImpl   │  Data access
│         (Data)             │  Makes API call
└───────────┬────────────────┘
            │ RetryPolicy.executeWithRetry { apiService.get() }
            ▼
┌────────────────────────┐
│   WelcomeApiService    │  Network call
│      (Data/API)        │  via Retrofit
└───────────┬────────────┘
            │ HTTP Request
            ▼
┌────────────────────────┐
│    Backend API         │  External service
└───────────┬────────────┘
            │ HTTP Response
            ▼
┌────────────────────────┐
│   WelcomeSlideDto      │  Raw data
│      (Data/DTO)        │
└───────────┬────────────┘
            │ WelcomeSlideMapper.mapToDomain()
            ▼
┌────────────────────────┐
│    WelcomeSlide        │  Domain model
│      (Domain)          │
└───────────┬────────────┘
            │ Result.Success(slides)
            ▼
┌───────────────────────┐
│  WelcomeViewModel.kt  │  Update UI state
│   (Presentation)      │
└───────────┬───────────┘
            │ _uiState.value = ...
            ▼
┌───────────────────────┐
│   WelcomeScreen.kt    │  Recompose UI
│   (Presentation)      │  Display data
└───────────────────────┘
```

### State Flow

```
ViewModel
    │
    ├─> StateFlow<UiState> ─────> Compose UI
    │                              (observes changes)
    │
    └─> viewModelScope.launch
            │
            └─> suspend useCase()
                    │
                    └─> suspend repository()
                            │
                            └─> Result<Data>
                                    │
                                    └─> Update StateFlow
                                            │
                                            └─> UI Recomposes
```

---

## 📊 Dependency Graph

```
MainActivity
    └─> WelcomeScreen (Compose)
            │
            └─> WelcomeViewModel (Hilt injected)
                    │
                    ├─> GetWelcomeSlidesUseCase (Hilt injected)
                    │       │
                    │       └─> WelcomeRepository (interface)
                    │               │
                    │               └─> WelcomeRepositoryApiImpl (Hilt injected)
                    │                       │
                    │                       ├─> WelcomeApiService (Retrofit)
                    │                       ├─> WelcomeSlideMapper
                    │                       ├─> DtoValidator
                    │                       └─> RetryPolicy
                    │
                    ├─> GetAppSettingsUseCase (Hilt injected)
                    │       │
                    │       └─> AppSettingsRepository (interface)
                    │               │
                    │               └─> AppSettingsRepositoryImpl (Hilt injected)
                    │                       └─> AppSettingsApiService (Retrofit)
                    │
                    └─> Analytics (Hilt injected)
                            │
                            └─> MockAnalytics (Debug)
                                FirebaseAnalytics (Release)
```

---

## 🎨 Design Patterns

### 1. **Repository Pattern**
- Abstracts data sources
- Single source of truth
- Easy to test

### 2. **Use Case Pattern**
- Encapsulates business logic
- Single responsibility
- Reusable operations

### 3. **MVVM Pattern**
- Separates UI from logic
- Reactive with StateFlow
- Lifecycle aware

### 4. **Dependency Injection**
- Loose coupling
- Easy testing with mocks
- Centralized configuration

### 5. **Observer Pattern**
- StateFlow/Flow for reactive data
- UI observes ViewModel
- Automatic UI updates

### 6. **Mapper Pattern**
- Transforms DTOs to domain models
- Keeps layers independent
- Centralized conversion logic

### 7. **Retry Pattern**
- Automatic error recovery
- Exponential backoff
- Smart error detection

### 8. **Strategy Pattern**
- Different retry strategies
- Pluggable error handlers
- Flexible configuration

### 9. **Factory Pattern**
- Hilt provides dependencies
- ViewModelFactory for ViewModels
- Service creation

### 10. **Singleton Pattern**
- Shared instances (repositories)
- Network clients
- Analytics trackers

---

## ✅ Best Practices

### Architectural Principles

**1. Dependency Rule**
```
✅ Domain doesn't depend on Data or Presentation
✅ Data depends on Domain
✅ Presentation depends on Domain
✅ Dependencies point inward
```

**2. Single Responsibility**
```
✅ Each class has one reason to change
✅ ViewModels only manage UI state
✅ Use cases only contain business logic
✅ Repositories only access data
```

**3. Interface Segregation**
```
✅ Small, focused interfaces
✅ Repository interfaces in domain layer
✅ Easy to mock for testing
```

**4. Dependency Inversion**
```
✅ Depend on abstractions, not concretions
✅ Use repository interfaces
✅ Inject dependencies
```

### Code Organization

**1. Package Structure**
```
✅ Organize by feature and layer
✅ Clear separation of concerns
✅ Easy to navigate
```

**2. Naming Conventions**
```
✅ Clear, descriptive names
✅ Follow Kotlin conventions
✅ Consistent across codebase
```

**3. Documentation**
```
✅ KDoc for public APIs
✅ Comments for complex logic
✅ Architecture documents
```

### Testing Strategy

**1. Unit Tests**
```
✅ Test use cases independently
✅ Test ViewModels with mocks
✅ Test repositories with fake data
✅ 100% coverage for business logic
```

**2. Integration Tests**
```
✅ Test repository with real API
✅ Test ViewModel with real use cases
✅ Test data flow end-to-end
```

**3. UI Tests**
```
✅ Test Compose screens
✅ Test user interactions
✅ Test state changes
```

---

## 🚀 Scalability

### Adding New Features

**1. New Screen**
```
1. Create Composable in presentation/screens/
2. Create ViewModel in presentation/viewmodel/
3. Create use case in domain/usecase/
4. Update navigation
```

**2. New Data Source**
```
1. Define interface in domain/repository/
2. Implement in data/repository/
3. Add DTO and mapper in data/remote/
4. Configure in DI module
```

**3. New Business Logic**
```
1. Create use case in domain/usecase/
2. Add required repository methods
3. Inject into ViewModel
4. Add tests
```

---

## 📚 Related Documentation

- [README.md](README.md) - Project overview
- [BEST_PRACTICES_IMPLEMENTED.md](BEST_PRACTICES_IMPLEMENTED.md) - Coding standards
- [API_INTEGRATION_DOCUMENTATION.md](API_INTEGRATION_DOCUMENTATION.md) - API details
- [TEST_COVERAGE_SUMMARY.md](TEST_COVERAGE_SUMMARY.md) - Testing approach

---

*Last Updated: October 30, 2025*  
*Architecture Version: 1.0.0*  
*Status: ✅ Production Ready*
