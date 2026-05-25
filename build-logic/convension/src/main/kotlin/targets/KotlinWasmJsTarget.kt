package targets

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

@OptIn(ExperimentalDistributionDsl::class, ExperimentalWasmDsl::class)
internal fun Project.wasmJsTarget(isApplication:Boolean = false) {
    val isRelease = project.hasProperty("release")
    extensions.configure<KotlinMultiplatformExtension> {
        wasmJs {
            browser {
                commonWebpackConfig {
                    mode = if (isRelease)
                        KotlinWebpackConfig.Mode.PRODUCTION
                    else
                        KotlinWebpackConfig.Mode.DEVELOPMENT

                    /*sourceMaps = !isRelease
                    devServer = KotlinWebpackConfig.DevServer(
                        port = if (isRelease) 8081 else 8080,
                        static = mutableListOf(
                            "ComposeApp/build/kotlin-webpack/wasmJs/developmentExecutable"
                        )
                    )*/
                }

                webpackTask {
                  //  mainOutputFileName.set("app.js")

                    if (isRelease) {
                        args.add("--optimization-minimize")
                        args.add("--optimization-concatenate-modules")
                    }
                }
                // ✅ outputDirectory uniquement en prod
                if (isRelease) {
                    distribution {
                        outputDirectory.set(project.file("production/my_colo_web_app/"))
                    }
                }
                // En dev → build/dist/wasmJs/developmentExecutable/ par défaut
            }

            if (isApplication) {
                binaries.executable()
            }
        }
    }
}