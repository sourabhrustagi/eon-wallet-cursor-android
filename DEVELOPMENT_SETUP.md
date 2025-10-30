# Development Environment Setup

Complete guide for setting up your development environment for EON Wallet Android.

---

## 📋 Prerequisites

### Required Software

| Software | Version | Download |
|----------|---------|----------|
| **Android Studio** | Hedgehog (2023.1.1)+ | [Download](https://developer.android.com/studio) |
| **JDK** | 11+ | [Download](https://adoptium.net/) |
| **Android SDK** | API 26-34 | Via Android Studio |
| **Git** | Latest | [Download](https://git-scm.com/) |
| **Kotlin Plugin** | 1.9.0+ | Pre-installed in AS |

### System Requirements

**Minimum:**
- OS: Windows 10/11, macOS 10.14+, or Linux
- RAM: 8GB
- Storage: 10GB free space
- CPU: Intel i5 or equivalent

**Recommended:**
- RAM: 16GB+
- Storage: SSD with 20GB+ free space
- CPU: Intel i7/M1 or equivalent

---

## 🔧 Installation Steps

### 1. Install Android Studio

#### macOS
```bash
# Download from website or use Homebrew
brew install --cask android-studio
```

#### Linux
```bash
# Download tar.gz from website
sudo tar -xzf android-studio-*.tar.gz -C /opt/
/opt/android-studio/bin/studio.sh
```

#### Windows
```powershell
# Download and run installer
# Install to: C:\Program Files\Android\Android Studio
```

### 2. Configure Android SDK

1. Open Android Studio
2. **Tools** → **SDK Manager**
3. Install required SDK platforms:
   - ✅ Android 14.0 (API 34) - Target
   - ✅ Android 8.0 (API 26) - Minimum
4. Install SDK tools:
   - ✅ Android SDK Build-Tools 34
   - ✅ Android Emulator
   - ✅ Android SDK Platform-Tools
   - ✅ Android SDK Tools

### 3. Install JDK 11

#### macOS/Linux
```bash
# Check existing Java
java -version

# Install OpenJDK 11 (if needed)
# macOS
brew install openjdk@11

# Ubuntu/Debian
sudo apt install openjdk-11-jdk

# Configure JAVA_HOME
export JAVA_HOME=/path/to/jdk-11
export PATH=$JAVA_HOME/bin:$PATH
```

#### Windows
```powershell
# Download from Adoptium
# Install and set JAVA_HOME

# Environment Variables
JAVA_HOME=C:\Program Files\Java\jdk-11
Path=%JAVA_HOME%\bin;%Path%
```

### 4. Clone Repository

```bash
# Clone the repository
git clone https://github.com/mobizonetech/aeon-wallet-android.git
cd aeon-wallet-android

# Configure upstream (if forked)
git remote add upstream https://github.com/mobizonetech/aeon-wallet-android.git
```

### 5. Open in Android Studio

```bash
# Option 1: Command line
studio .  # macOS/Linux
# or
studio.bat .  # Windows

# Option 2: Android Studio UI
File → Open → Select project folder
```

### 6. First Build

```bash
# Sync Gradle
./gradlew clean build

# Or in Android Studio
File → Sync Project with Gradle Files
```

---

## 🎨 Android Studio Configuration

### 1. Code Style

**File** → **Settings** → **Editor** → **Code Style** → **Kotlin**

```
Tabs and Indents:
- Tab size: 4
- Indent: 4
- Continuation indent: 4

Wrapping and Braces:
- Hard wrap at: 120
- Keep when reformatting: ✅ All options

Imports:
- ❌ Use single name import
- ✅ Use import with '*' for: 5
```

### 2. Inspections

**File** → **Settings** → **Editor** → **Inspections**

Enable:
- ✅ Kotlin → Probable bugs
- ✅ Kotlin → Style issues
- ✅ Android → Lint → Performance
- ✅ Android → Lint → Correctness

### 3. Plugins

Install recommended plugins:
```
Settings → Plugins → Marketplace

Required:
- ✅ Kotlin (pre-installed)
- ✅ Android (pre-installed)

Recommended:
- ✅ .ignore
- ✅ Rainbow Brackets
- ✅ Key Promoter X
- ✅ GitToolBox
```

### 4. Emulator Setup

**Tools** → **Device Manager** → **Create Virtual Device**

Recommended configuration:
```
Device: Pixel 6
System Image: Android 14.0 (API 34) - x86_64
RAM: 2048 MB
Internal Storage: 2048 MB
```

---

## 🔐 Environment Configuration

### 1. local.properties

Create `local.properties` in project root:

```properties
## Android SDK location
sdk.dir=/Users/username/Library/Android/sdk

## Optional: Custom properties
# Debug settings
DEBUG_MODE=true
MOCK_API_ENABLED=true

# API Configuration
API_BASE_URL=https://api.aeonwallet.com/
API_TIMEOUT=30

# Analytics
ANALYTICS_ENABLED=true
```

### 2. Signing Configuration (Optional)

For release builds, create `keystore.properties`:

```properties
storeFile=/path/to/keystore.jks
storePassword=your_store_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

---

## 🧪 Testing Setup

### 1. Unit Tests

No additional setup required. Run with:
```bash
./gradlew test
```

### 2. Instrumented Tests

Requires emulator or physical device:

```bash
# List devices
adb devices

# Run tests
./gradlew connectedAndroidTest
```

### 3. Test Configuration

**Run** → **Edit Configurations** → **Android Instrumented Tests**

```
Name: All Instrumented Tests
Module: app
Class: Leave empty (runs all)
Use classpath of module: app
```

---

## 🛠️ Gradle Configuration

### 1. Gradle Wrapper

Verify Gradle wrapper:
```bash
# Check version
./gradlew --version

# Update if needed
./gradlew wrapper --gradle-version=8.2
```

### 2. Gradle Properties

Edit `gradle.properties`:

```properties
# Performance
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true
org.gradle.configureondemand=true

# Kotlin
kotlin.code.style=official
kotlin.incremental=true
kotlin.incremental.java=true
kotlin.incremental.js=true

# Android
android.useAndroidX=true
android.enableJetifier=true
android.nonTransitiveRClass=true
android.nonFinalResIds=true
```

### 3. Build Cache

Enable build cache for faster builds:

```bash
# Create cache directory
mkdir -p ~/.gradle/caches

# Verify in gradle.properties
org.gradle.caching=true
```

---

## 📱 Device Configuration

### Physical Device

1. **Enable Developer Options**:
   - Settings → About Phone → Tap "Build Number" 7 times

2. **Enable USB Debugging**:
   - Settings → Developer Options → USB Debugging

3. **Connect Device**:
   ```bash
   # Verify connection
   adb devices
   
   # Should show: device_id    device
   ```

### Emulator

```bash
# List available AVDs
emulator -list-avds

# Start emulator
emulator -avd Pixel_6_API_34

# Or use Android Studio Device Manager
```

---

## 🔍 Debugging Tools

### 1. Android Studio Profiler

**View** → **Tool Windows** → **Profiler**

Monitor:
- CPU usage
- Memory allocation
- Network activity
- Energy consumption

### 2. Layout Inspector

**Tools** → **Layout Inspector**

View:
- View hierarchy
- Layout properties
- Compose tree

### 3. Logcat

**View** → **Tool Windows** → **Logcat**

Filters:
```
# App logs only
package:com.mobizonetech.aeon_wallet_cursor

# Error level
level:error

# Custom tag
tag:RetryPolicy
```

---

## 🚀 Build & Run

### Debug Build

```bash
# Command line
./gradlew assembleDebug
./gradlew installDebug

# Or Android Studio
Run → Run 'app' (Shift+F10)
```

### Release Build

```bash
# Build APK
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk

# Build Bundle (for Play Store)
./gradlew bundleRelease

# Output: app/build/outputs/bundle/release/app-release.aab
```

### Clean Build

```bash
# Clean project
./gradlew clean

# Clean + rebuild
./gradlew clean build
```

---

## 🐛 Troubleshooting

### Common Issues

**Issue: Gradle sync fails**
```bash
# Solution
./gradlew clean
File → Invalidate Caches → Invalidate and Restart
```

**Issue: SDK not found**
```bash
# Solution
# Set SDK location in local.properties
sdk.dir=/path/to/Android/sdk
```

**Issue: JDK version mismatch**
```bash
# Solution
# In Android Studio:
File → Project Structure → SDK Location → JDK location
# Select JDK 11
```

**Issue: Emulator won't start**
```bash
# Solution
# Enable Intel HAXM (Intel) or Hypervisor (AMD)
SDK Manager → SDK Tools → Intel x86 Emulator Accelerator
```

**Issue: Tests fail with "No connected devices"**
```bash
# Solution
# Start emulator first
adb devices  # Verify device is connected
```

### Build Performance

**Slow builds?**

1. **Increase Gradle memory**:
```properties
# gradle.properties
org.gradle.jvmargs=-Xmx8192m
```

2. **Enable parallel builds**:
```properties
org.gradle.parallel=true
```

3. **Use build cache**:
```properties
org.gradle.caching=true
```

4. **Disable unnecessary modules**:
   - Only sync modules you're working on

---

## 📊 Verification

### Verify Setup

Run this checklist:

```bash
# 1. Java version
java -version
# Expected: openjdk version "11.0.x"

# 2. Gradle version
./gradlew --version
# Expected: Gradle 8.2

# 3. Android SDK
echo $ANDROID_HOME
# Expected: /path/to/Android/sdk

# 4. ADB
adb version
# Expected: Android Debug Bridge version 34.x.x

# 5. Build project
./gradlew assembleDebug
# Expected: BUILD SUCCESSFUL

# 6. Run tests
./gradlew test
# Expected: 130+ tests passing

# 7. Lint check
./gradlew lint
# Expected: No errors
```

### First Run Checklist

- [ ] Android Studio installed
- [ ] JDK 11 configured
- [ ] Android SDK installed (API 26-34)
- [ ] Project cloned
- [ ] Gradle sync successful
- [ ] Build successful
- [ ] Tests passing
- [ ] Emulator/Device working
- [ ] App runs successfully

---

## 📚 Additional Resources

### Documentation
- [Android Developer Docs](https://developer.android.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)

### Tools
- [Android Studio Tips](https://developer.android.com/studio/intro)
- [ADB Commands](https://developer.android.com/studio/command-line/adb)
- [Gradle Build Cache](https://docs.gradle.org/current/userguide/build_cache.html)

---

## ✅ Next Steps

After setup:

1. **Read Documentation**
   - [README.md](README.md)
   - [ARCHITECTURE.md](ARCHITECTURE.md)
   - [CONTRIBUTING.md](CONTRIBUTING.md)

2. **Explore Codebase**
   - Run the app
   - Browse code structure
   - Read existing tests

3. **Start Developing**
   - Create feature branch
   - Implement changes
   - Add tests
   - Submit PR

---

*Last Updated: October 30, 2025*  
*Environment Version: 1.0.0*

