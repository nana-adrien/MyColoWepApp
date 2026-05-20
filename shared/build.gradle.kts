import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
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
            implementation(project(":feature:registration:data"))
            implementation(project(":feature:registration:domain"))
            implementation(project(":feature:confirmation"))
            implementation(project(":feature:admin:login:presentation"))
            implementation(project(":feature:admin:login:data"))
            implementation(project(":feature:admin:login:domain"))
            implementation(project(":feature:admin:dashboard:presentation"))
            implementation(project(":feature:admin:dashboard:domain"))
            implementation(project(":feature:admin:dashboard:data"))
            implementation(project(":feature:admin:security_code:presentation"))
            implementation(project(":feature:admin:security_code:data"))
            implementation(project(":feature:admin:security_code:domain"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.ui)
          //  implementation(libs.supabase.bom)
            implementation(libs.kotlinx.datetime)
            implementation(libs.supabase.postgrest)
            implementation(libs.supabase.auth)
        }
        jsMain.dependencies {
            //implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.js)
            implementation(libs.wrappers.browser)
        }
        val wasmJsMain by getting {
            dependencies {
                runtimeOnly("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0-wasm0")
                implementation(libs.ktor.client.js)
                implementation(libs.wrappers.browser)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
