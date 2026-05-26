package empire.digiprem.mycoloapp.core.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// desktopMain/kotlin/Platform.kt

actual fun openPrintWindow(htmlContent: String) {
    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        // ✅ Crée un fichier HTML temporaire et l'ouvre dans le navigateur
        val tempFile = java.io.File.createTempFile("print_", ".html").apply {
            writeText(htmlContent, Charsets.UTF_8)
            deleteOnExit()
        }
        // Ouvre le fichier dans le navigateur par défaut
        java.awt.Desktop.getDesktop().browse(tempFile.toURI())
    }
}