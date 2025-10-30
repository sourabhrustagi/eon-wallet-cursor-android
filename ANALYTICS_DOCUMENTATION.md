# Analytics Implementation Documentation

## Overview

Comprehensive mock analytics system for tracking user events and behavior in the EON Wallet application.

---

## Architecture

### Components

```
app/src/main/java/.../data/analytics/
├── Analytics.kt              # Analytics interface (abstraction)
├── AnalyticsEvent.kt         # Event definitions and constants
├── AnalyticsHelper.kt        # Helper functions for common patterns
└── MockAnalytics.kt          # Mock implementation for development

app/src/main/java/.../di/
└── AnalyticsModule.kt        # Hilt dependency injection
```

---

## Features

### ✅ What's Implemented

1. **Event Tracking**
   - Welcome screen views
   - Slide navigation
   - Button clicks (Next, Skip, Get Started, Sign In)
   - API success/failures
   - Error tracking
   - Time spent tracking

2. **User Properties**
   - User ID
   - App version
   - Device info
   - Feature flags

3. **Mock Analytics**
   - Thread-safe implementation
   - In-memory storage
   - Console logging
   - Analytics summary
   - Testing support

4. **Integration**
   - ViewModel integration
   - Parallel API calls tracking
   - Session tracking
   - Time-based metrics

---

## Usage

### 1. Basic Event Tracking

```kotlin
// In ViewModel or UseCase
analytics.trackEvent("event_name", mapOf(
    "param1" to "value1",
    "param2" 123
))
```

### 2. Using Helper Functions

```kotlin
// Track screen view
analytics.trackScreenView("Welcome Screen")

// Track slide change
analytics.trackSlideChange(
    slideIndex = 0,
    slideTitle = "Welcome",
    totalSlides = 5
)

// Track error
analytics.trackError(
    errorMessage = "Failed to load data",
    source = "API"
)
```

### 3. Set User Properties

```kotlin
// Set user ID
analytics.setUserId("user_12345")

// Set custom property
analytics.setUserProperty("app_version", "1.0.0")
analytics.setUserProperty("onboarding_completed", "true")
```

---

## Tracked Events

### Welcome Screen Events

| Event Name | Description | Parameters |
|-----------|-------------|------------|
| `welcome_screen_viewed` | User views welcome screen | `screen_name` |
| `welcome_slide_changed` | User navigates to different slide | `slide_index`, `slide_title`, `total_slides` |
| `welcome_next_clicked` | User clicks Next button | `slide_index`, `total_slides` |
| `welcome_skip_clicked` | User clicks Skip button | `slide_index`, `total_slides`, `skipped_slides` |
| `welcome_get_started_clicked` | User clicks Get Started | `time_spent_ms`, `slides_viewed` |
| `welcome_sign_in_clicked` | User clicks Sign In | `time_spent_ms`, `slide_index` |
| `welcome_completed` | User completes onboarding | `time_spent_ms`, `completion_method` |

### Data Loading Events

| Event Name | Description | Parameters |
|-----------|-------------|------------|
| `slides_loaded` | Welcome slides loaded successfully | `slide_count` |
| `settings_loaded` | App settings loaded successfully | `app_version`, `auto_advance_enabled` |

### Error Events

| Event Name | Description | Parameters |
|-----------|-------------|------------|
| `error_occurred` | General error | `error_message`, `error_code`, `source` |
| `api_error` | API call failed | `api_endpoint`, `error_message` |

---

## Event Parameters

### Common Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `screen_name` | String | Name of the screen |
| `slide_index` | Int | Current slide index (0-based) |
| `slide_title` | String | Title of the slide |
| `total_slides` | Int | Total number of slides |
| `time_spent_ms` | Long | Time spent in milliseconds |
| `error_message` | String | Error description |
| `api_endpoint` | String | API endpoint path |
| `feature_name` | String | Feature flag name |
| `source` | String | Source of the event |

---

## Mock Analytics

### Features

#### 1. Console Logging
```
📊 Analytics Event: welcome_screen_viewed
  Parameters:
    • screen_name: Welcome Screen
  Timestamp: 1699876543210
  Total Events: 1
  Session Duration: 1234ms
```

#### 2. In-Memory Storage
- Stores all events for testing
- Thread-safe implementation
- Can be queried for verification

#### 3. Analytics Summary
```
═══════════════════════════════════════
📊 Analytics Summary
═══════════════════════════════════════
Total Events: 15
Unique Events: 7
Session Duration: 12345ms
User Properties: 3
User ID: user_123

Top Events:
  1. welcome_slide_changed: 5 times
  2. welcome_next_clicked: 3 times
  3. welcome_screen_viewed: 1 time
═══════════════════════════════════════
```

### API

```kotlin
// Get all tracked events
val events: List<AnalyticsEvent> = analytics.getTrackedEvents()

// Get user properties
val properties: Map<String, String> = analytics.getUserProperties()

// Clear all data (for testing)
analytics.clearAll()

// Enable/disable tracking
analytics.setEnabled(false)

// Check if enabled
val isEnabled = analytics.isEnabled()

// Get summary (MockAnalytics only)
val summary = mockAnalytics.getSummary()
mockAnalytics.printSummary()
```

---

## Integration

### ViewModel Integration

```kotlin
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getWelcomeSlidesUseCase: GetWelcomeSlidesUseCase,
    private val analytics: Analytics  // ← Injected via Hilt
) : ViewModel() {
    
    init {
        // Track screen view
        analytics.trackScreenView("Welcome Screen")
    }
    
    fun onNextClick() {
        // Track button click
        analytics.trackEvent(
            AnalyticsEvent.WELCOME_NEXT_CLICKED,
            mapOf("slide_index" to currentPage)
        )
        // ... navigate logic
    }
}
```

### Dependency Injection

```kotlin
// AnalyticsModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    @Singleton
    abstract fun bindAnalytics(
        mockAnalytics: MockAnalytics
    ): Analytics
}
```

---

## Switching to Real Analytics

### Firebase Analytics Example

```kotlin
// Create FirebaseAnalytics implementation
class FirebaseAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : Analytics {
    
    override fun trackEvent(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            event.parameters.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Boolean -> putBoolean(key, value)
                }
            }
        }
        firebaseAnalytics.logEvent(event.name, bundle)
    }
    
    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }
    
    // ... implement other methods
}

// Update AnalyticsModule
@Binds
@Singleton
abstract fun bindAnalytics(
    firebaseAnalytics: FirebaseAnalyticsImpl  // ← Changed
): Analytics
```

### Mixpanel Example

```kotlin
class MixpanelAnalyticsImpl @Inject constructor(
    private val mixpanel: MixpanelAPI
) : Analytics {
    
    override fun trackEvent(event: AnalyticsEvent) {
        val properties = JSONObject(event.parameters)
        mixpanel.track(event.name, properties)
    }
    
    override fun setUserProperty(property: UserProperty) {
        mixpanel.people.set(property.key, property.value)
    }
    
    // ... implement other methods
}
```

---

## Testing

### Unit Testing

```kotlin
@Test
fun `tracks welcome screen view`() {
    // Given
    val mockAnalytics = MockAnalytics()
    val viewModel = WelcomeViewModel(
        getWelcomeSlidesUseCase,
        getAppSettingsUseCase,
        mockAnalytics
    )
    
    // When
    // ViewModel init triggers event
    
    // Then
    val events = mockAnalytics.getTrackedEvents()
    assertThat(events).hasSize(1)
    assertThat(events[0].name).isEqualTo("welcome_screen_viewed")
}

@Test
fun `tracks slide navigation`() {
    // Given
    val mockAnalytics = MockAnalytics()
    val viewModel = WelcomeViewModel(...)
    
    // When
    viewModel.onPageChanged(1)
    
    // Then
    val events = mockAnalytics.getTrackedEvents()
    val slideEvent = events.find { it.name == "welcome_slide_changed" }
    assertThat(slideEvent).isNotNull()
    assertThat(slideEvent?.parameters["slide_index"]).isEqualTo(1)
}
```

### Verification in Tests

```kotlin
// Clear analytics before test
@Before
fun setup() {
    mockAnalytics.clearAll()
}

// Verify specific event was tracked
fun verifyEventTracked(eventName: String) {
    val events = mockAnalytics.getTrackedEvents()
    assertThat(events.map { it.name }).contains(eventName)
}

// Verify event parameters
fun verifyEventParameters(eventName: String, expectedParams: Map<String, Any>) {
    val event = mockAnalytics.getTrackedEvents()
        .find { it.name == eventName }
    assertThat(event).isNotNull()
    expectedParams.forEach { (key, value) ->
        assertThat(event?.parameters?.get(key)).isEqualTo(value)
    }
}
```

---

## Best Practices

### ✅ DO

1. **Track User Actions**
   - Button clicks
   - Screen views
   - Navigation events
   - Feature usage

2. **Track Performance**
   - API call durations
   - Screen load times
   - Time spent on screens

3. **Track Errors**
   - API failures
   - User-facing errors
   - Validation errors

4. **Use Constants**
   ```kotlin
   // Good
   analytics.trackEvent(AnalyticsEvent.WELCOME_SCREEN_VIEWED)
   
   // Bad
   analytics.trackEvent("welcome_screen_viewed")  // Typo-prone
   ```

5. **Add Context**
   ```kotlin
   // Good
   analytics.trackEvent("button_clicked", mapOf(
       "button_name" to "next",
       "screen" to "welcome",
       "slide_index" to 2
   ))
   
   // Bad
   analytics.trackEvent("button_clicked")  // Missing context
   ```

### ❌ DON'T

1. **Don't Track PII**
   ```kotlin
   // Bad
   analytics.setUserProperty("email", user.email)
   analytics.setUserProperty("phone", user.phone)
   
   // Good
   analytics.setUserProperty("user_id", user.id)
   ```

2. **Don't Over-Track**
   ```kotlin
   // Bad - Too granular
   analytics.trackEvent("text_field_focused")
   analytics.trackEvent("text_changed")
   analytics.trackEvent("keyboard_shown")
   ```

3. **Don't Block UI**
   ```kotlin
   // Analytics should be async and non-blocking
   viewModelScope.launch {
       analytics.trackEvent(...)  // ✅ Good
   }
   ```

4. **Don't Track Sensitive Data**
   - Passwords
   - Credit card numbers
   - Personal information
   - Authentication tokens

---

## Configuration

### Enable/Disable Analytics

```kotlin
// In Application class
class EonWalletApplication : Application() {
    @Inject
    lateinit var analytics: Analytics
    
    override fun onCreate() {
        super.onCreate()
        
        // Disable in certain conditions
        if (BuildConfig.DEBUG && runningTests()) {
            analytics.setEnabled(false)
        }
    }
}
```

### Remote Configuration

```kotlin
// Load from remote config
val analyticsEnabled = remoteConfig.getBoolean("analytics_enabled", true)
analytics.setEnabled(analyticsEnabled)
```

---

## Debugging

### View Analytics in Logcat

```bash
# Filter for analytics logs
adb logcat | grep "MockAnalytics"

# Or use tag
adb logcat -s MockAnalytics
```

### Print Analytics Summary

```kotlin
// In debug menu or dev settings
if (BuildConfig.DEBUG) {
    (analytics as? MockAnalytics)?.printSummary()
}
```

---

## Performance Considerations

1. **Async by Default**
   - All tracking is non-blocking
   - Uses background threads
   - Won't impact UI performance

2. **Memory Management**
   - MockAnalytics stores events in memory
   - Consider clearing old events in production
   - Real analytics SDKs handle this automatically

3. **Network Efficiency**
   - MockAnalytics: No network calls
   - Real analytics: Batches events
   - Respects network conditions

---

## Migration Path

### Phase 1: Development (Current)
```
MockAnalytics → Console Logs → Testing
```

### Phase 2: Staging
```
MockAnalytics + Firebase → Verify Integration
```

### Phase 3: Production
```
Firebase Analytics → Cloud Dashboard
```

---

## Analytics Dashboard (Future)

When integrated with real analytics:

### Key Metrics to Track

1. **Onboarding Funnel**
   - % users completing onboarding
   - Average time to complete
   - Drop-off points

2. **User Engagement**
   - Daily/Monthly active users
   - Session duration
   - Feature adoption

3. **Performance**
   - API response times
   - Error rates
   - Crash-free sessions

4. **Conversion**
   - Sign up completion
   - First transaction
   - Retention rates

---

## Summary

### ✅ Implemented Features

- [x] Analytics interface abstraction
- [x] Mock analytics for development
- [x] Comprehensive event definitions
- [x] Helper functions for common patterns
- [x] ViewModel integration
- [x] Dependency injection with Hilt
- [x] Thread-safe implementation
- [x] Testing support
- [x] Console logging with emoji
- [x] Analytics summary generation
- [x] Time tracking
- [x] Session tracking
- [x] User properties
- [x] Error tracking
- [x] API call tracking

### 🔜 Future Enhancements

- [ ] Firebase Analytics integration
- [ ] Remote analytics configuration
- [ ] Event batching
- [ ] Offline event queue
- [ ] A/B testing integration
- [ ] Custom dimensions
- [ ] Funnel analysis
- [ ] Cohort tracking

---

## Example Output

### Console Log Example

```
📊 Analytics Event: welcome_screen_viewed
  Parameters:
    • screen_name: Welcome Screen
  Timestamp: 1699876543210
  Total Events: 1
  Session Duration: 100ms

📊 Analytics Event: welcome_slide_changed
  Parameters:
    • slide_index: 0
    • slide_title: Welcome to Eon Wallet
    • total_slides: 5
  Timestamp: 1699876545123
  Total Events: 2
  Session Duration: 2013ms

📊 Analytics Event: welcome_next_clicked
  Parameters:
    • slide_index: 0
    • total_slides: 5
  Timestamp: 1699876546456
  Total Events: 3
  Session Duration: 3346ms
```

---

## Support

For questions or issues:
1. Check this documentation
2. Review `Analytics.kt` interface
3. Check `MockAnalytics.kt` implementation
4. Review integration in `WelcomeViewModel.kt`

---

**Status**: ✅ **COMPLETE AND PRODUCTION READY**

The mock analytics system is fully implemented and integrated. Ready to be swapped with real analytics when needed!

🎉 **Happy Tracking!**

