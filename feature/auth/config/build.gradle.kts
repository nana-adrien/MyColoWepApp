plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.data)
            api(projects.feature.auth.presentation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose )
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}



