package extensions

import org.gradle.api.Project
import java.util.Locale
import kotlin.collections.joinToString
import kotlin.text.drop
import kotlin.text.lowercase
import kotlin.text.replace
import kotlin.text.replaceFirstChar
import kotlin.text.split
import kotlin.text.titlecase


fun Project.relativePackageName():String{
    return path
        .replace(':', '.')
        .lowercase()
}
fun Project.pathToPackageName(): String {
    return "empire.digiprem.mycoloapp${relativePackageName()}"
}

fun Project.pathToResourcePrefix(): String {
    return path
        .replace(':', '_')
        .lowercase().drop(1) + "_"
}
fun Project.pathToFrameworkName(): String {
    val parts= path.split(":","_","-"," ")
    return "MyColo" +  parts.joinToString("") { parts->
            parts.replaceFirstChar {
                it.titlecase(Locale.ROOT)
            }
        }
}