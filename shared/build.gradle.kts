import com.codingfeline.buildkonfig.compiler.FieldSpec

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}
val devPort = 8080
val prodPort = 8081
val isRelease = project.hasProperty("release")
kotlin {
    js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                mode = KotlinWebpackConfig.Mode.PRODUCTION
                // ✅ Source maps désactivées en prod (allège le bundle)
                sourceMaps = false

                devServer = KotlinWebpackConfig.DevServer(
                    port = if (isRelease) 8081 else 8080,

                )
            }
            // ✅ Optimisations Webpack production

            webpackTask {
                mainOutputFileName = "app.js"

                // ✅ Compression maximale
                args.add("--optimization-minimize")
                args.add("--optimization-concatenate-modules")
            }
            // ✅ Optimisations spécifiques distribution
            distribution {
                outputDirectory = project.file("production/my_colo_web_app/")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.navigation.compose)
            implementation(project(":core:navigation"))
            implementation(project(":core:presentation"))
            implementation(project(":core:design_system"))
          implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.ui)
            //  implementation(libs.supabase.bom)
            implementation(libs.kotlinx.datetime)
            implementation(libs.supabase.postgrest)
            implementation(libs.supabase.auth)
            implementation(libs.supabase.realtime)
            implementation(libs.supabase.storage)
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

