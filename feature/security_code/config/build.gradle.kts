plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(libs.koin.core)
            implementation(projects.feature.securityCode.domain)
            implementation(projects.feature.securityCode.data)
            implementation(projects.feature.securityCode.presentation)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
