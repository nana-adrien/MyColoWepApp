package plugin


import extensions.androidMainImplementation
import extensions.commonMainImplementation
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("empire.digiprem.mycoloapp.kmp.library")
            }
            dependencies {
                commonMainImplementation(project(":core:domain"))
                //commonMainImplementation(libs.findLibrary("koin-compose").get())
            }
        }
    }


}
