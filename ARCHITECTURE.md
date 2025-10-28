# Aeon Wallet - Clean Architecture

## Overview
This application follows Clean Architecture principles, ensuring separation of concerns and maintainability.

## Architecture Layers

### 1. **Domain Layer** (Business Logic)
Contains the core business logic and is independent of any framework.

```
domain/
├── model/
│   └── WelcomeSlide.kt          # Domain entities
└── usecase/
    └── GetWelcomeSlidesUseCase.kt # Business use cases
```

**Responsibilities:**
- Define business entities (WelcomeSlide)
- Implement business rules and use cases
- No dependencies on Android or UI frameworks

**Files:**
- `domain/model/WelcomeSlide.kt` - Data model for welcome screen slides
- `domain/usecase/GetWelcomeSlidesUseCase.kt` - Use case to retrieve welcome slides

---

### 2. **Presentation Layer** (UI & State Management)
Handles UI and user interactions using Jetpack Compose and ViewModel.

```
presentation/
├── screens/
│   └── WelcomeScreen.kt          # UI composables
└── viewmodel/
    ├── WelcomeViewModel.kt       # State management
    └── WelcomeViewModelFactory.kt # ViewModel factory
```

**Responsibilities:**
- Manage UI state with ViewModels
- Render UI with Jetpack Compose
- Handle user interactions
- Observable data flow with StateFlow

**Files:**
- `presentation/screens/WelcomeScreen.kt` - Main welcome screen composable
- `presentation/viewmodel/WelcomeViewModel.kt` - Manages welcome screen state
- `presentation/viewmodel/WelcomeViewModelFactory.kt` - Factory for creating ViewModels with dependencies

---

### 3. **UI Theme Layer** (Appearance)
Contains theming and styling configurations.

```
ui/
└── theme/
    ├── Color.kt                  # Color definitions
    ├── Theme.kt                  # Material Design 3 theme
    └── Type.kt                   # Typography
```

**Responsibilities:**
- Define app-wide colors, typography, and theme
- Implement Material Design 3 guidelines

---

## Data Flow

```
MainActivity
    ↓
WelcomeScreen (UI)
    ↓
WelcomeViewModel (State Management)
    ↓
GetWelcomeSlidesUseCase (Business Logic)
    ↓
Domain Model (WelcomeSlide)
```

## Key Principles

1. **Dependency Rule**: Inner layers (Domain) don't know about outer layers (Presentation)
2. **Single Responsibility**: Each class has one reason to change
3. **Testability**: Business logic is separated from UI and frameworks
4. **Maintainability**: Clear separation makes code easier to understand and modify

## Benefits

✅ **Testable**: Business logic can be tested independently  
✅ **Maintainable**: Clear structure makes code easier to navigate  
✅ **Scalable**: Easy to add new features without breaking existing code  
✅ **Framework Independent**: Domain layer can be reused across platforms

## Future Enhancements

The current structure supports easy expansion for:
- **Data Layer**: Add repositories and data sources
- **Dependency Injection**: Integrate Hilt for DI
- **Navigation**: Add Navigation Compose for multi-screen flows
- **State Management**: Add more ViewModels as features grow
