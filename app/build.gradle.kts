plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.example.todo"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.versionCodeMobile
        versionName = Versions.versionName

        testInstrumentationRunner = "com.example.todo.di.CustomTestRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
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
        val options = this
        options.jvmTarget = "1.8"
    }
    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
    }
}

dependencies {
    api(platform(projects.depconstraints))
    kapt(platform(projects.depconstraints))
    androidTestApi(platform(projects.depconstraints))
    implementation(projects.about)
    implementation(projects.settings)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.RECYCLER_VIEW)
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)

    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.NAVIGATION_UI_KTX)

    implementation(Libs.ROOM_RUNTIME)
    implementation(Libs.ROOM_KTX)
    kapt(Libs.ROOM_COMPILER)

    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    implementation(Libs.LIFECYCLE_RUNTIME_KTX)

    implementation(Libs.HANDLE_BARS)

    implementation(Libs.RETROFIT)
    implementation(Libs.CONVERTER_GSON)

    implementation(Libs.WORK_RUNTIME)
    implementation(Libs.HILT_WORK)
    kapt(Libs.ANDROIDX_HILT_COMPILER)

    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.CORE_KTX)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.MATERIAL)

    // Testing
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.CORE_TESTING)
    testImplementation(Libs.MOCKK)
    testImplementation(Libs.COROUTINE_TEST)
    androidTestImplementation(Libs.TEST_RUNNER)
    androidTestImplementation(Libs.ESPRESSO)
    androidTestImplementation(Libs.JUNIT_EXT)
    androidTestImplementation(Libs.CORE_TESTING)
    androidTestImplementation(Libs.COROUTINE_TEST)
    kaptTest(Libs.HILT_ANDROID_COMPILER)
    androidTestImplementation(Libs.HILT_ANDROID_TESTING)
    kaptAndroidTest(Libs.HILT_ANDROID_COMPILER)
}
tasks.withType<Test> {
    extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
        generateXml = true
        generateHtml = true
        coverageEngine = kotlinx.kover.api.CoverageEngine.INTELLIJ
        includes = listOf("com\\.example\\..*")
        excludes = listOf(
            "dagger\\.hilt\\..*",
            "com\\.example\\.todo\\.databinding\\..*",
            "com\\.example\\.todo\\.di\\..*",
            "com\\.example\\.settings\\..*",
        )
    }
}
