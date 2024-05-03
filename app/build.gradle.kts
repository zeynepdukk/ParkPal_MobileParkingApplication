plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.parkpal1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.parkpal1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions {
        resources {
            excludes += setOf(
                // To compile the current version of UX Framework you need to add only these two lines:
                "META-INF/DEPENDENCIES",
                "META-INF/INDEX.LIST",
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.mapbox.maps:android:10.16.3")
    implementation("com.mapbox.extension:maps-compose:11.2.1")
    //implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:9.7.1")
   // implementation("com.mapbox.navigation:android:2.18.0")
    //implementation("com.mapbox.mapboxsdk:mapbox-android-plugin-annotation-v9:0.9.0")
   // implementation("com.mapbox.navigation:ui-dropin:2.19.0")
    implementation("com.mapbox.navigation:android:2.17.1")
    implementation("com.airbnb.android:lottie:6.0.0")

}