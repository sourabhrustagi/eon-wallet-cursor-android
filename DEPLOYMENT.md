# Deployment Guide - EON Wallet Android

Complete guide for building, testing, and deploying the EON Wallet Android application.

---

## 📋 Table of Contents

- [Build Types](#build-types)
- [Pre-Deployment Checklist](#pre-deployment-checklist)
- [Building for Production](#building-for-production)
- [Google Play Store](#google-play-store)
- [Internal Testing](#internal-testing)
- [Beta Testing](#beta-testing)
- [Production Release](#production-release)
- [Post-Deployment](#post-deployment)

---

## 🏗️ Build Types

### Debug Build

**Purpose**: Development and testing

```bash
# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Location
app/build/outputs/apk/debug/app-debug.apk
```

**Features:**
- ✅ Debuggable
- ✅ Logging enabled
- ✅ Mock API enabled
- ✅ No code obfuscation
- ❌ Not production-ready

### Release Build

**Purpose**: Production deployment

```bash
# Build release APK
./gradlew assembleRelease

# Build release AAB (for Play Store)
./gradlew bundleRelease

# Locations
app/build/outputs/apk/release/app-release.apk
app/build/outputs/bundle/release/app-release.aab
```

**Features:**
- ❌ Not debuggable
- ❌ Minimal logging
- ❌ Mock API disabled
- ✅ Code obfuscation (ProGuard/R8)
- ✅ Optimized
- ✅ Signed

---

## ✅ Pre-Deployment Checklist

### Code Quality

```bash
# Run all tests
./gradlew test connectedAndroidTest

# Expected: All tests passing (175+)
# ✅ Unit Tests: 130+
# ✅ UI Tests: 45+
```

```bash
# Run lint checks
./gradlew lint

# Expected: No errors, minimal warnings
```

```bash
# Check code style
# File → Code → Inspect Code
# Expected: No critical issues
```

### Version Management

Update version in `app/build.gradle.kts`:

```kotlin
android {
    defaultConfig {
        versionCode = 2  // Increment for each release
        versionName = "1.0.1"  // Semantic versioning
    }
}
```

**Version Strategy:**
- **versionCode**: Integer, increment by 1 for each release
- **versionName**: String, semantic versioning (MAJOR.MINOR.PATCH)
  - MAJOR: Breaking changes
  - MINOR: New features
  - PATCH: Bug fixes

### Configuration Updates

**1. Update API endpoints:**
```kotlin
// NetworkModule.kt
const val BASE_URL = "https://api.aeonwallet.com/"  // Production URL
```

**2. Disable mock API:**
```kotlin
// MockInterceptor.kt
val mockInterceptor = MockInterceptor(enabled = false)
```

**3. Update analytics:**
```kotlin
// AnalyticsModule.kt
// Switch from MockAnalytics to real analytics
```

**4. Update app configuration:**
```kotlin
// Constants.kt
const val DEBUG_MODE = false
const val ENABLE_LOGGING = false
```

### Signing Configuration

Create `keystore.properties` in project root:

```properties
storeFile=../keystores/aeon-wallet-release.jks
storePassword=STORE_PASSWORD
keyAlias=aeon-wallet
keyPassword=KEY_PASSWORD
```

**Generate keystore** (first time only):
```bash
keytool -genkey -v \
  -keystore keystores/aeon-wallet-release.jks \
  -alias aeon-wallet \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

⚠️ **Important**: Backup keystore file securely!

### ProGuard Configuration

Verify `proguard-rules.pro` is complete:

```proguard
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep your models
-keep class com.mobizonetech.aeon_wallet_cursor.domain.model.** { *; }
-keep class com.mobizonetech.aeon_wallet_cursor.data.remote.dto.** { *; }
```

### Documentation

- [ ] Update CHANGELOG.md
- [ ] Update version in README.md
- [ ] Update API documentation
- [ ] Update user-facing docs

### Testing

```bash
# Clean build
./gradlew clean

# Run all tests
./gradlew test

# Run UI tests on multiple devices
./gradlew connectedAndroidTest

# Manual testing checklist
- [ ] Fresh install
- [ ] Update from previous version
- [ ] All features work
- [ ] No crashes
- [ ] Performance acceptable
```

---

## 🚀 Building for Production

### Step 1: Prepare Environment

```bash
# Switch to main branch
git checkout main

# Pull latest changes
git pull origin main

# Clean workspace
./gradlew clean
```

### Step 2: Update Version

```kotlin
// app/build.gradle.kts
defaultConfig {
    versionCode = 2  // Increment
    versionName = "1.0.1"  // Update
}
```

### Step 3: Build Release

```bash
# Build signed APK
./gradlew assembleRelease

# Or build AAB for Play Store
./gradlew bundleRelease
```

### Step 4: Verify Build

```bash
# Check outputs
ls -lh app/build/outputs/apk/release/
ls -lh app/build/outputs/bundle/release/

# Install and test (if APK)
adb install app/build/outputs/apk/release/app-release.apk
```

### Step 5: Generate Mapping Files

ProGuard/R8 generates mapping files for stack trace deobfuscation:

```
app/build/outputs/mapping/release/
├── mapping.txt          # ProGuard mapping
├── seeds.txt            # Entry points
└── usage.txt            # Removed code
```

⚠️ **Important**: Save `mapping.txt` for each release!

---

## 📱 Google Play Store

### First-Time Setup

1. **Create Developer Account**
   - Visit [Google Play Console](https://play.google.com/console)
   - Pay $25 one-time registration fee

2. **Create App**
   - Click "Create app"
   - Fill in app details
   - Set up store listing

3. **Complete Store Listing**
   - App name: "EON Wallet"
   - Short description (80 chars)
   - Full description (4000 chars)
   - Screenshots (phone, tablet)
   - Feature graphic
   - App icon

### App Content

- [ ] Privacy Policy URL
- [ ] Data safety declarations
- [ ] Target audience & content
- [ ] App category
- [ ] Contact details

### Pricing & Distribution

- [ ] Countries/regions
- [ ] Pricing (Free/Paid)
- [ ] Device categories
- [ ] User programs

---

## 🧪 Internal Testing

### Setup Internal Testing Track

1. **Create Internal Testing Release**
   - Play Console → Releases → Internal Testing
   - Upload AAB
   - Add release notes

2. **Add Testers**
   - Email list
   - Google Groups
   - Max 100 testers

3. **Share Test Link**
   - Copy opt-in URL
   - Share with testers

### Testing Checklist

```markdown
- [ ] Install successfully
- [ ] Fresh install works
- [ ] Update from previous version works
- [ ] All features functional
- [ ] No crashes
- [ ] Performance good
- [ ] UI looks correct
- [ ] Network errors handled
- [ ] Analytics working
```

---

## 🐛 Beta Testing (Closed/Open)

### Closed Beta

**Purpose**: Larger group testing (up to 2,000 testers)

1. **Create Closed Beta Release**
   - Play Console → Closed Testing
   - Upload AAB
   - Set up testing audience

2. **Invite Testers**
   - Email lists
   - Testing groups

### Open Beta

**Purpose**: Public testing (unlimited testers)

1. **Create Open Beta Release**
   - Play Console → Open Testing
   - Upload AAB
   - Anyone can join

2. **Collect Feedback**
   - Monitor reviews
   - Track crash reports
   - Analyze usage

---

## 🎉 Production Release

### Step 1: Create Production Release

1. **Play Console → Production**
2. **Create New Release**
3. **Upload AAB**
4. **Add Release Notes**

Example release notes:
```markdown
Version 1.0.1

New Features:
- Automatic API retry with exponential backoff
- Improved error handling
- Enhanced analytics tracking

Improvements:
- Better performance
- UI polish
- Bug fixes

Bug Fixes:
- Fixed crash on network error
- Resolved loading state issues
```

### Step 2: Set Rollout Percentage

Start with gradual rollout:
- Day 1: 5% of users
- Day 2: 10%
- Day 3: 25%
- Day 4: 50%
- Day 5: 100%

### Step 3: Review & Submit

1. **Review release**
   - Verify AAB
   - Check release notes
   - Confirm countries

2. **Submit for review**
   - Google reviews (1-3 days)
   - Wait for approval

### Step 4: Monitor Launch

```bash
# Monitor key metrics
- Install rate
- Crash rate
- ANR rate
- Ratings/reviews
- Active users
```

### Step 5: Increase Rollout

If metrics look good:
- Increase rollout percentage
- Monitor after each increase
- Halt if issues detected

---

## 📊 Post-Deployment

### Monitoring

**1. Crashlytics/Firebase**
```
- Monitor crash rates
- Review stack traces
- Track ANRs
- Analyze crashes by version
```

**2. Play Console Vitals**
```
- Crash rate < 1%
- ANR rate < 0.5%
- Excessive wakeups
- Stuck wake locks
```

**3. User Feedback**
```
- Monitor ratings
- Respond to reviews
- Track common complaints
- Collect feature requests
```

### Hotfix Process

If critical bug found:

```bash
# 1. Create hotfix branch
git checkout -b hotfix/critical-bug
git push origin hotfix/critical-bug

# 2. Fix bug
# ... implement fix
# ... add regression test

# 3. Bump version
versionCode = 3
versionName = "1.0.2"

# 4. Build & test
./gradlew clean test assembleRelease

# 5. Create hotfix release
# Upload to Play Console
# Use "Production" track
# Fast rollout (25% → 100%)

# 6. Merge back
git checkout main
git merge hotfix/critical-bug
git push origin main
```

### Analytics Review

```bash
# Weekly review
- User acquisition
- User retention
- Feature usage
- Error rates
- Performance metrics
- Conversion rates
```

### Version Maintenance

```bash
# Keep multiple versions supported
- Latest: 1.0.2 (95% of users)
- Previous: 1.0.1 (4% of users)
- Older: 1.0.0 (1% of users)

# Deprecation policy
- Support N-2 versions
- Force update after 6 months
```

---

## 🔄 Continuous Deployment

### CI/CD Pipeline (Optional)

**Using GitHub Actions:**

`.github/workflows/deploy.yml`:
```yaml
name: Deploy to Play Store

on:
  push:
    tags:
      - 'v*'

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
      
      - name: Run tests
        run: ./gradlew test
      
      - name: Build release AAB
        run: ./gradlew bundleRelease
        env:
          SIGNING_KEY: ${{ secrets.SIGNING_KEY }}
          SIGNING_PASSWORD: ${{ secrets.SIGNING_PASSWORD }}
      
      - name: Upload to Play Store
        uses: r0adkll/upload-google-play@v1
        with:
          serviceAccountJsonPlainText: ${{ secrets.SERVICE_ACCOUNT_JSON }}
          packageName: com.mobizonetech.aeon_wallet_cursor
          releaseFiles: app/build/outputs/bundle/release/app-release.aab
          track: internal
```

---

## 📝 Checklist Summary

### Pre-Release
- [ ] All tests passing
- [ ] Lint check clean
- [ ] Version updated
- [ ] Configuration updated
- [ ] ProGuard configured
- [ ] Keystore ready
- [ ] Documentation updated
- [ ] Manual testing completed

### Build
- [ ] Clean workspace
- [ ] Build release AAB
- [ ] Verify signing
- [ ] Test installation
- [ ] Save mapping files

### Deploy
- [ ] Upload to Play Console
- [ ] Set release notes
- [ ] Configure rollout
- [ ] Submit for review
- [ ] Monitor approval

### Post-Release
- [ ] Monitor crashes
- [ ] Review vitals
- [ ] Read user feedback
- [ ] Track metrics
- [ ] Plan next release

---

## 🆘 Troubleshooting

### Build Issues

**Problem**: Build fails with signing error
```bash
# Solution: Check keystore.properties
# Verify keystore file exists
# Check passwords are correct
```

**Problem**: ProGuard removes needed classes
```bash
# Solution: Add -keep rules
# Check mapping.txt
# Test with ProGuard enabled
```

### Deployment Issues

**Problem**: Upload rejected
```bash
# Solution: Check version code
# Must be higher than previous
# Verify signing certificate matches
```

**Problem**: Store listing incomplete
```bash
# Solution: Complete all required fields
# Add all required graphics
# Fill in privacy policy
```

---

## 📚 Resources

- [Play Console Help](https://support.google.com/googleplay/android-developer)
- [Android App Bundles](https://developer.android.com/guide/app-bundle)
- [ProGuard Manual](https://www.guardsquare.com/manual)
- [Play Store Policies](https://play.google.com/about/developer-content-policy/)

---

*Last Updated: October 30, 2025*  
*Deployment Guide Version: 1.0.0*

