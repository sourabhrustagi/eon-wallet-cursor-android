# =============================================================================
# EON Wallet ProGuard Rules
# =============================================================================
# These rules optimize and obfuscate the app for release builds while
# maintaining functionality and proper stack traces for debugging.
#
# For more details, see:
#   http://developer.android.com/guide/developing/tools/proguard.html
# =============================================================================

# =============================================================================
# General Android and Kotlin Rules
# =============================================================================

# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep annotations for runtime processing
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep generic type information for Kotlin
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Kotlin specific rules
-dontwarn kotlin.**
-dontwarn kotlinx.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# =============================================================================
# Jetpack Compose Rules
# =============================================================================

# Keep Composable functions and their annotations
-keep @androidx.compose.runtime.Composable class * { *; }
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep CompositionLocal providers
-keepclassmembers class * {
    @androidx.compose.runtime.ComposableTarget *;
}

# Keep Material3 components
-keep class androidx.compose.material3.** { *; }
-dontwarn androidx.compose.material3.**

# =============================================================================
# Hilt/Dagger Rules
# =============================================================================

# Keep Hilt generated classes
-keep class * extends dagger.hilt.internal.GeneratedComponent
-keep class * extends dagger.hilt.internal.GeneratedComponentManager
-keep class **_HiltModules { *; }
-keep class **_HiltModules$** { *; }
-keep class **_Impl { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# Keep @Inject annotated classes
-keepclasseswithmembers class * {
    @javax.inject.Inject <init>(...);
}

# Keep @HiltAndroidApp annotated application class
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }

# =============================================================================
# Lifecycle and ViewModel Rules
# =============================================================================

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# Keep lifecycle observers
-keepclassmembers class * implements androidx.lifecycle.LifecycleObserver {
    <init>(...);
}

# =============================================================================
# Data Classes and Models
# =============================================================================

# Keep data classes (especially for Kotlin serialization and Gson)
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers class * {
    @kotlinx.serialization.SerialName *;
}

# Keep model classes with their fields
-keepclassmembers class com.mobizonetech.aeon_wallet_cursor.domain.model.** {
    <fields>;
    <init>(...);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# =============================================================================
# Coroutines Rules
# =============================================================================

# Keep coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# =============================================================================
# Retrofit and OkHttp (if used in future)
# =============================================================================

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# =============================================================================
# Gson (if used)
# =============================================================================

-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# =============================================================================
# Coil Image Loading
# =============================================================================

-keep class coil.** { *; }
-keep interface coil.** { *; }
-dontwarn coil.**

# =============================================================================
# Accompanist Libraries
# =============================================================================

-keep class com.google.accompanist.** { *; }
-dontwarn com.google.accompanist.**

# =============================================================================
# Custom Application Classes
# =============================================================================

# Keep Application class
-keep class com.mobizonetech.aeon_wallet_cursor.EonWalletApplication { *; }

# Keep all Activities, Services, BroadcastReceivers
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# =============================================================================
# WebView (if used)
# =============================================================================

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void *(android.webkit.WebView, java.lang.String);
}

# =============================================================================
# Remove Logging (optional - uncomment to remove logs in release)
# =============================================================================

# Uncomment to remove all logging
# -assumenosideeffects class android.util.Log {
#     public static *** d(...);
#     public static *** v(...);
#     public static *** i(...);
#     public static *** w(...);
#     public static *** e(...);
# }

# =============================================================================
# Optimization
# =============================================================================

# Enable aggressive optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization options
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# =============================================================================
# Warnings to Ignore
# =============================================================================

-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**
-dontwarn java.lang.instrument.**