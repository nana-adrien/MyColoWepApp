plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(libs.koin.core)
            implementation(projects.feature.admin.login.domain)
            implementation(projects.feature.admin.login.data)
            implementation(projects.feature.admin.login.presentation)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
