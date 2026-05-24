package plugin

import extension.commonMainImplementation
import extension.getLibrary
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("octopusfx.client.mobile.boxoffice.kmp.library")
              apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }


            dependencies {
                commonMainImplementation( libs.getLibrary("jetbrains-compose-ui"))
                commonMainImplementation( libs.getLibrary("jetbrains-compose-foundation"))
                commonMainImplementation( libs.getLibrary("jetbrains-compose-material3"))
                commonMainImplementation( libs.getLibrary("jetbrains-compose-icons-core"))
                commonMainImplementation( libs.getLibrary("jetbrains-compose-icons-extended"))

                "debugImplementation"(libs.getLibrary("androidx-compose-ui-tooling"))
                //commonMainImplementation(libs.getLibrary("androidx-compose-ui-tooling-preview"))

            }
        }
    }
}