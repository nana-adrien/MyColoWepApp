rootProject.name = "MyColoWepApp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
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
        google()
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
include(":core:config")
include(":core:navigation")
include(":core:resources")
include(":core:presentation")
include(":core:design_system")

// Feature modules — pre_auth
include(":feature:pre_auth:landing")
include(":feature:confirmation")

// Feature modules — auth
include(":feature:auth:domain")
include(":feature:auth:data")
include(":feature:auth:presentation")
include(":feature:auth:config")

// Feature modules — admin:login
include(":feature:admin:login:domain")
include(":feature:admin:login:data")
include(":feature:admin:login:presentation")
include(":feature:admin:login:config")

// Feature modules — admin:dashboard
include(":feature:admin:dashboard:domain")
include(":feature:admin:dashboard:data")
include(":feature:admin:dashboard:presentation")
include(":feature:admin:dashboard:config")

// Feature modules — registration
include(":feature:registration:domain")
include(":feature:registration:data")
include(":feature:registration:presentation")
include(":feature:registration:config")

// Feature modules — security_code
include(":feature:security_code:domain")
include(":feature:security_code:data")
include(":feature:security_code:presentation")
include(":feature:security_code:config")

// Feature modules — participants
include(":feature:participants:domain")
include(":feature:participants:data")
include(":feature:participants:presentation")
include(":feature:participants:config")

// Feature modules — profile
include(":feature:profile:domain")
include(":feature:profile:data")
include(":feature:profile:presentation")
include(":feature:profile:config")

// Feature modules — feed
include(":feature:feed:domain")
include(":feature:feed:data")
include(":feature:feed:presentation")
include(":feature:feed:config")

// Feature modules — live
include(":feature:live:domain")
include(":feature:live:data")
include(":feature:live:presentation")
include(":feature:live:config")

// Feature modules — settings
include(":feature:settings:domain")
include(":feature:settings:data")
include(":feature:settings:presentation")
include(":feature:settings:config")

// App modules
include(":shared")
include(":ComposeApp")