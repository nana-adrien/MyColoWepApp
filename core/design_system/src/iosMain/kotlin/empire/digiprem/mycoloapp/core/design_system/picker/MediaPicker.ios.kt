package empire.digiprem.mycoloapp.core.design_system.picker


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import empire.digiprem.mycoloapp.core.domain.enums.MimeType
import empire.digiprem.mycoloapp.core.domain.model.AppFile
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.accessibilityPath
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeAudio
import platform.UniformTypeIdentifiers.UTTypeData
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.UTTypeMovie
import platform.UniformTypeIdentifiers.UTTypePDF
import platform.UniformTypeIdentifiers.UTTypePlainText
import platform.darwin.NSObject

@Composable
actual fun rememberGalleryPicker(
    maxImages: Int,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    return remember(maxImages) {
        object : MediaPicker {
            override fun launch() {
                var config = PHPickerConfiguration()
                config.selectionLimit = maxImages.toLong() // 0 = illimité
                config.filter = PHPickerFilter.imagesFilter

                val picker = PHPickerViewController(configuration = config)

                val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(
                        picker: PHPickerViewController,
                        didFinishPicking: List<*>
                    ) {
                        picker.dismissViewControllerAnimated(true, completion = null)

                        val results = didFinishPicking.filterIsInstance<PHPickerResult>()
                        val files = mutableListOf<AppFile>()
                        var remaining = results.size

                        if (remaining == 0) {
                            onResult(emptyList())
                            return
                        }

                        results.forEach { result ->
                            result.itemProvider.loadDataRepresentationForTypeIdentifier(
                                UTTypeImage.identifier
                            ) { data, error ->
                                if (data != null) {
                                    files.add(AppFile(
                                        name = "image_${files.size}.jpg",
                                        byteArray = data.toByteArray(),
                                        mimeType = MimeType.IMAGE_JPEG,
                                    ))
                                }
                                remaining--
                                if (remaining == 0) onResult(files)
                            }
                        }
                    }
                }

                picker.delegate = delegate
                UIApplication.sharedApplication.keyWindow
                    ?.rootViewController
                    ?.presentViewController(picker, animated = true, completion = null)
            }
        }
    }
}

@Composable
actual fun rememberCameraPicker(onResult: (AppFile?) -> Unit): MediaPicker {
    return remember {
        IosPicker(
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera,
            onResult = onResult,
        )
    }
}

private class IosPicker(
    private val sourceType: UIImagePickerControllerSourceType,
    private val onResult: (AppFile?) -> Unit,
) : MediaPicker {

    @OptIn(ExperimentalForeignApi::class)
    override fun launch() {
        val picker = UIImagePickerController()
        picker.sourceType = sourceType
        picker.allowsEditing = false

        val delegate = object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {

            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>,
            ) {
                val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
                val appFile = image?.let {
                    val data = UIImageJPEGRepresentation(it, 0.85) ?: return@let null
                    val bytes = data.toByteArray()
                    AppFile(byteArray = bytes, name = "photo.jpg", mimeType = MimeType.IMAGE_JPEG)
                }
                onResult(appFile)
                picker.dismissViewControllerAnimated(true, null)
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                onResult(null)
                picker.dismissViewControllerAnimated(true, null)
            }
        }

        picker.delegate = delegate
        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(picker, animated = true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    return ByteArray(size).also { bytes ->
        bytes.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), bytes.toCValues(), length)
        }
    }
}

@Composable
actual fun rememberFilePicker(
    allowedMimeTypes: List<MimeType>,
    allowMultiple: Boolean,
    onResult: (List<AppFile>) -> Unit
): MediaPicker {
    return remember {
        object : MediaPicker {
            override fun launch() {
                val types = allowedMimeTypes.map { it.value }.map { mime ->
                    when {
                        mime == "*/*" -> UTTypeData
                        mime.startsWith("image/") -> UTTypeImage
                        mime.startsWith("video/") -> UTTypeMovie
                        mime.startsWith("audio/") -> UTTypeAudio
                        mime == "application/pdf" -> UTTypePDF
                        mime.startsWith("text/") -> UTTypePlainText
                        else -> UTTypeData
                    }
                }

                val picker = UIDocumentPickerViewController(
                    forOpeningContentTypes = types
                )
                picker.allowsMultipleSelection = allowMultiple

                val delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
                    override fun documentPicker(
                        controller: UIDocumentPickerViewController,
                        didPickDocumentsAtURLs: List<*>
                    ) {
                        val files = didPickDocumentsAtURLs.mapNotNull { url ->
                            (url as? NSURL)?.let { nsUrl ->
                                nsUrl.startAccessingSecurityScopedResource()
                                val data = NSData.dataWithContentsOfURL(nsUrl)
                                nsUrl.stopAccessingSecurityScopedResource()
                                data?.let {
                                    AppFile(
                                        name = nsUrl.lastPathComponent ?: "file",
                                        byteArray = it.toByteArray(),
                                        mimeType = allowedMimeTypes.firstOrNull() ?: MimeType.ALL
                                    )
                                }
                            }
                        }
                        onResult(files)
                    }

                    override fun documentPickerWasCancelled(
                        controller: UIDocumentPickerViewController
                    ) {
                        onResult(emptyList())
                    }
                }

                picker.delegate = delegate

                UIApplication.sharedApplication.keyWindow
                    ?.rootViewController
                    ?.presentViewController(picker, animated = true, completion = null)
            }
        }
    }
}