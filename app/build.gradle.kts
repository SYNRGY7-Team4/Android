import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.synrgy7team4.bankingapps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.synrgy7team4.bankingapps"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BUILD_TYPE", "\"release\"")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("String", "BUILD_FLAVOR", "\"debug\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
        useBuildCache = true
    }
}
buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.5.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

    }
}

dependencies {

    implementation(project(":feature_auth"))
    implementation(project(":feature_mutasi"))
    implementation(project(":feature_dashboard"))
    implementation(project(":common"))
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-crashlytics:19.0.3")

    val lifecycle_version = "2.7.0"
    val koin_version = "3.5.6"

    // ViewModel
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // LiveData
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycles only (without ViewModel or LiveData)
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    // Lifecycle utilities for Compose
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")

    // Saved state module for ViewModel
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")

    // Annotation processor
    //noinspection GradleDependency,LifecycleAnnotationProcessorWithJava8
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")

    implementation("androidx.core:core-ktx:1.13.1")
    //noinspection GradleDependency
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //biometric
    var biometricLibraryVersion = "1.1.0"
    implementation ("androidx.biometric:biometric:$biometricLibraryVersion")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Facebook Shimmer Loading
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Koin (Dependency Injection)
    // Declare koin-bom version
    implementation(platform("io.insert-koin:koin-bom:$koin_version"))

    // Declare the koin dependencies that you need
    implementation("io.insert-koin:koin-android")
    implementation("io.insert-koin:koin-core-coroutines")
    implementation("io.insert-koin:koin-androidx-workmanager")

    // Play Store Services Dependencies
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Work Manager
    //noinspection GradleDependency
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    // Chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    var camerax_version = "1.3.4"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")

    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")
}