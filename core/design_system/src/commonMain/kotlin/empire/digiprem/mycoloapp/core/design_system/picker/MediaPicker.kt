package empire.digiprem.mycoloapp.core.design_system.picker

import androidx.compose.runtime.Composable
import empire.digiprem.mycoloapp.core.domain.enums.MimeType
import empire.digiprem.mycoloapp.core.domain.model.AppFile

interface MediaPicker {
    fun launch()
}

@Composable
expect fun rememberGalleryPicker(
    maxImages: Int = 1,
    onResult: (List<AppFile>) -> Unit): MediaPicker

@Composable
expect fun rememberCameraPicker(onResult: (AppFile?) -> Unit): MediaPicker

@Composable
expect fun rememberFilePicker(
    allowedMimeTypes: List<MimeType> = listOf(MimeType.ALL),
    allowMultiple: Boolean=true,
    onResult: (List<AppFile>) -> Unit
): MediaPicker