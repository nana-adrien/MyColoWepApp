package plugin


import extensions.androidMainImplementation
import extensions.commonMainImplementation
import extensions.iosMainImplementation
import extensions.jsMainImplementation
import extensions.jvmMainImplementation
import extensions.libs
import extensions.pathToResourcePrefix
import extensions.wasmJsMainImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpNetWorkRequestLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("empire.digiprem.mycoloapp.kmp.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies{

                "commonMainImplementation"(libs.findBundle("ktor-common").get())
                "commonMainImplementation"(libs.findLibrary("kotlinx-serialization-json").get())
                "commonMainImplementation"(libs.findLibrary("supabase-realtime").get())
                "commonMainImplementation"(libs.findLibrary("supabase-postgrest").get())
                "commonMainImplementation"(libs.findLibrary("supabase-auth").get())
                "commonMainImplementation"(libs.findLibrary("supabase-storage").get())
                "commonMainImplementation"(libs.findLibrary("supabase-functions").get())

                androidMainImplementation(libs.findLibrary("ktor-client-okhttp").get())
                iosMainImplementation(libs.findLibrary("ktor-client-darwin").get())
                jsMainImplementation(libs.findLibrary("ktor-client-js").get())
                wasmJsMainImplementation(libs.findLibrary("ktor-client-js").get())
                jvmMainImplementation(libs.findLibrary("ktor-client-java").get())

            }
        }
    }
}