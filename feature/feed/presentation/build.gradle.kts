import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designSystem)
            implementation(projects.core.domain)
            implementation(projects.feature.feed.domain)
            implementation(libs.kotlinx.datetime)
        }
    }
}
