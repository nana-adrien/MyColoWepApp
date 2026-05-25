package targets

import androidx.compose.ui.window.application
import com.android.build.api.dsl.CommonExtension
import com.android.tools.r8.internal.de
import extensions.libs
import org.gradle.api.Project
import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.JvmApplicationDistributions
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.core.Environment.Companion.application
import org.jetbrains.compose.reload.core.HotReloadEnvironment.mainClass
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.awt.SystemColor.desktop


fun Project.desktopTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm("desktop")
    }

}