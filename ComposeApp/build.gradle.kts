import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
        }

        commonMain.dependencies {
            implementation(projects.shared)

            implementation(libs.compose.ui)
        }
    }
}


tasks.register("wasmJsBrowserDistributionAndCopyToProduction") {
    dependsOn("wasmJsBrowserDistribution")
    val buildDir = layout.buildDirectory.get().asFile
    val projectDir = layout.projectDirectory.asFile
    val projectVersion = project.version.toString()

    val sourceDir = File("${buildDir}/dist/wasmJs/productionExecutable/")
    val targetDir = File("${projectDir}/production/my_colo_web_app/")

    inputs.dir(sourceDir)
    outputs.dir(targetDir)

    doLast {
        // 1. Copie manuelle sans référence au contexte Gradle
        targetDir.mkdirs()
        sourceDir.walkTopDown().forEach { source ->
            val target = File(targetDir, source.relativeTo(sourceDir).path)
            if (source.isDirectory) {
                target.mkdirs()
            } else {
                source.copyTo(target, overwrite = true)
            }
        }
        println("✅ Fichiers copiés vers : ${targetDir.absolutePath}")

        // 2. Génère build-info.json
        val buildInfoFile = File("${projectDir}/production/my_colo_web_app/build-info.json")
        val timestamp = System.currentTimeMillis().toString()
        val buildNumber = System.currentTimeMillis()
        buildInfoFile.writeText(
            """
            {
                "buildTime": "$timestamp",
                "buildNumber": $buildNumber,
                "version": "$projectVersion"
            }
            """.trimIndent()
        )

        println("✅ Build info généré : $timestamp")

        // 3. Deploy sur Vercel
        println("🚀 Déploiement sur Vercel...")
        val vercelDir = File("${projectDir}/production/my_colo_web_app/")
        val result = ProcessBuilder("vercel", "--prod", "--yes")
            .directory(vercelDir)
            .redirectErrorStream(true)
            .start()

        result.inputStream.bufferedReader().forEachLine { line ->
            println(line)
        }

        val exitCode = result.waitFor()
        if (exitCode != 0) {
            throw GradleException("❌ Déploiement Vercel échoué (exit code: $exitCode)")
        }

        println("✅ Déploiement Vercel réussi !")
    }
}
/*
tasks.register<Copy>("wasmJsBrowserDistributionAndCopyToProduction") {

    // dependsOn("wasmJsBrowserDistribution")
    dependsOn("wasmJsBrowserDistribution")
    from("$buildDir/dist/wasmJs/productionExecutable/")
    into("$projectDir/production/my_colo_web_app/")
}
*/
