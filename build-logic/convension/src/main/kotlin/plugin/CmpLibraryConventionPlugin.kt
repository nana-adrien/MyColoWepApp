package plugin

import extensions.commonMainImplementation
import extensions.getLibrary
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("empire.digiprem.mycoloapp.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                commonMainImplementation(libs.getLibrary("compose-ui"))
                commonMainImplementation(libs.getLibrary("jetbrains-compose-foundation"))
                commonMainImplementation(libs.getLibrary("jetbrains-compose-material3"))
                commonMainImplementation(libs.getLibrary("compose-material-icons-core"))
                commonMainImplementation(libs.getLibrary("kotlinx-datetime"))
                commonMainImplementation(libs.getLibrary("compose-material-icons-extended"))

                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-compose").get())
                //"debugImplementation"(libs.getLibrary("compose-ui-tooling"))
                //commonMainImplementation(libs.getLibrary("androidx-compose-ui-tooling-preview"))

            }
        }
    }
}