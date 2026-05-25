package empire.digiprem.mycoloapp.features.live.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DesktopAccessDisabled
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@androidx.compose.runtime.Composable
actual fun LiveVideoView(videoTrack: Any?, modifier: androidx.compose.ui.Modifier) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.inverseSurface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.DesktopAccessDisabled,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.4f),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Live non disponible sur Desktop",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Rejoignez depuis votre mobile",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.4f),
        )
    }
}