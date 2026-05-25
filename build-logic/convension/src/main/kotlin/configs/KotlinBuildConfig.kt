package configs

import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import extensions.libs
import extensions.pathToPackageName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.internal.builtins.StandardNames.FqNames.target
import java.util.Properties

fun Project.buildConfig() {
    val isRelease = project.hasProperty("release")
    val localProperties = Properties().apply {
        rootProject.file("local.properties")
            .takeIf { it.exists() }
            ?.inputStream()
            ?.let { load(it) }
    }

    // ✅ Accès direct, pas lazy
    val buildKonfig = extensions.getByType<BuildKonfigExtension>()
    buildKonfig.packageName = pathToPackageName()
    buildKonfig.defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING, "SUPABASE_URL",
            if (isRelease)
                localProperties.getProperty("SUPABASE_URL_PROD", "")
            else
                localProperties.getProperty("SUPABASE_URL_DEV", "")
        )
        buildConfigField(
            FieldSpec.Type.STRING, "SUPABASE_KEY",
            if (isRelease)
                localProperties.getProperty("SUPABASE_KEY_PROD", "")
            else
                localProperties.getProperty("SUPABASE_KEY_DEV", "")
        )
        buildConfigField(
            FieldSpec.Type.STRING, "ENV",
            if (isRelease) "prod" else "dev"
        )
    }
}