package empire.digiprem.mycoloapp.core.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.swing.JFileChooser

// desktopMain/kotlin/Platform.kt

actual fun downloadCsvFile(csvContent: String, fileName: String) {
    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        try {
            // ✅ Ouvre une boîte de dialogue de sauvegarde
            val fileChooser = JFileChooser().apply {
                dialogTitle = "Enregistrer le fichier"
                selectedFile = java.io.File(fileName)
                fileFilter = object : javax.swing.filechooser.FileFilter() {
                    override fun accept(f: java.io.File) =
                        f.isDirectory || f.name.endsWith(".csv", ignoreCase = true)
                    override fun getDescription() = "Fichiers CSV (*.csv)"
                }
            }

            val result = fileChooser.showSaveDialog(null)

            if (result == JFileChooser.APPROVE_OPTION) {
                var file = fileChooser.selectedFile
                // ✅ Ajouter .csv si manquant
                if (!file.name.endsWith(".csv", ignoreCase = true)) {
                    file = java.io.File(file.absolutePath + ".csv")
                }
                file.writeText(csvContent, Charsets.UTF_8)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}