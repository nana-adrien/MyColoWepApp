package configs

import com.android.build.api.dsl.CommonExtension
import extensions.libs
import org.gradle.api.Project
import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import kotlin.text.get
import kotlin.toString


fun Project.desktopConfig() {
    val jvmPackageName = libs.findVersion("projectApplicationId").get().toString()
    extensions.configure<ComposeExtension> {
        extensions.configure<org.jetbrains.compose.desktop.DesktopExtension> {
            application {
                mainClass = "$jvmPackageName.MainKt"
                nativeDistributions {
                    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                    packageName = jvmPackageName
                    packageVersion = libs.findVersion("projectVersionName").get().toString()
                }
            }

        }
    }
}