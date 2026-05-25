package plugin

import com.android.build.api.dsl.LibraryExtension
import configs.androidComposeConfig
import configs.androidConfig
import configs.kotlinMultiplaformConfig
import extensions.commonMainImplementation
import extensions.libs
import extensions.pathToResourcePrefix
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KmpLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }

            kotlinMultiplaformConfig(true)
            extensions.configure<LibraryExtension>{
                androidConfig(this)
                resourcePrefix=this@with.pathToResourcePrefix()

                // Required to make build of app run in ios simulator
                experimentalProperties["android.experimental.kmp.enableAndroidResources"]="true"

            }
            dependencies{
                commonMainImplementation(libs.findLibrary("kotlinx-datetime").get())
                commonMainImplementation(libs.findLibrary("kotlinx-serialization-json").get())
                commonMainImplementation(libs.findLibrary("coroutines-core").get())
                "commonTestImplementation"(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}