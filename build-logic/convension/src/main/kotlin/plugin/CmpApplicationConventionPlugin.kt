package plugin
import extension.commonMainImplementation
import extension.getLibrary
import extension.libs
import extension.relativePackageName
import octopusfx.client.mobile.convention.configureAndroidTarget
import octopusfx.client.mobile.convention.configureIosTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

class CmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("octopusfx.client.mobile.boxoffice.android.application.compose")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
            configureAndroidTarget()
            configureIosTarget(
              "ComposeApp"  //project.relativePackageName().split(".").last().uppercaseFirstChar()
            )
            println("Configuring Kotlin iOS target ${project.relativePackageName().split(".").last().uppercaseFirstChar()}")
            dependencies {
                commonMainImplementation( libs.getLibrary("jetbrains-compose-icons-core"))
                commonMainImplementation( libs.getLibrary("jetbrains-compose-icons-extended"))

                "debugImplementation"(libs.getLibrary("androidx-compose-ui-tooling"))
             //   commonMainImplementation(libs.getLibrary("androidx-compose-ui-tooling-preview"))
            }
        }
    }
}