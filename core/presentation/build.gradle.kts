
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    js {
        browser {
            webpackTask {
                mode = org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode.PRODUCTION
                // sourceMap = false
                //  optimize(  true)
            }
        }
        binaries.executable()

    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }

                /* devServer = devServer?.apply {
                     hot = false          // Désactive HMR qui casse le WASM
                     liveReload = true
                     overlay = true       // Affiche les erreurs directement dans le navigateur
                 }*/
            }
        }
        binaries.executable()

    }

    sourceSets {
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(compose.html.core)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
        }

        commonMain.dependencies {
            //  implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.navigation.compose)
            implementation(libs.compose.components.resources)
            api(project(":core:resources"))
            implementation(libs.material3.adaptive)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network.ktor)
            implementation(libs.bundles.ktor.common)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
tasks.register<Copy>("wasmJsBrowserDistributionAndCopyToProduction") {

    // dependsOn("wasmJsBrowserDistribution")
    dependsOn("wasmJsBrowserDistribution")
    from("$buildDir/dist/wasmJs/productionExecutable/")
    into("$projectDir/production/portfolio/")
}

