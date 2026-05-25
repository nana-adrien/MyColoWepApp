import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}


group = "empire.digiprem.mycoloapp.buildlogic"


dependencies{
    implementation(libs.androidx.ui.desktop)
    compileOnly(libs.android.gradlePlugin)
    implementation(libs.compose.runtime)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.gradle.compiler.plugin)
    compileOnly(libs.ksp.gradlePlugin)
    implementation(libs.buildkoinfig.gradlePlugin)
    implementation(libs.buildkoinfig.compiler)
}

java{
    sourceCompatibility=JavaVersion.VERSION_21
    targetCompatibility=JavaVersion.VERSION_21
}

kotlin{
    compilerOptions{
        jvmTarget= JvmTarget.JVM_21
    }
}


tasks{
    validatePlugins{
        enableStricterValidation=true
        failOnWarning=true
    }
}

gradlePlugin{
    plugins {
        register("androidApplication"){
            id = "empire.digiprem.mycoloapp.android.application"
            implementationClass = "plugin.AndroidApplicationConventionPlugin"
        }
        register("androidComposeApplication"){
            id = "empire.digiprem.mycoloapp.android.compose.application"
            implementationClass = "plugin.AndroidApplicationComposeConventionPlugin"
        }
        register("kmpLibrary"){
            id = "empire.digiprem.mycoloapp.kmp.library"
            implementationClass = "plugin.KmpLibraryConventionPlugin"
        }
        register("cmpLibrary"){
            id = "empire.digiprem.mycoloapp.cmp.library"
            implementationClass = "plugin.CmpLibraryConventionPlugin"
        }
        register("cmpApplication"){
            id = "empire.digiprem.mycoloapp.cmp.application"
            implementationClass = "plugin.CmpApplicationConventionPlugin"
        }
        register("kmpFeature"){
            id = "empire.digiprem.mycoloapp.kmp.feature"
            implementationClass = "plugin.KmpFeatureConventionPlugin"
        }
        register("CmpFeature"){
            id = "empire.digiprem.mycoloapp.cmp.feature"
            implementationClass = "plugin.CmpFeatureConventionPlugin"
        }
        register("KmpNetWorkRequest"){
            id = "empire.digiprem.mycoloapp.kmp.netWork.request"
            implementationClass = "plugin.KmpNetWorkRequestLibraryConventionPlugin"
        }
        register("buildKonfig"){
            id="empire.digiprem.mycoloapp.buildKonfig"
            implementationClass="plugin.BuildKonfigConventionPlugin"
        }

    }
}