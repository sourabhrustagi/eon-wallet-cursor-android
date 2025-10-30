# Contributing to EON Wallet Android

Thank you for your interest in contributing to EON Wallet! This document provides guidelines and instructions for contributing to the project.

---

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Testing Requirements](#testing-requirements)
- [Documentation](#documentation)

---

## 🤝 Code of Conduct

### Our Pledge

We are committed to providing a welcoming and inspiring community for all.

### Our Standards

**Positive behavior includes:**
- Using welcoming and inclusive language
- Being respectful of differing viewpoints
- Gracefully accepting constructive criticism
- Focusing on what is best for the community
- Showing empathy towards other community members

**Unacceptable behavior includes:**
- Harassment of any kind
- Publishing others' private information
- Other conduct which could reasonably be considered inappropriate

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Latest stable version
- **JDK**: Version 11 or later
- **Git**: Version control
- **Android SDK**: API 26-34

### Fork and Clone

```bash
# Fork the repository on GitHub
# Then clone your fork
git clone https://github.com/YOUR_USERNAME/aeon-wallet-android.git
cd aeon-wallet-android

# Add upstream remote
git remote add upstream https://github.com/mobizonetech/aeon-wallet-android.git
```

### Sync with Upstream

```bash
# Fetch upstream changes
git fetch upstream

# Merge upstream changes
git merge upstream/main
```

---

## 💻 Development Setup

### 1. Install Dependencies

```bash
# Sync Gradle
./gradlew clean build

# Or in Android Studio
File -> Sync Project with Gradle Files
```

### 2. Run the App

```bash
# Debug build
./gradlew installDebug

# Or press Shift+F10 in Android Studio
```

### 3. Run Tests

```bash
# Unit tests
./gradlew test

# UI tests
./gradlew connectedAndroidTest
```

---

## 📝 Coding Standards

### Kotlin Style Guide

Follow the [Official Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)

#### Key Points:

**Naming:**
```kotlin
// Classes: PascalCase
class WelcomeViewModel

// Functions: camelCase
fun loadWelcomeSlides()

// Constants: UPPER_SNAKE_CASE
const val MAX_RETRIES = 3

// Variables: camelCase
val welcomeSlides = listOf(...)
```

**Formatting:**
```kotlin
// 4 spaces for indentation
class Example {
    fun method() {
        if (condition) {
            doSomething()
        }
    }
}

// Line length: 120 characters max
```

**Documentation:**
```kotlin
/**
 * Brief description
 * 
 * Detailed description with:
 * - Key points
 * - Usage examples
 * 
 * @param parameter Parameter description
 * @return Return value description
 */
fun exampleFunction(parameter: String): Boolean {
    // Implementation
}
```

### Architecture Guidelines

**Clean Architecture:**
- Keep layers separate
- Dependencies point inward
- Use dependency injection

**File Organization:**
```
presentation/
├── screens/        # UI composables
├── viewmodel/      # ViewModels
└── state/          # UI state classes

domain/
├── model/          # Domain models
├── repository/     # Repository interfaces
└── usecase/        # Use cases

data/
├── remote/         # API, DTOs
├── repository/     # Repository implementations
└── mapper/         # Data mappers
```

---

## 📝 Commit Guidelines

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting)
- **refactor**: Code refactoring
- **test**: Adding/updating tests
- **chore**: Build/tooling changes

### Examples

```bash
# Good commits
git commit -m "feat(welcome): add retry mechanism for API calls"
git commit -m "fix(viewmodel): correct state update logic"
git commit -m "test(repository): add comprehensive error handling tests"
git commit -m "docs(readme): update installation instructions"

# Bad commits
git commit -m "fixed stuff"
git commit -m "WIP"
git commit -m "asdfasdf"
```

### Detailed Example

```
feat(retry): implement exponential backoff for API calls

- Add RetryPolicy utility class
- Integrate retry logic in repositories
- Add comprehensive unit tests
- Update documentation

Closes #123
```

---

## 🔄 Pull Request Process

### Before Submitting

1. **Create a Feature Branch**
```bash
git checkout -b feature/your-feature-name
```

2. **Make Your Changes**
- Follow coding standards
- Add tests
- Update documentation

3. **Test Everything**
```bash
# Run all tests
./gradlew test connectedAndroidTest

# Check linting
./gradlew lint
```

4. **Commit Your Changes**
```bash
git add .
git commit -m "feat: your feature description"
```

5. **Push to Your Fork**
```bash
git push origin feature/your-feature-name
```

### Submitting the PR

1. **Create Pull Request** on GitHub
2. **Fill out the template** completely
3. **Link related issues** (e.g., "Closes #123")
4. **Request reviews** from maintainers

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] UI tests added/updated
- [ ] Manual testing completed

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] Tests pass locally
- [ ] No new warnings generated

## Screenshots (if applicable)
[Add screenshots here]

## Related Issues
Closes #issue_number
```

### Review Process

1. **Automated Checks**: CI/CD runs tests and linting
2. **Code Review**: Maintainers review your code
3. **Address Feedback**: Make requested changes
4. **Approval**: Once approved, PR will be merged

---

## 🧪 Testing Requirements

### Required Tests

**All code changes must include:**
- ✅ Unit tests for business logic
- ✅ UI tests for user-facing features
- ✅ Edge case testing
- ✅ Error scenario testing

### Test Coverage

**Minimum coverage:**
- Repositories: 100%
- Use Cases: 100%
- ViewModels: 100%
- UI Components: 80%+

### Writing Tests

**Unit Test Example:**
```kotlin
@Test
fun `feature should work correctly`() = runTest {
    // Given - Setup
    val input = createTestInput()
    
    // When - Execute
    val result = useCase.execute(input)
    
    // Then - Verify
    assertThat(result).isEqualTo(expectedOutput)
}
```

**UI Test Example:**
```kotlin
@Test
fun screen_displaysCorrectly() {
    // Given
    composeTestRule.setContent {
        MyScreen()
    }
    
    // Then
    composeTestRule
        .onNodeWithText("Expected Text")
        .assertIsDisplayed()
}
```

### Running Tests

```bash
# All tests
./gradlew test connectedAndroidTest

# Specific test class
./gradlew test --tests "*WelcomeViewModelTest"

# Specific test method
./gradlew test --tests "*WelcomeViewModelTest.`initial state is loading`"
```

---

## 📚 Documentation

### Required Documentation

**For new features:**
- [ ] Code comments for complex logic
- [ ] KDoc for public APIs
- [ ] README updates if needed
- [ ] Architecture documentation updates
- [ ] User-facing documentation

### Documentation Style

```kotlin
/**
 * Brief one-line description
 * 
 * Detailed multi-line description explaining:
 * - What the component does
 * - When to use it
 * - Important considerations
 * 
 * Example usage:
 * ```kotlin
 * val result = myFunction(param1, param2)
 * ```
 * 
 * @param param1 Description of parameter
 * @param param2 Description of parameter
 * @return Description of return value
 * @throws ExceptionType When this exception is thrown
 * 
 * @see RelatedClass
 * @since 1.0.0
 */
fun myFunction(param1: String, param2: Int): Result<Data>
```

---

## 🎯 Feature Development Workflow

### 1. Planning

- Discuss feature in GitHub Issues
- Get approval from maintainers
- Define acceptance criteria

### 2. Implementation

```bash
# Create feature branch
git checkout -b feature/my-feature

# Implement feature
# - Write code
# - Add tests
# - Update docs

# Commit changes
git commit -m "feat: implement my feature"
```

### 3. Testing

```bash
# Run all tests
./gradlew test connectedAndroidTest

# Manual testing on emulator/device
./gradlew installDebug
```

### 4. Review

- Create pull request
- Address review comments
- Ensure CI passes

### 5. Merge

- Maintainer merges PR
- Feature available in main branch

---

## 🐛 Bug Fix Workflow

### 1. Report

- Check existing issues
- Create new issue if needed
- Provide reproduction steps

### 2. Fix

```bash
# Create fix branch
git checkout -b fix/bug-description

# Fix the bug
# Add regression tests

# Commit fix
git commit -m "fix: resolve bug description"
```

### 3. Test

```bash
# Verify fix
./gradlew test

# Test manually
./gradlew installDebug
```

### 4. Submit

- Create pull request
- Reference original issue
- Explain the fix

---

## ⚡ Quick Reference

### Common Commands

```bash
# Build
./gradlew build

# Test
./gradlew test

# Lint
./gradlew lint

# Install debug
./gradlew installDebug

# Clean
./gradlew clean
```

### File Locations

```
Source code:       app/src/main/java/
Unit tests:        app/src/test/java/
UI tests:          app/src/androidTest/java/
Resources:         app/src/main/res/
Documentation:     *.md files in root
```

---

## 💬 Getting Help

### Resources

- **Documentation**: See `docs/` folder
- **GitHub Issues**: Report bugs/request features
- **GitHub Discussions**: Ask questions
- **Code Review**: Learn from existing PRs

### Questions?

- Create a GitHub Discussion
- Ask in pull request comments
- Email: support@mobizonetech.com

---

## 🏆 Recognition

Contributors will be recognized in:
- CONTRIBUTORS.md file
- Release notes
- Project README

---

## 📄 License

By contributing, you agree that your contributions will be licensed under the project's license.

---

Thank you for contributing to EON Wallet! 🙏

---

*Last Updated: October 30, 2025*

