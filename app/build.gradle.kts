import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.andresual.assesment_mandiri"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.andresual.assesment_mandiri"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(FileInputStream(localPropertiesFile))
        }
        
        val tmdbApiKey = localProperties.getProperty("TMDB_API_KEY", "")
        val tmdbBaseUrl = localProperties.getProperty("TMDB_BASE_URL", "")
        val tmdbImageBaseUrl = localProperties.getProperty("TMDB_IMAGE_BASE_URL", "")
        val tmdbImageBaseUrlW500 = localProperties.getProperty("TMDB_IMAGE_BASE_URL_W500", "")
        val tmdbImageBaseUrlW1280 = localProperties.getProperty("TMDB_IMAGE_BASE_URL_W1280", "")
        val picsumBaseUrl = localProperties.getProperty("PICSUM_BASE_URL", "")
        val featuredGenreImageUrl = localProperties.getProperty("FEATURED_GENRE_IMAGE_URL", "")
        val youtubeEmbedUrl = localProperties.getProperty("YOUTUBE_EMBED_URL", "")
        val youtubeOriginUrl = localProperties.getProperty("YOUTUBE_ORIGIN_URL", "")

        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"$tmdbBaseUrl\"")
        buildConfigField("String", "TMDB_IMAGE_BASE_URL", "\"$tmdbImageBaseUrl\"")
        buildConfigField("String", "TMDB_IMAGE_BASE_URL_W500", "\"$tmdbImageBaseUrlW500\"")
        buildConfigField("String", "TMDB_IMAGE_BASE_URL_W1280", "\"$tmdbImageBaseUrlW1280\"")
        buildConfigField("String", "PICSUM_BASE_URL", "\"$picsumBaseUrl\"")
        buildConfigField("String", "FEATURED_GENRE_IMAGE_URL", "\"$featuredGenreImageUrl\"")
        buildConfigField("String", "YOUTUBE_EMBED_URL", "\"$youtubeEmbedUrl\"")
        buildConfigField("String", "YOUTUBE_ORIGIN_URL", "\"$youtubeOriginUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit & Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // Paging 3
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Coil
    implementation(libs.coil.compose)

    // Navigation Compose
    implementation(libs.navigation.compose)


    // Google Fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.4")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}