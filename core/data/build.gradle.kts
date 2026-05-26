import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import extensions.pathToPackageName
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.convention.kmp.network.request)
    alias(libs.plugins.convention.buildkonfig)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(libs.bundles.dataStore.common)

        }
    }
}
