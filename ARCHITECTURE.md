# AEON Wallet - Android App Architecture

## Overview
This Android application has been restructured to follow modern Android development best practices with proper separation of concerns, dependency injection, and clean architecture principles.

## Architecture Components

### 1. Dependency Injection with Hilt
- **Application Class**: `AeonWalletApplication` annotated with `@HiltAndroidApp`
- **MainActivity**: Annotated with `@AndroidEntryPoint` for Hilt injection
- **AppModule**: Provides singleton dependencies for repositories and preferences
- **ViewModels**: All ViewModels use `@HiltViewModel` and `@Inject` for dependency injection

### 2. Clean Architecture Structure

```
app/src/main/java/com/mobizonetech/aeon_wallet_cursor/
├── data/
│   ├── repository/           # Repository implementations
│   │   ├── CardRepositoryImpl.kt
│   │   ├── LoanRepositoryImpl.kt
│   │   └── UserRepositoryImpl.kt
│   ├── UnlockPreferences.kt  # DataStore for unlock states
│   └── UserPreferences.kt    # DataStore for user data
├── di/
│   └── AppModule.kt          # Hilt dependency injection module
├── domain/
│   ├── model/                # Data models
│   │   ├── Card.kt
│   │   ├── Loan.kt
│   │   └── User.kt
│   ├── repository/           # Repository interfaces
│   │   └── Repository.kt
│   └── usecase/              # Use cases for business logic
│       ├── CardUseCases.kt
│       └── LoanUseCases.kt
├── presentation/
│   ├── navigation/           # Navigation components
│   │   ├── AeonWalletNavigation.kt
│   │   └── AeonWalletRoutes.kt
│   ├── screens/              # UI screens organized by feature
│   │   ├── auth/             # Authentication screens
│   │   ├── cards/            # Card-related screens
│   │   ├── crypto/           # Crypto trading screens
│   │   ├── home/             # Home and main screens
│   │   ├── loans/            # Loan-related screens
│   │   ├── notifications/    # Notification screens
│   │   ├── payments/         # Payment screens
│   │   ├── profile/          # Profile and settings screens
│   │   └── common/           # Common/shared screens
│   └── viewmodel/            # ViewModels with business logic
│       ├── AuthViewModel.kt
│       ├── CardsViewModel.kt
│       ├── HomeViewModel.kt
│       ├── LoansViewModel.kt
│       ├── NotificationsViewModel.kt
│       ├── PaymentViewModel.kt
│       ├── CardRepaymentViewModel.kt
│       ├── LoanRepaymentViewModel.kt
│       └── UtilityPaymentViewModel.kt
└── utils/
    └── RandomNameGenerator.kt
```

### 3. Jetpack Navigation
- **AeonWalletNavigation**: Centralized navigation setup using Jetpack Navigation Compose
- **AeonWalletRoutes**: Route constants for type-safe navigation
- **MainScreen**: Bottom navigation with proper state management

### 4. Repository Pattern
- **CardRepository**: Interface for card-related data operations
- **LoanRepository**: Interface for loan-related data operations  
- **UserRepository**: Interface for user-related data operations
- All repositories are implemented with proper dependency injection

### 5. ViewModel Architecture
- All ViewModels follow MVVM pattern with proper state management
- Business logic is separated from UI components
- ViewModels use StateFlow for reactive UI updates
- Proper error handling and loading states

### 6. Data Layer
- **DataStore**: Used for preferences and user data persistence
- **UnlockPreferences**: Manages card/loan unlock states
- **UserPreferences**: Manages user authentication and profile data

## Key Features Implemented

### Authentication Flow
- Welcome screen with onboarding
- ID and card number entry
- OTP verification
- Passcode and security passcode setup
- Proper state management with DataStore

### Main App Features
- **Cards**: Credit/debit card management with unlock functionality
- **Loans**: Personal, home, and car loan management
- **Payments**: Send money, request money, utility payments
- **Crypto**: Cryptocurrency trading interface
- **Profile**: User settings, feedback, contact us
- **Notifications**: Push notification management

### Navigation
- Bottom navigation for main app sections
- Deep linking support with parameterized routes
- Proper back stack management
- Type-safe navigation with route constants

## Dependencies

### Core Android
- `androidx.core:core-ktx`
- `androidx.lifecycle:lifecycle-runtime-ktx`
- `androidx.activity:activity-compose`

### Compose
- `androidx.compose.bom` (BOM for version management)
- `androidx.compose.ui`
- `androidx.compose.material3`
- `androidx.compose.ui.tooling`

### Navigation
- `androidx.navigation:navigation-compose`

### Dependency Injection
- `com.google.dagger:hilt-android`
- `androidx.hilt:hilt-navigation-compose`

### Data Persistence
- `androidx.datastore:datastore-preferences`

## Usage

### Running the App
1. Build and run the app using Android Studio
2. The app will start with the welcome screen for new users
3. Complete the authentication flow to access the main app
4. Use bottom navigation to switch between different sections

### Adding New Features
1. Create new screens in appropriate feature packages under `presentation/screens/`
2. Add corresponding ViewModels in `presentation/viewmodel/`
3. Update navigation routes in `AeonWalletRoutes.kt`
4. Add navigation composables in `AeonWalletNavigation.kt`
5. Use Hilt for dependency injection

## Architecture Benefits

1. **Separation of Concerns**: Clear separation between UI, business logic, and data layers
2. **Testability**: ViewModels and repositories can be easily unit tested
3. **Maintainability**: Organized package structure makes code easy to navigate and maintain
4. **Scalability**: Clean architecture allows for easy addition of new features
5. **Type Safety**: Navigation routes are type-safe and centralized
6. **Dependency Injection**: Hilt provides clean dependency management
7. **Reactive UI**: StateFlow ensures UI updates reactively to data changes

This architecture follows Android's recommended practices and provides a solid foundation for a production-ready financial application.
