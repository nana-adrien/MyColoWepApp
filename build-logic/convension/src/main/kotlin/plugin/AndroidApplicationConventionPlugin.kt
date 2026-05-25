package plugin

import com.android.build.api.dsl.ApplicationExtension
import configs.androidConfig
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import targets.androidTarget
import kotlin.collections.plusAssign
import kotlin.text.toInt

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")

            }
            extensions.configure<ApplicationExtension> {

                namespace =  libs.findVersion("projectApplicationId").get().toString()

                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get()
                        .toString()
                    targetSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }
                androidConfig(this)
            }
        }
    }
}