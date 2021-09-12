plugins {
    id("java-platform")
    id("maven-publish")
}

dependencies {
    constraints {
        api(libs.kotlin.stdlib)
        api(libs.core.ktx)
        api(libs.appcompat)
        api(libs.material)
        api(libs.constraintlayout)
        api(libs.recyclerview)
        api(libs.handlebars)
        api(libs.work.runtime.ktx)
        api(libs.preference.ktx)

        api(libs.bundles.hilts)
        api(libs.bundles.room)
        api(libs.bundles.navigation)
        api(libs.bundles.retrofits)
        api(libs.bundles.lifecycles)
        api(libs.bundles.benchmarks)
        api(libs.bundles.tests)
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
