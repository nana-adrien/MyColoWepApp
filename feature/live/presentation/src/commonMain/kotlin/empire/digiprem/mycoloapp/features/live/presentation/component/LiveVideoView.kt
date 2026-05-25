package empire.digiprem.mycoloapp.features.live.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun LiveVideoView(
    videoTrack: Any?,
    modifier: Modifier = Modifier,
)
