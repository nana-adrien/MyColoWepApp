plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(projects.core.domain)
            implementation(projects.feature.securityCode.domain)
            implementation(projects.feature.securityCode.data)
            api(projects.feature.securityCode.presentation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
