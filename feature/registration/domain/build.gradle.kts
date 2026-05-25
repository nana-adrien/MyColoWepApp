import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    /*js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }*/

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
        }
    }
}
