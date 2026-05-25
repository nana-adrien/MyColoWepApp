package empire.digiprem.mycoloapp.core.design_system.picker

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import empire.digiprem.mycoloapp.core.domain.enums.MimeType
import empire.digiprem.mycoloapp.core.domain.model.AppFile
import java.io.File

@Composable
actual fun rememberGalleryPicker(
    maxImages: Int,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    val context = LocalContext.current

    val launcher = if (maxImages == 1) {
        val single = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val mimeType = context.contentResolver.getType(uri) ?: "image/*"
                    onResult(listOf(AppFile(
                        name = uri.lastPathSegment ?: "image",
                        byteArray = bytes,
                        mimeType  = MimeType.fromString(mimeType) ,
                        path = uri.path
                    )))
                }
            } else onResult(emptyList())
        }
        remember { object : MediaPicker { override fun launch() = single.launch("image/*") } }
    } else {
        val multiple = rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxImages)
        ) { uris ->
            val files = uris.take(maxImages).mapNotNull { uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val mimeType = context.contentResolver.getType(uri) ?: "image/*"
                    AppFile(
                        name = uri.lastPathSegment ?: "image",
                        byteArray = bytes,
                        mimeType  = MimeType.fromString(mimeType) ,
                        path = uri.path
                    )
                }
            }
            onResult(files)
        }
        remember {
            object : MediaPicker {
                override fun launch() = multiple.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }
    }

    return launcher
}
@Composable
actual fun rememberCameraPicker(onResult: (AppFile?) -> Unit): MediaPicker {
    val context = LocalContext.current
    val tempFile = remember {
        File.createTempFile("mycolo_", ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }
    val tempUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success: Boolean ->
        if (success) onResult(tempUri.toAppFile(context))
        else onResult(null)
    }
    return remember { object : MediaPicker { override fun launch() = launcher.launch(tempUri) } }
}

private fun Uri.toAppFile(context: Context): AppFile? {
    return try {
        val contentResolver: ContentResolver = context.contentResolver
        val mimeType = contentResolver.getType(this) ?: "application/octet-stream"
        val name = lastPathSegment ?: "media"
        val bytes = contentResolver.openInputStream(this)?.use { it.readBytes() }
        AppFile(byteArray = bytes, name = name, mimeType  = MimeType.fromString(mimeType) , path = toString())
    } catch (e: Exception) {
        null
    }
}

@Composable
actual fun rememberFilePicker(
    allowedMimeTypes: List<MimeType>,
    allowMultiple: Boolean,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    val context = LocalContext.current
    val launcher = if (allowMultiple) {
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) { uris ->
            val files = uris.mapNotNull { uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val name = uri.lastPathSegment ?: "file"
                    val mimeType = context.contentResolver.getType(uri) ?: "*/*"
                    AppFile(name = name, byteArray = bytes, mimeType  = MimeType.fromString(mimeType) ,path = uri.path)
                }
            }
            onResult(files)
        }
    } else {
        val single = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val name = uri.lastPathSegment ?: "file"
                    val mimeType = context.contentResolver.getType(uri) ?: "*/*"
                    onResult(listOf(AppFile(name = name, byteArray = bytes,mimeType  = MimeType.fromString(mimeType) ,path = uri.path)))
                }
            } else {
                onResult(emptyList())
            }
        }
        // wrap en launcher multiple pour uniformiser
        object {
            fun launch(type: String) = single.launch(type)
        }.let {
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val bytes = stream.readBytes()
                        val name = uri.lastPathSegment ?: "file"
                        val mimeType = context.contentResolver.getType(uri) ?: "*/*"
                        onResult(listOf(AppFile(name = name, byteArray = bytes, mimeType  = MimeType.fromString(mimeType) ,path = uri.path)))
                    }
                } else onResult(emptyList())
            }
        }
    }

    val mimeType = (allowedMimeTypes.firstOrNull() ?:MimeType.ALL).value
    return remember { object : MediaPicker { override fun launch() = launcher.launch(mimeType) } }
}