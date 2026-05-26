package extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope


// ksp

fun DependencyHandlerScope.ksp(dependency: Provider<MinimalExternalModuleDependency>) {
    kspAndroid(dependency)
    kspIosSimulatorArm64(dependency)
    kspIosArm64(dependency)
  //  kspIosX64(dependency)
}

fun DependencyHandlerScope.kspAndroid(dependency: Provider<MinimalExternalModuleDependency>) {
    "kspAndroid"(dependency)
}

fun DependencyHandlerScope.kspIosSimulatorArm64(dependency: Provider<MinimalExternalModuleDependency>) {
    "kspIosSimulatorArm64"(dependency)
}
fun DependencyHandlerScope.kspIosArm64(dependency: Provider<MinimalExternalModuleDependency>) {
    "kspIosArm64"(dependency)
}
fun DependencyHandlerScope.kspIosX64(dependency: Provider<MinimalExternalModuleDependency>) {
    "kspIosX64"(dependency)
}


// common main implementation

 fun DependencyHandlerScope.commonMainImplementation(project: Project) {
    "commonMainImplementation"(project)
}
 fun DependencyHandlerScope.commonMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) {
    "commonMainImplementation"(dependency)
}
fun DependencyHandlerScope.commonMainApi(dependency: Provider<MinimalExternalModuleDependency>) {
    "commonMainApi"(dependency)
}
fun DependencyHandlerScope.commonMainImplementation(library: String) {
    "commonMainImplementation"(library)
}

// android main implementation

 fun DependencyHandlerScope.androidMainImplementation(project: Project) {
    "androidMainImplementation"(project)
}
fun DependencyHandlerScope.androidMainImplementation(library: String) {
    "androidMainImplementation"(library)
}
fun DependencyHandlerScope.androidMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) {
    "androidMainImplementation"(dependency)
}

// Fonction générique interne
private fun DependencyHandlerScope.sourceSetImplementation(
    name: String,
    dependency: Any
) {
    "${name}MainImplementation"(dependency)
}

// iOS → iosX64 + iosArm64 + iosSimulatorArm64
fun DependencyHandlerScope.iosMainImplementation(project: Project) {
    sourceSetImplementation("iosX64", project)
    sourceSetImplementation("iosArm64", project)
    sourceSetImplementation("iosSimulatorArm64", project)
}
fun DependencyHandlerScope.iosMainImplementation(library: String){

        sourceSetImplementation("iosX64", library)
        sourceSetImplementation("iosArm64", library)
        sourceSetImplementation("iosSimulatorArm64", library)

}
fun DependencyHandlerScope.iosMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) {

   // sourceSetImplementation("iosX64", dependency)
    sourceSetImplementation("iosArm64", dependency)
    sourceSetImplementation("iosSimulatorArm64", dependency)
}

// Web → wasmJs + js
fun DependencyHandlerScope.webMainImplementation(project: Project) = sourceSetImplementation("web", project)
fun DependencyHandlerScope.webMainImplementation(library: String) = sourceSetImplementation("web", library)
fun DependencyHandlerScope.webMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) = sourceSetImplementation("web", dependency)

// JS uniquement
fun DependencyHandlerScope.jsMainImplementation(project: Project) = sourceSetImplementation("js", project)
fun DependencyHandlerScope.jsMainImplementation(library: String) = sourceSetImplementation("js", library)
fun DependencyHandlerScope.jsMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) = sourceSetImplementation("js", dependency)

// WasmJs uniquement
fun DependencyHandlerScope.wasmJsMainImplementation(project: Project) = sourceSetImplementation("wasmJs", project)
fun DependencyHandlerScope.wasmJsMainImplementation(library: String) = sourceSetImplementation("wasmJs", library)
fun DependencyHandlerScope.wasmJsMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) = sourceSetImplementation("wasmJs", dependency)

// JVM → desktop + android
fun DependencyHandlerScope.jvmMainImplementation(project: Project) = sourceSetImplementation("desktop", project)
fun DependencyHandlerScope.jvmMainImplementation(library: String) = sourceSetImplementation("desktop", library)
fun DependencyHandlerScope.jvmMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) = sourceSetImplementation("desktop", dependency)