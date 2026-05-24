plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(libs.koin.core)
            implementation(projects.feature.registration.domain)
            implementation(projects.feature.registration.data)
            implementation(projects.feature.registration.presentation)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
