import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {

    sourceSets {

        commonMain.dependencies {
            implementation(libs.compose.resources)
        }

    }
}
