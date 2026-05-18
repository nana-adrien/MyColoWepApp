import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.navigation.compose)
            implementation(project(":core:navigation"))
            implementation(project(":core:presentation"))
            implementation(project(":core:design_system"))
            implementation(project(":feature:landing"))
            implementation(project(":feature:registration:presentation"))
            implementation(project(":feature:confirmation"))
            implementation(project(":feature:admin:login:presentation"))
            implementation(project(":feature:admin:dashboard:presentation"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
