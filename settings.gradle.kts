rootProject.name = "MyColoWepApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// Core modules
include(":core:domain")
include(":core:data")
include(":core:navigation")
include(":core:resources")
include(":core:presentation")
include(":core:design_system")

// Feature modules
include(":feature:landing")
include(":feature:registration:domain")
include(":feature:registration:presentation")
include(":feature:confirmation")
include(":feature:admin:login:domain")
include(":feature:admin:login:data")
include(":feature:admin:login:presentation")
include(":feature:admin:dashboard:domain")
include(":feature:admin:dashboard:data")
include(":feature:admin:dashboard:presentation")
include(":feature:registration:data")
include(":feature:admin:security_code:domain")
include(":feature:admin:security_code:data")
include(":feature:admin:security_code:presentation")

// App modules
include(":shared")
include(":webApp")