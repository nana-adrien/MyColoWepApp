plugins {

    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.feature.preAuth.domain)
        }
    }
}
