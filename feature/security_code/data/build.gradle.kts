import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:data"))
            implementation(project(":feature:security_code:domain"))
         /*   implementation(libs.supabase.postgrest)
            implementation(libs.supabase.auth)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.coroutines.core)*/
        }
     /*   jsMain.dependencies { implementation(libs.ktor.client.js) }
        val wasmJsMain by getting { dependencies { implementation(libs.ktor.client.js) } }
  */  }
}
