import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.feed.data)
            implementation(projects.core.domain)
            implementation(projects.feature.feed.domain)
            api(projects.feature.feed.presentation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose )
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
