plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("jacoco")
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("com.diffplug.spotless") version "6.25.0"
}

android {
    namespace = "com.mobizonetech.aeon_wallet_cursor"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mobizonetech.aeon_wallet_cursor"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Enable vector drawable support for API < 21
        vectorDrawables {
            useSupportLibrary = true
        }
        
        // ProGuard optimization flags for release builds
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }

    buildTypes {
        debug {
            // Debug-specific configuration
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        
        release {
            // Enable code shrinking, obfuscation, and optimization
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            // Signing configuration should be added for production
            // signingConfig = signingConfigs.getByName("release")
        }
    }

    flavorDimensions += listOf("environment")
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"https://dev.api.aeonwallet.com/\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "true")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "BASE_URL", "\"https://staging.api.aeonwallet.com/\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "true")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://api.aeonwallet.com/\"")
            buildConfigField("Boolean", "ENABLE_LOGGING", "false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        
        // Enable desugaring for newer Java APIs on older Android versions
        isCoreLibraryDesugaringEnabled = true
    }
    
    kotlinOptions {
        jvmTarget = "11"
        
        // Enable experimental Kotlin compiler features for better performance
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
        // Disable unused features to improve build performance
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }
    
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
        }
    }
    
    lint {
        enable += listOf("Interoperability")
        checkReleaseBuilds = true
        abortOnError = false
    }
}

// Jacoco coverage configuration
tasks.withType<Test> {
    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.register<JacocoReport>("jacocoTestReport") {
    // Align with the 'devDebug' flavor/buildType
    dependsOn(tasks.named("testDevDebugUnitTest"))
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )
    val javaTree = fileTree(layout.buildDirectory.dir("intermediates/javac/devDebug/classes")) { exclude(fileFilter) }
    val kotlinTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/devDebug")) { exclude(fileFilter) }
    classDirectories.setFrom(files(javaTree, kotlinTree))
    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("**/jacoco/testDevDebugUnitTest.exec")
        include("**/outputs/unit_test_code_coverage/**/testDevDebugUnitTest.exec")
    })
}

// Detekt configuration
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files(rootProject.file("detekt.yml")))
    autoCorrect = true
}

// Spotless configuration
spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("0.50.0").editorConfigOverride(mapOf(
            "ktlint_standard_no-wildcard-imports" to "disabled",
            "indent_size" to "4",
            "insert_final_newline" to "true"
        ))
        licenseHeader("""/*
 * Copyright (c) AEON
 */
"""
        ).updateYearWithLatest(true)
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint("0.50.0")
    }
}

dependencies {
    // Java 8+ API desugaring support for older Android versions
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // DataStore
    implementation(libs.datastore.preferences)
    
    // Navigation
    implementation(libs.navigation.compose)
    
    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    
    // Compose BOM and Core UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    // Lifecycle Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    
    // Compose Animation & Foundation
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.extended)
    
    // Accompanist Libraries
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.placeholder)
    
    // Image Loading
    implementation(libs.coil.compose)
    
    // Lottie Animations
    implementation(libs.lottie.compose)
    
    // Shimmer Effect
    implementation(libs.shimmer.compose)
    
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Additional dependencies
    implementation("androidx.webkit:webkit:1.8.0")
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    // Hilt WorkManager integration
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    
    // Testing
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.mockk:mockk-android:1.13.8")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.48")
    
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}