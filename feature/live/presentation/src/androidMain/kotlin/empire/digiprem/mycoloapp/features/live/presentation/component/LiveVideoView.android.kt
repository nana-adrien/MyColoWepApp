package empire.digiprem.mycoloapp.features.live.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.livekit.android.renderer.TextureViewRenderer
import io.livekit.android.room.track.VideoTrack

@Composable
actual fun LiveVideoView(
    videoTrack: Any?,
    modifier: Modifier,
) {
    val track = videoTrack as? VideoTrack
    if (track != null) {
        AndroidView(
            factory = { context ->
                TextureViewRenderer(context).also { renderer ->
                    try {
                        renderer.init(null, null)
                        track.addRenderer(renderer)
                    } catch (_: Exception) {}
                }
            },
            update = {},
            onRelease = { renderer ->
                try {
                    track.removeRenderer(renderer)
                    renderer.release()
                } catch (_: Exception) {}
            },
            modifier = modifier,
        )
    } else {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.inverseSurface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.VideocamOff,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
            )
        }
    }
}
