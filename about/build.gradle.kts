plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(platform(project(":depconstraints")))
    androidTestApi(platform(project(":depconstraints")))

    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.CORE_KTX)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.MATERIAL)
    testImplementation(Libs.JUNIT)
    androidTestImplementation(Libs.JUNIT_EXT)
    androidTestImplementation(Libs.ESPRESSO)
}