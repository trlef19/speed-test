plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    defaultConfig {
        applicationId = "com.letr.speedtest"
        namespace = "com.letr.speedtest"
        compileSdk = 35
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.16.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation ("androidx.activity:activity-compose:1.10.1")
    implementation ("androidx.compose.ui:ui:1.8.3")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.8.3")
    implementation ("androidx.compose.material:material:1.8.3")
    implementation ("androidx.compose.material3:material3-android:1.3.2")
    implementation ("androidx.navigation:navigation-compose:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation ("androidx.activity:activity-ktx:1.10.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.8.3")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.8.3")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.8.3")
}