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
        }
    }
}

val isRelease = project.hasProperty("release")
val localProperties = Properties().apply {
    rootProject.file("local.properties")
        .takeIf { it.exists() }
        ?.inputStream()
        ?.let { load(it) }
}

// ✅ Accès direct, pas lazy

/*buildkonfig {
    packageName = "empire.digiprem.mycoloapp.core.data"
    defaultConfigs {
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
}*/


