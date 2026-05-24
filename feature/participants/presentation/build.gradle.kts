import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
//   /* js { browser() }
//
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs { browser() }*/

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            api(project(":core:presentation"))
            implementation(project(":core:design_system"))
            implementation(project(":feature:admin:dashboard:domain"))
            implementation(project(":feature:admin:dashboard:data"))
            implementation(project(":feature:registration:domain"))
            implementation(projects.feature.registration.presentation)
            implementation(projects.feature.admin.securityCode.domain)
            implementation(projects.feature.admin.securityCode.data)
            implementation(projects.feature.admin.securityCode.presentation)
            //  implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
          /*  implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.navigation.compose)
            implementation(libs.compose.components.resources)
            implementation(project(":core:resources"))
            implementation(libs.material3.adaptive)
           // implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.json)*/
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network.ktor)
            implementation(libs.bundles.ktor.common)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.supabase.auth)
            implementation(libs.supabase.realtime)

        }
    }
}
