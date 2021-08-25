plugins {
    id("java-platform")
    id("maven-publish")
}

val coreKtx = "1.6.0"
val appcompat = "1.3.1"
val material = "1.4.0"
val junit = "4.13.2"
val junitExt = "1.1.3"
val espresso = "3.4.0"
val constraintLayout = "2.1.0"
val recyclerView = "1.2.1"
val hilt = "2.38.1"
val room = "2.3.0"
val lifecycle = "2.4.0-alpha03"
val handleBars = "4.2.0"
val retrofit = "2.9.0"
val work = "2.6.0-rc01"
val workHilt = "1.0.0"
val coreTesting = "2.1.0"
val mockk = "1.12.0"
val coroutineTest = "1.5.1"
val testRunner = "1.4.0"
val benchmark = "1.0.0"
val benchmarkMacro = "1.1.0-alpha05"
val preference = "1.1.1"

dependencies {
    constraints {
        api("${Libs.KOTLIN_STDLIB}:${Versions.KOTLIN}")
        api("${Libs.CORE_KTX}:$coreKtx")
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.JUNIT_EXT}:$junitExt")
        api("${Libs.ESPRESSO}:$espresso")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraintLayout")
        api("${Libs.RECYCLER_VIEW}:$recyclerView")
        api("${Libs.HILT_ANDROID}:$hilt")
        api("${Libs.HILT_COMPILER}:$hilt")
        api("${Libs.HILT_ANDROID_COMPILER}:$hilt")
        api("${Libs.HILT_ANDROID_TESTING}:$hilt")

        api("${Libs.NAVIGATION_FRAGMENT_KTX}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_UI_KTX}:${Versions.NAVIGATION}")

        api("${Libs.ROOM_KTX}:$room")
        api("${Libs.ROOM_RUNTIME}:$room")
        api("${Libs.ROOM_COMPILER}:$room")

        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_LIVE_DATA_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_RUNTIME_KTX}:$lifecycle")

        api("${Libs.HANDLE_BARS}:$handleBars")

        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.CONVERTER_GSON}:$retrofit")
        api("${Libs.CONVERTER_GSON}:$retrofit")

        api("${Libs.WORK_RUNTIME}:$work")
        api("${Libs.HILT_WORK}:$workHilt")
        api("${Libs.ANDROIDX_HILT_COMPILER}:$workHilt")
        api("${Libs.CORE_TESTING}:$coreTesting")
        api("${Libs.MOCKK}:$mockk")
        api("${Libs.COROUTINE_TEST}:$coroutineTest")
        api("${Libs.TEST_RUNNER}:$testRunner")
        api("${Libs.BENCHMARK}:$benchmark")
        api("${Libs.BENCHMARK_MACRO}:$benchmarkMacro")
        api("${Libs.BENCHMARK_MACRO_JUNIT}:$benchmarkMacro")
        api("${Libs.PREFERENCE}:$preference")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
