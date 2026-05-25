plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(projects.core.domain)
            implementation(projects.feature.settings.domain)
            implementation(projects.feature.settings.data)
            api(projects.feature.settings.presentation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
