import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {

    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
  /*  js { browser() }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }*/
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:presentation"))
            implementation(project(":core:design_system"))
            implementation(project(":core:resources"))
            implementation(project(":feature:admin:security_code:domain"))
            implementation(project(":feature:admin:security_code:data"))
            /*implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.material3.adaptive)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)*/
            implementation(libs.supabase.auth)
        }
    }
}
