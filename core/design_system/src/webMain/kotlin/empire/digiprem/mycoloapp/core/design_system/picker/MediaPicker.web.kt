package empire.digiprem.mycoloapp.core.design_system.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import empire.digiprem.mycoloapp.core.domain.enums.MimeType
import empire.digiprem.mycoloapp.core.domain.model.AppFile
import js.buffer.ArrayBuffer
import js.numbers.JsNumbers.toKotlinByte
import js.typedarrays.Int8Array
import kotlinx.coroutines.launch
import web.dom.document
import web.events.EventHandler
import web.file.File
import web.file.FileReader
import web.html.HTMLInputElement
import web.html.InputType
import web.html.file
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// webMain/commonWebMain/kotlin/MediaPicker.web.kt

@Composable
actual fun rememberGalleryPicker(
    maxImages: Int,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    val scope = rememberCoroutineScope()

    return remember(maxImages) {
        object : MediaPicker {
            override fun launch() {
                val input = document.createElement("input") as HTMLInputElement
                input.type = InputType.file
                input.accept = "image/*"
                input.multiple = maxImages > 1

                input.onchange = EventHandler {
                    val files = input.files
                    if (files != null && files.length > 0) {
                        scope.launch {
                            val result = (0 until minOf(files.length, maxImages))
                                .mapNotNull { i ->
                                    files.item(i)?.let { file ->
                                        AppFile(
                                            name = file.name,
                                            byteArray = file.readBytes(),
                                            mimeType = MimeType.fromString(file.type) ,
                                            path = file.webkitRelativePath
                                        )
                                    }
                                }
                            onResult(result)
                        }
                    } else {
                        onResult(emptyList())
                    }
                }
                input.click()
            }
        }
    }
}
@Composable
actual fun rememberCameraPicker(onResult: (AppFile?) -> Unit): MediaPicker {
    val scope = rememberCoroutineScope()

    return remember {
        object : MediaPicker {

            override fun launch() {
                val input = document.createElement("input") as HTMLInputElement
                input.type = InputType.file
                input.accept = "image/*"
                input.capture = "environment" // ← ouvre la caméra sur mobile
                input.onchange = EventHandler {
                    val file = input.files?.get(0)
                    if (file != null) {
                        scope.launch {
                            val bytes = file.readBytes()
                            onResult(
                                AppFile(
                                    name = file.name,
                                    byteArray = bytes,
                                    mimeType = MimeType.fromString(file.type) ,
                                    path = file.webkitRelativePath
                                )
                            )
                        }
                    } else {
                        onResult(null)
                    }
                }
                input.click()
            }
        }
    }
}

// Extension pour lire les bytes d'un File JS
private suspend fun File.readBytes(): ByteArray {
    return suspendCoroutine { continuation ->
        val reader = FileReader()
        reader.onloadend = EventHandler {
            val result = reader.result
            if (result is ArrayBuffer) {
                val int8Array = Int8Array(result)
                val bytes = ByteArray(int8Array.length) { int8Array[it].toKotlinByte() }
                continuation.resume(bytes)
            } else {
                continuation.resumeWithException(Exception("Failed to read file"))
            }
        }
        reader.onerror = EventHandler {
            continuation.resumeWithException(Exception("Failed to read file"))
        }
        reader.readAsArrayBuffer(this)
    }
}

@Composable
actual fun rememberFilePicker(
    allowedMimeTypes: List<MimeType>,
    allowMultiple: Boolean,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    val scope = rememberCoroutineScope()

    return remember(allowedMimeTypes, allowMultiple) {
        object : MediaPicker {
            override fun launch() {
                val input = document.createElement("input") as HTMLInputElement
                input.type = InputType.file
                input.accept = allowedMimeTypes.map { it.value } .joinToString(",")
                input.multiple = allowMultiple
                input.onchange = EventHandler {
                    val files = input.files
                    if (files != null && files.length > 0) {
                        scope.launch {
                            val result = (0 until files.length).mapNotNull { i ->
                                files.item(i)?.let { file ->
                                    AppFile(
                                        name = file.name,
                                        byteArray = file.readBytes(),
                                        mimeType = MimeType.fromString(file.type) ,
                                        path = file.webkitRelativePath
                                    )
                                }
                            }
                            onResult(result)
                        }
                    } else {
                        onResult(emptyList())
                    }
                }
                input.click()
            }
        }
    }
}