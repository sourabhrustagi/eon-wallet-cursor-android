# UI Tests Documentation

This directory contains instrumentation (UI) tests for the AEON Wallet application using Jetpack Compose Testing.

## Test Structure

```
androidTest/java/com/mobizonetech/aeon_wallet_cursor/
├── presentation/
│   └── screens/
│       ├── WelcomeScreenTest.kt                    (30+ tests)
│       └── WelcomeScreenStateComponentsTest.kt     (20+ tests)
└── README_UI_TESTS.md
```

## Test Coverage

### 1. WelcomeScreenTest (30+ tests)

Tests for the main WelcomeScreen composables:

**WelcomeSlide Tests:**
- ✅ Displays title correctly
- ✅ Displays description correctly
- ✅ Displays icon correctly
- ✅ Displays all features with checkmarks
- ✅ Handles empty features list
- ✅ Handles long text properly
- ✅ Displays different slides with correct content
- ✅ Displays various icons correctly

**WelcomeFeatures Tests:**
- ✅ Displays single feature
- ✅ Displays multiple features
- ✅ Handles empty features list
- ✅ Shows checkmarks for all features

**WelcomeTitle Tests:**
- ✅ Displays text correctly
- ✅ Handles empty string

**WelcomeDescription Tests:**
- ✅ Displays text correctly
- ✅ Handles long text with proper wrapping

**Edge Cases:**
- ✅ Handles special characters (&, <, >, ", ')
- ✅ Handles Unicode characters (multilingual support)
- ✅ Handles maximum number of features
- ✅ All components exist and are accessible

**Theme Tests:**
- ✅ Renders correctly in light theme
- ✅ Renders correctly in dark theme

### 2. WelcomeScreenStateComponentsTest (20+ tests)

Tests for state components:

**PageIndicators Tests:**
- ✅ Shows correct indicators for first page
- ✅ Shows correct indicators for middle page
- ✅ Shows correct indicators for last page
- ✅ Handles single page
- ✅ Handles many pages (10+)
- ✅ Recomposes on page change
- ✅ Handles edge cases (zero pages, out of bounds, negative)
- ✅ Renders in light and dark themes
- ✅ Multiple instances render simultaneously
- ✅ Handles rapid page changes

**Performance Tests:**
- ✅ Handles large number of pages (50+)
- ✅ No memory leaks with repeated recompositions

## Testing Libraries

### Dependencies
```kotlin
androidTestImplementation("androidx.test.ext:junit:1.3.0")
androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
```

### Tools Used:
- **Compose Test Rule**: Main testing API for Compose
- **JUnit 4**: Test framework
- **Espresso**: For Android instrumentation
- **Coroutines Test**: For async testing

## Running UI Tests

### Prerequisites:
- Physical device or emulator running
- Debug build configured

### From Android Studio:
1. Right-click on `androidTest` directory
2. Select "Run 'All Tests'"
3. Choose target device

### From Command Line:

```bash
# Run all instrumentation tests
./gradlew connectedAndroidTest

# Run specific test class
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mobizonetech.aeon_wallet_cursor.presentation.screens.WelcomeScreenTest

# Run on specific device
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.device=<device-id>
```

### With Android Debug Bridge (ADB):
```bash
# List connected devices
adb devices

# Install test APK
./gradlew installDebugAndroidTest

# Run specific test
adb shell am instrument -w -e class com.mobizonetech.aeon_wallet_cursor.presentation.screens.WelcomeScreenTest com.mobizonetech.aeon_wallet_cursor.test/androidx.test.runner.AndroidJUnitRunner
```

## Test Patterns

### 1. Compose Test Rule
```kotlin
@get:Rule
val composeTestRule = createComposeRule()
```

### 2. Setting Content
```kotlin
composeTestRule.setContent {
    AeonwalletcursorTheme {
        WelcomeSlide(slide = mockSlide)
    }
}
```

### 3. Finding Nodes
```kotlin
// By text
composeTestRule.onNodeWithText("Title").assertIsDisplayed()

// By content description
composeTestRule.onNodeWithContentDescription("Icon").assertExists()

// By tag
composeTestRule.onNodeWithTag("welcome_slide").assertIsDisplayed()

// All nodes matching
composeTestRule.onAllNodesWithText("✓").assertCountEquals(3)
```

### 4. Assertions
```kotlin
// Visibility
assertIsDisplayed()
assertIsNotDisplayed()
assertExists()
assertDoesNotExist()

// Count
assertCountEquals(3)

// Boolean
assertIsEnabled()
assertIsNotEnabled()
```

### 5. Actions
```kotlin
// Click
onNodeWithText("Button").performClick()

// Scroll
onNodeWithTag("list").performScrollTo()

// Text input
onNodeWithTag("textfield").performTextInput("Hello")
```

## Best Practices Implemented

✅ **Test Isolation**: Each test is independent
✅ **Arrange-Act-Assert**: Clear test structure
✅ **Descriptive Names**: Test names describe what they test
✅ **Theme Testing**: Tests run in both light and dark themes
✅ **Edge Cases**: Tests cover error scenarios
✅ **Accessibility**: Semantic properties are tested
✅ **Performance**: Large data sets tested
✅ **Real Interactions**: Tests simulate actual user behavior

## Writing New UI Tests

### Template:
```kotlin
@Test
fun componentName_scenario_expectedBehavior() {
    // Given - Setup
    composeTestRule.setContent {
        AeonwalletcursorTheme {
            YourComposable(params)
        }
    }

    // When - Action (if needed)
    composeTestRule.onNodeWithText("Button").performClick()

    // Then - Assertion
    composeTestRule.onNodeWithText("Result").assertIsDisplayed()
}
```

### Checklist for New Tests:
- [ ] Test renders correctly
- [ ] Test handles user interactions
- [ ] Test shows correct data
- [ ] Test handles edge cases
- [ ] Test works in light/dark themes
- [ ] Test is accessible
- [ ] Test name is descriptive
- [ ] Test is isolated and repeatable

## Common Issues & Solutions

### Issue: Test hangs or times out
**Solution**: Add `composeTestRule.waitForIdle()` or use `waitUntil {}`

### Issue: Node not found
**Solution**: 
- Verify text is exact match
- Check if node is scrolled out of view
- Use `printToLog()` to debug tree

### Issue: Flaky tests
**Solution**:
- Use `waitUntil {}` instead of fixed delays
- Ensure proper state initialization
- Check for race conditions

### Issue: Tests work locally but fail in CI
**Solution**:
- Ensure consistent screen sizes
- Check for timing issues
- Verify CI has proper emulator setup

## Debugging Tests

### Print Semantic Tree:
```kotlin
composeTestRule.onRoot().printToLog("TAG")
```

### Wait for Specific Condition:
```kotlin
composeTestRule.waitUntil(timeoutMillis = 5000) {
    // condition
    true
}
```

### Take Screenshot:
```kotlin
composeTestRule.onRoot().captureToImage()
```

## Coverage Reports

Generate coverage reports:
```bash
./gradlew createDebugAndroidTestCoverageReport
```

Reports location:
```
app/build/reports/androidTests/connected/
app/build/reports/coverage/androidTest/debug/
```

## Continuous Integration

Example CI configuration (GitHub Actions):
```yaml
- name: Run Instrumentation Tests
  uses: reactivecircus/android-emulator-runner@v2
  with:
    api-level: 33
    target: google_apis
    arch: x86_64
    script: ./gradlew connectedAndroidTest

- name: Upload Test Results
  if: always()
  uses: actions/upload-artifact@v3
  with:
    name: test-results
    path: app/build/reports/androidTests/
```

## Future Test Additions

Consider adding:
1. **Navigation Tests**: Test screen transitions
2. **State Persistence**: Test configuration changes
3. **Animation Tests**: Test animation completion
4. **Gesture Tests**: Test swipe, drag, pinch
5. **Screenshot Tests**: Visual regression testing
6. **A11y Tests**: Comprehensive accessibility testing

## Performance Benchmarks

Target performance:
- **Single test**: < 5 seconds
- **Full test suite**: < 5 minutes
- **No memory leaks**: Verified with repeated runs

## Contributing

When adding new UI tests:
1. Follow existing patterns
2. Add tests for success and error cases
3. Test both themes
4. Test edge cases
5. Keep tests fast and focused
6. Update this README if needed

---

**Total UI Tests**: 50+
**Device Requirements**: API 24+ (Android 7.0+)
**Execution Time**: ~3-5 minutes on emulator

Last Updated: October 28, 2025

