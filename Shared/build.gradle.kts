
plugins {
    alias(libs.plugins.convention.cmp.application)
    //alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.core.splashscreen)
            implementation(libs.koin.android)
        }
        wasmJsMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-browser:0.3")
        }

        webMain.dependencies {
            implementation(libs.wrappers.browser)
        }
        commonMain.dependencies {
            implementation(libs.supabase.postgrest)
            implementation(libs.supabase.auth)
            implementation(libs.supabase.realtime)
            implementation(libs.supabase.storage)

            implementation(project(":core:navigation"))
            implementation(project(":core:presentation"))
            implementation(project(":core:design_system"))
            implementation(project(":core:config"))
            implementation(project(":feature:confirmation"))
            implementation(project(":feature:auth:config"))
            implementation(project(":feature:registration:config"))
            implementation(project(":feature:security_code:config"))
            implementation(project(":feature:participants:config"))
            implementation(project(":feature:feed:config"))
            implementation(project(":feature:live:config"))
            implementation(project(":feature:profile:config"))
            implementation(project(":feature:pre_auth:config"))
            implementation(project(":feature:settings:config"))
            implementation(projects.core.resources)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}


tasks.register("wasmJsBrowserDistributionAndCopyToProduction") {
    dependsOn("wasmJsBrowserDistribution")
    //val buildDir = layout.buildDirectory.get().asFile
   // val projectDir = layout.projectDirectory.asFile
    val projectVersion = project.version.toString()

    //val sourceDir = File("${buildDir}/dist/wasmJs/productionExecutable/")
   // val targetDir = File("${projectDir}/production/my_colo_web_app/")

   // inputs.dir(sourceDir)
   // outputs.dir(targetDir)

    doLast {
        // 1. Copie manuelle sans référence au contexte Gradle
    //    targetDir.mkdirs()
       /* sourceDir.walkTopDown().forEach { source ->
            val target = File(targetDir, source.relativeTo(sourceDir).path)
            if (source.isDirectory) {
                target.mkdirs()
            } else {
                source.copyTo(target, overwrite = true)
            }
        }*/
      //  println("✅ Fichiers copiés vers : ${targetDir.absolutePath}")

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
