package configs

import com.android.build.api.dsl.CommonExtension
import extensions.getLibrary
import extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import kotlin.text.get
import kotlin.toString

fun Project.androidConfig(
    commonExtension: CommonExtension<*, *, *, *,*,*>,
) {
    with(commonExtension) {
        compileSdk=  libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
        defaultConfig.minSdk=libs.findVersion("projectMinSdkVersion").get().toString().toInt()
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
            isCoreLibraryDesugaringEnabled = true

        }
        compilerConfig()
        dependencies {
            "coreLibraryDesugaring"(libs.getLibrary("android-desugarJdkLibs"))
        }
    }

}