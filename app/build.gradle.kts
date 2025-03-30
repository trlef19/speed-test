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
        targetSdk = 35
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

    implementation ("androidx.core:core-ktx:1.15.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation ("androidx.activity:activity-compose:1.10.0")
    implementation ("androidx.compose.ui:ui:1.7.8")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation ("androidx.compose.material:material:1.7.8")
    implementation ("androidx.compose.material3:material3-android:1.3.1")
    implementation ("androidx.navigation:navigation-compose:2.8.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.7.8")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.7.8")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.7.8")
}