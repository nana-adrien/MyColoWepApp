package empire.digiprem.mycoloapp.core.design_system.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import empire.digiprem.mycoloapp.core.domain.enums.MimeType
import empire.digiprem.mycoloapp.core.domain.model.AppFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
actual fun rememberGalleryPicker(
    maxImages: Int,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    val scope = rememberCoroutineScope()

    return remember(maxImages) {
        object : MediaPicker {
            override fun launch() {
                scope.launch(Dispatchers.IO) {
                    val chooser = JFileChooser().apply {
                        isMultiSelectionEnabled = maxImages > 1
                        fileFilter = object : javax.swing.filechooser.FileFilter() {
                            override fun accept(f: java.io.File) =
                                f.isDirectory || f.name.matches(
                                    Regex(".*\\.(jpg|jpeg|png|gif|webp|bmp)", RegexOption.IGNORE_CASE)
                                )
                            override fun getDescription() = "Images"
                        }
                    }

                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        val selected = if (maxImages == 1) {
                            listOf(chooser.selectedFile)
                        } else {
                            chooser.selectedFiles.take(maxImages).toList()
                        }

                        val files = selected.map { file ->
                            AppFile(
                                name = file.name,
                                byteArray = file.readBytes(),
                                mimeType = "image/${file.extension.lowercase()}",
                                path = file.absolutePath
                            )
                        }
                        withContext(Dispatchers.Main) { onResult(files) }
                    } else {
                        withContext(Dispatchers.Main) { onResult(emptyList()) }
                    }
                }
            }
        }
    }
}
@Composable
actual fun rememberCameraPicker(onResult: (AppFile?) -> Unit): MediaPicker {
    return remember { object : MediaPicker { override fun launch() = onResult(null) } }
}

@Composable
actual fun rememberFilePicker(
    allowedMimeTypes: List<MimeType>,
    allowMultiple: Boolean,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    val scope = rememberCoroutineScope()

    return remember {
        object : MediaPicker {
            override fun launch() {
                scope.launch(Dispatchers.IO) {
                    val fileChooser = JFileChooser().apply {
                        isMultiSelectionEnabled = allowMultiple
                        dialogTitle = "Choisir un fichier"

                        // Filtres selon mimeTypes
                        val extensions = allowedMimeTypes.map { it.value } .flatMap { mime ->
                            when {
                                mime == "*/*" -> emptyList()
                                mime == "image/*" -> listOf("jpg", "jpeg", "png", "gif", "webp")
                                mime == "video/*" -> listOf("mp4", "avi", "mov", "mkv")
                                mime == "audio/*" -> listOf("mp3", "wav", "ogg", "flac")
                                mime == "application/pdf" -> listOf("pdf")
                                else -> emptyList()
                            }
                        }

                        if (extensions.isNotEmpty()) {
                            fileFilter = object : javax.swing.filechooser.FileFilter() {
                                override fun accept(f: java.io.File): Boolean {
                                    return f.isDirectory || extensions.any {
                                        f.name.endsWith(".$it", ignoreCase = true)
                                    }
                                }
                                override fun getDescription() = extensions.joinToString(", ")
                            }
                        }
                    }

                    val result = fileChooser.showOpenDialog(null)

                    if (result == JFileChooser.APPROVE_OPTION) {
                        val files = if (allowMultiple) {
                            fileChooser.selectedFiles.map { file ->
                                AppFile(
                                    name = file.name,
                                    byteArray = file.readBytes(),

                                    mimeType = MimeType.fromExtension(file.extension)
                                        ?: allowedMimeTypes.firstOrNull()
                                        ?: MimeType.ALL,
                                    path = file.absolutePath
                                )
                            }
                        } else {
                            listOf(
                                AppFile(
                                    name = fileChooser.selectedFile.name,
                                    byteArray = fileChooser.selectedFile.readBytes(),
                                   mimeType =allowedMimeTypes.firstOrNull() ?: MimeType.ALL,
                                    path = fileChooser.selectedFile.absolutePath
                                )
                            )
                        }
                        withContext(Dispatchers.Main) { onResult(files) }
                    } else {
                        withContext(Dispatchers.Main) { onResult(emptyList()) }
                    }
                }
            }
        }
    }
}