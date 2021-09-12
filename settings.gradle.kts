enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Todo"
include(
    ":app",
    ":macrobenchmark",
    ":benchmark",
    ":lib",
    ":about",
    ":settings",
    "depconstraints",
)
