# Unit Tests Documentation

This directory contains comprehensive unit tests for the AEON Wallet application.

## Test Structure

```
test/java/com/mobizonetech/aeon_wallet_cursor/
├── data/
│   └── repository/
│       └── WelcomeRepositoryImplTest.kt      (15 tests)
├── domain/
│   └── usecase/
│       └── GetWelcomeSlidesUseCaseTest.kt    (10 tests)
├── presentation/
│   └── viewmodel/
│       └── WelcomeViewModelTest.kt           (20+ tests)
└── README_TESTS.md
```

## Test Coverage

### 1. WelcomeRepositoryImplTest (15 tests)
Tests the data layer implementation:

**Success Scenarios:**
- ✅ Returns Success with valid slides data
- ✅ Returns all five slides with correct IDs
- ✅ Uses correct icon background colors
- ✅ Each slide has three features
- ✅ All required string resources are retrieved
- ✅ Data mapping is correct for all slides

**Error Scenarios:**
- ✅ Returns Error when resources throw exception
- ✅ Returns Error when getString throws exception
- ✅ Returns Error with default message when exception message is null

**Verification:**
- ✅ Context resources are accessed correctly
- ✅ String resources retrieval is verified

### 2. GetWelcomeSlidesUseCaseTest (10 tests)
Tests the business logic layer:

**Success Scenarios:**
- ✅ Returns Success when repository returns Success
- ✅ Propagates all slide data correctly
- ✅ Handles empty list correctly
- ✅ Can be called multiple times

**Error Scenarios:**
- ✅ Returns Error when repository returns Error
- ✅ Returns Loading when repository returns Loading
- ✅ Handles repository error without crashing

**Verification:**
- ✅ Calls repository exactly once
- ✅ Returns correct result type

### 3. WelcomeViewModelTest (20+ tests)
Tests the presentation layer:

**Initial State:**
- ✅ Initial state is loading
- ✅ LoadSlides updates state with success result
- ✅ LoadSlides updates state with error result
- ✅ LoadSlides calls use case exactly once

**Navigation:**
- ✅ onPageChanged updates current page
- ✅ onNextClick increments current page
- ✅ onNextClick does not increment when on last page
- ✅ onSkipClick navigates to last page
- ✅ Multiple page changes are tracked correctly

**State Properties:**
- ✅ isOnLastPage returns true/false correctly
- ✅ canNavigateNext returns true/false correctly
- ✅ canNavigateSkip returns true/false correctly

**Actions:**
- ✅ onGetStartedClick does not crash
- ✅ onSignInClick does not crash

**Error Handling:**
- ✅ Error state preserves empty slides list

## Testing Libraries

### Dependencies
```kotlin
// Unit Testing
testImplementation("junit:junit:4.13.2")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("com.google.truth:truth:1.1.5")
testImplementation("androidx.arch.core:core-testing:2.2.0")
```

### Tools Used:
- **JUnit 4**: Test framework
- **MockK**: Mocking framework for Kotlin
- **Coroutines Test**: Testing coroutines
- **Truth**: Fluent assertions library
- **Arch Core Testing**: LiveData testing utilities

## Running Tests

### From Android Studio:
1. Right-click on `test` directory
2. Select "Run 'Tests in 'com.mobizonetech...'"

### From Command Line:
```bash
./gradlew test
```

### Run specific test class:
```bash
./gradlew test --tests WelcomeRepositoryImplTest
./gradlew test --tests GetWelcomeSlidesUseCaseTest
./gradlew test --tests WelcomeViewModelTest
```

### Run with coverage:
```bash
./gradlew testDebugUnitTest jacocoTestReport
```

## Test Patterns

### 1. Arrange-Act-Assert (AAA)
All tests follow the AAA pattern:
```kotlin
@Test
fun `test name describes what is being tested`() = runTest {
    // Given (Arrange)
    val mockData = createMockData()
    coEvery { dependency.method() } returns mockData
    
    // When (Act)
    val result = systemUnderTest.method()
    
    // Then (Assert)
    assertThat(result).isEqualTo(expected)
}
```

### 2. Descriptive Test Names
Using backticks for readable test names:
```kotlin
`getWelcomeSlides returns Success with valid slides data`
`onNextClick increments current page when canNavigateNext is true`
```

### 3. MockK for Mocking
```kotlin
private lateinit var dependency: Dependency
dependency = mockk()
coEvery { dependency.method() } returns result
coVerify(exactly = 1) { dependency.method() }
```

### 4. Truth for Assertions
```kotlin
assertThat(result).isInstanceOf(Result.Success::class.java)
assertThat(list).hasSize(5)
assertThat(list).containsExactly(item1, item2).inOrder()
assertThat(value).isEqualTo(expected)
```

### 5. Coroutines Testing
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
@Test
fun `test with coroutines`() = runTest {
    // Test coroutine code
    testDispatcher.scheduler.advanceUntilIdle()
}
```

## Best Practices Implemented

✅ **Test Isolation**: Each test is independent
✅ **Setup/Teardown**: Using `@Before` and `@After` annotations
✅ **Mocking**: External dependencies are mocked
✅ **Descriptive Names**: Clear test names describing the scenario
✅ **Single Responsibility**: Each test verifies one behavior
✅ **Edge Cases**: Testing success, error, and edge cases
✅ **Verification**: Verifying mock interactions
✅ **Fast Tests**: No Android framework dependencies (Robolectric not needed)
✅ **Maintainable**: Helper functions for common setup

## Code Coverage Goals

Target coverage:
- **Repository Layer**: 100% (all branches covered)
- **Use Case Layer**: 100% (simple pass-through logic)
- **ViewModel Layer**: 90%+ (UI logic thoroughly tested)

## Future Test Additions

Consider adding:
1. **Integration Tests**: Test multiple layers together
2. **UI Tests**: Compose UI testing with `@Composable` functions
3. **Parameterized Tests**: Using `@ParameterizedTest` for multiple scenarios
4. **Property-Based Tests**: Using Kotest for property testing
5. **Performance Tests**: Measure execution time for critical paths

## Troubleshooting

### Common Issues:

**Issue**: Tests fail with "Method ... not mocked"
**Solution**: Add `testOptions { unitTests.returnDefaultValues = true }` to `build.gradle.kts`

**Issue**: Coroutine tests hang
**Solution**: Ensure you're using `runTest` and `advanceUntilIdle()`

**Issue**: ViewModel tests fail
**Solution**: Use `InstantTaskExecutorRule` for LiveData testing

**Issue**: MockK throws exception
**Solution**: Ensure MockK version is compatible with Kotlin version

## Continuous Integration

Tests should be run automatically in CI/CD:
```yaml
- name: Run Unit Tests
  run: ./gradlew test
  
- name: Generate Coverage Report
  run: ./gradlew jacocoTestReport
  
- name: Upload Coverage
  uses: codecov/codecov-action@v3
```

## Contributing

When adding new tests:
1. Follow the AAA pattern
2. Use descriptive test names
3. Test success and error scenarios
4. Verify mock interactions
5. Keep tests fast and isolated
6. Update this README if needed

---

**Total Tests**: 45+
**Test Success Rate**: Should be 100%
**Execution Time**: < 5 seconds for all unit tests

Last Updated: October 28, 2025

