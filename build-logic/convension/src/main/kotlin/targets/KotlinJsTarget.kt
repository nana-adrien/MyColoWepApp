package targets

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

@OptIn(ExperimentalDistributionDsl::class)
internal fun Project.wasmTarget(basename: String) {
    val isRelease = project.hasProperty("release")
    extensions.configure<KotlinWasmJsTargetDsl> {
        browser {
            commonWebpackConfig {
                mode = KotlinWebpackConfig.Mode.PRODUCTION
                // ✅ Source maps désactivées en prod (allège le bundle)
                sourceMaps = false

                devServer = KotlinWebpackConfig.DevServer(
                    port = if (isRelease) 8081 else 8080,

                    )
            }
            // ✅ Optimisations Webpack production
            webpackTask {
                mainOutputFileName.set("app.js")

                // ✅ Compression maximale
                args.add("--optimization-minimize")
                args.add("--optimization-concatenate-modules")
            }

            // ✅ Optimisations spécifiques distribution
            distribution {
                outputDirectory.set(project.file("production/my_colo_web_app/"))
            }
        }
    }
}