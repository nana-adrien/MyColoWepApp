import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
   /* js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }*/

    sourceSets {
        androidMain.dependencies {

            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            //api(project(":core:domain"))
            api(projects.core.resources)
            api(projects.core.domain)
          //  implementation(libs.compose.runtime)
            //implementation(libs.compose.foundation)
            //implementation(libs.compose.material3)
            //implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.material3.adaptive)
            //implementation(libs.androidx.lifecycle.viewmodelCompose)
           // implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.svg)
          //  implementation(libs.compose.uiToolingPreview)
            //implementation(libs.kotlinx.datetime)
           // implementation(libs.compose.material.icons.core)
           // implementation(libs.compose.material.icons.extended)
        }
        webMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}
