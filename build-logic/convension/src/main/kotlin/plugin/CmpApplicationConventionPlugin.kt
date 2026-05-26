package plugin
import configs.desktopConfig
import configs.kotlinMultiplaformConfig
import extensions.commonMainImplementation
import extensions.getLibrary
import extensions.libs
import extensions.pathToFrameworkName
import extensions.relativePackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

class CmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("empire.digiprem.mycoloapp.android.compose.application")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
//jsTarget()
           // wasmJsTarget()
           // androidTarget()
            /*iosTarget(
                frameworkIosNativeName = this.pathToFrameworkName()
            )
            desktopTarget()*/
            kotlinMultiplaformConfig()
            desktopConfig()
            println("Configuring Kotlin iOS target ${project.relativePackageName().split(".").last().uppercaseFirstChar()}")
            dependencies {
                commonMainImplementation(libs.getLibrary("compose-ui"))
                commonMainImplementation(libs.getLibrary("jetbrains-compose-foundation"))
                commonMainImplementation(libs.getLibrary("jetbrains-compose-material3"))
                commonMainImplementation(libs.getLibrary("compose-material-icons-core"))

                commonMainImplementation(libs.findLibrary("jetbrains-compose-runtime").get())
                commonMainImplementation(libs.findLibrary("jetbrains-compose-viewmodel").get())
                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-compose").get())
                commonMainImplementation( libs.getLibrary("compose-material-icons-extended"))
                commonMainImplementation( libs.getLibrary("compose-material-icons-core"))
                commonMainImplementation(libs.findLibrary("navigation-compose").get())

                "debugImplementation"(libs.getLibrary("androidx-compose-ui-tooling"))
             //   commonMainImplementation(libs.getLibrary("androidx-compose-ui-tooling-preview"))
            }
        }
    }
}