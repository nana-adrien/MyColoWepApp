import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(projects.core.domain)
            implementation(projects.feature.live.data)
            implementation(projects.feature.live.domain)
            api(projects.feature.live.presentation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
