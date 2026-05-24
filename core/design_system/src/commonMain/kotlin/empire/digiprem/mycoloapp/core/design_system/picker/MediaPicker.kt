package empire.digiprem.mycoloapp.core.presentation.picker

import androidx.compose.runtime.Composable
import empire.digiprem.mycoloapp.core.domain.model.AppFile

interface MediaPicker {
    fun launch()
}

@Composable
expect fun rememberGalleryPicker(onResult: (AppFile?) -> Unit): MediaPicker

@Composable
expect fun rememberCameraPicker(onResult: (AppFile?) -> Unit): MediaPicker
