package configs

import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.BaseExtension
import com.android.builder.model.v2.ide.Library
import extensions.libs
import extensions.pathToFrameworkName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import targets.androidTarget
import targets.desktopTarget
import targets.iosTarget
import targets.jsTarget
import targets.wasmJsTarget

fun Project.kotlinMultiplaformConfig(isLibrary: Boolean = false) {
    if (isLibrary) {
        extensions.configure<LibraryExtension> {
            namespace = libs.findVersion("projectApplicationId").get().toString()
        }
    }

    jsTarget(!isLibrary)
    wasmJsTarget(!isLibrary)
    androidTarget()
    iosTarget(frameworkIosNativeName = this@kotlinMultiplaformConfig.pathToFrameworkName())

    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
            val iosMain = create("iosMain") {
                dependsOn(commonMain.get())
            }
            listOf(
                "iosX64Main",
                "iosArm64Main",
                "iosSimulatorArm64Main"
            ).forEach {
                getByName(it).dependsOn(iosMain)
            }
            // ✅ Web — regroupe wasmJs + js
            val webMain = create("webMain") {
               // kotlin.srcDirs("src/webMain/commonWebMain/kotlin")
                dependsOn(commonMain.get())
            }
            listOf(
                "wasmJsMain",
                "jsMain",
            ).forEach {
                getByName(it).dependsOn(webMain)
            }
            getByName("wasmJsMain") {
                dependsOn(webMain)
            }
            getByName("jsMain") {
                //dependsOn(webMain)
            }
        }

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
            freeCompilerArgs.add("-XXLanguage:+ExpectRefinement")
        }

    }

    desktopTarget()


}