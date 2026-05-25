plugins {
    alias(libs.plugins.convention.kmp.network.request)
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(projects.core.domain)
            implementation(projects.feature.preAuth.domain)
            implementation(projects.feature.preAuth.data)
            api(projects.feature.preAuth.presentation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
