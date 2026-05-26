package empire.digiprem.mycoloapp.features.live.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.outlined.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycoloapp.features.live.domain.model.LiveKitWebData
import java.awt.Desktop
import java.net.URI
import java.net.URLEncoder

@Composable
actual fun LiveVideoView(
    videoTrack: Any?,
    modifier: Modifier,
) {
    val data = videoTrack as? LiveKitWebData

    Box(
        modifier = modifier.background(Color(0xFF0d0d1a)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                if (data != null) Icons.Outlined.Videocam else Icons.Outlined.VideocamOff,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = Color.White.copy(alpha = if (data != null) 0.85f else 0.4f),
            )
            Text(
                if (data != null) {
                    if (data.isPublishing) "Diffusion ouverte dans le navigateur" else "Live ouvert dans le navigateur"
                } else {
                    "Flux vidéo non disponible sur Desktop"
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.75f),
            )
            if (data != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Utilisez la fenêtre du navigateur pour diffuser ou regarder.",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.45f),
                )
                Button(
                    onClick = { openBrowserLive(data) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6c5ce7)),
                ) {
                    Icon(
                        Icons.Outlined.OpenInBrowser,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White,
                    )
                    Text(
                        "  Ré-ouvrir dans le navigateur",
                        fontSize = 12.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

private fun openBrowserLive(data: LiveKitWebData) {
    try {
        val encodedUrl = URLEncoder.encode(data.livekitUrl, "UTF-8")
        val encodedToken = URLEncoder.encode(data.token, "UTF-8")
        val uri = URI("https://meet.livekit.io/custom?liveKitUrl=$encodedUrl&token=$encodedToken")
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(uri)
        }
    } catch (_: Exception) {}
}
