plugins {
    id("com.android.library")
    id("androidx.benchmark")
    id("kotlin-android")
}

android {
    compileSdk = Versions.COMPILE_SDK
    buildToolsVersion = "30.0.3"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
    }

    testBuildType = "release"
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isDefault = true
        }
    }
}

dependencies {
    androidTestImplementation(platform(projects.depconstraints))
    androidTestImplementation(platform(projects.lib))
    androidTestImplementation(Libs.TEST_RUNNER)
    androidTestImplementation(Libs.JUNIT_EXT)
    androidTestImplementation(Libs.JUNIT)
    androidTestImplementation(Libs.BENCHMARK)

    // Add your dependencies here. Note that you cannot benchmark code
    // in an app module this way - you will need to move any code you
    // want to benchmark to a library module:
    // https://developer.android.com/studio/projects/android-library#Convert
}
