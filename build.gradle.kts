// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-rc01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}")
        classpath("androidx.benchmark:benchmark-gradle-plugin:${Versions.BENCHMARK}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.27.1"
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "com.diffplug.gradle.spotless")
    val ktlintVer = "0.40.0"
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(ktlintVer)
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint(ktlintVer)
        }
    }

    tasks.whenTaskAdded {
        if (name == "preBuild") {
            mustRunAfter("spotlessCheck")
        }
    }
}