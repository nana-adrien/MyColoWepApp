package empire.digiprem.mycoloapp.features.live.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.outlined.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycoloapp.features.live.domain.model.LiveKitWebData
import kotlinx.browser.window

@Composable
actual fun LiveVideoView(
    videoTrack: Any?,
    modifier: Modifier,
) {
    val data = videoTrack as? LiveKitWebData

    if (data == null) {
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
        return
    }

    Box(
        modifier = modifier.background(Color(0xFF1a1a2e)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                if (data.isPublishing) Icons.Outlined.Videocam else Icons.Outlined.Videocam,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = Color.White.copy(alpha = 0.85f),
            )
            Text(
                if (data.isPublishing) "Caméra prête" else "Live en cours",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.9f),
            )
            Button(
                onClick = { window.open(buildPlayerUrl(data), "_blank") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6c5ce7)),
            ) {
                Icon(
                    Icons.Outlined.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White,
                )
                Text(
                    if (data.isPublishing) "  Diffuser en direct" else "  Regarder en direct",
                    fontSize = 13.sp,
                    color = Color.White,
                )
            }
        }
    }
}

private fun buildPlayerUrl(data: LiveKitWebData): String {
    val encodedUrl = data.livekitUrl.urlEncode()
    return "/livekit-player.html?url=$encodedUrl&token=${data.token}&publishing=${data.isPublishing}"
}

private fun String.urlEncode(): String = this
    .replace("%", "%25")
    .replace(" ", "%20")
    .replace(":", "%3A")
    .replace("/", "%2F")
    .replace("?", "%3F")
    .replace("=", "%3D")
    .replace("&", "%26")
    .replace("+", "%2B")
    .replace("#", "%23")
    .replace("@", "%40")
