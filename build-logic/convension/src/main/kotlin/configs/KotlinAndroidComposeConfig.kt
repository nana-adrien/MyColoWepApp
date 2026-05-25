package configs

import com.android.build.api.dsl.CommonExtension
import extensions.getLibrary
import extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import kotlin.text.get
import kotlin.toString

fun Project.androidComposeConfig(
    commonExtension: CommonExtension<*, *, *, *,*,*>,
) {
    with(commonExtension) {
        buildFeatures {
            compose = true
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true
        }
        dependencies {
            val bom = libs.getLibrary("androidx-compose-bom")
            "implementation"(platform(bom))
            "testImplementation"(platform(bom))
            "debugImplementation"(libs.getLibrary("compose-uiToolingPreview"))
            "debugImplementation"(libs.getLibrary("androidx-compose-ui-tooling"))
        }
    }

}