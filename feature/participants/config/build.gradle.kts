plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(projects.core.domain)
            implementation(projects.feature.participants.domain)
            implementation(projects.feature.participants.data)
            api(projects.feature.participants.presentation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose )
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}



