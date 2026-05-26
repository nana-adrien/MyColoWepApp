import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.convention.kmp.network.request)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.feature.live.domain)
            implementation(libs.supabase.functions)
            implementation(libs.supabase.postgrest)
            implementation(libs.supabase.realtime)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            //implementation(libs.livekit.android)
        }
    }
}
