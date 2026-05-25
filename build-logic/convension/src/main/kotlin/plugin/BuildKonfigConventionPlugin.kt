package plugin

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.tools.r8.internal.va
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import configs.buildConfig
import extensions.libs
import extensions.pathToPackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties

import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class BuildKonfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")


            }
            buildConfig()
        }

    }
}