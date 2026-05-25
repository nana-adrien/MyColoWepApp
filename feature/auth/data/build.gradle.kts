import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {
  /*  js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
*/
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            implementation(project(":core:data"))
            implementation(project(":feature:auth:domain"))
           /* implementation(libs.supabase.auth)
            implementation(libs.koin.core)
            implementation(libs.coroutines.core)*/
        }
      /*  jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }*/
    }
}
