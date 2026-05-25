package empire.digiprem.mycoloapp.core.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyColoBrand(
    logoSize: Dp = 64.dp,
    logoFontSize: TextUnit = 22.sp,
    showAppName: Boolean = true,
    appNameFontSize: TextUnit = 22.sp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
            modifier = Modifier.size(logoSize),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "MC",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = logoFontSize,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        if (showAppName) {
            Text(
                text = "My Colo",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = appNameFontSize,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun MyColoBrandCompact(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
            modifier = Modifier.size(28.dp),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "MC",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Text("My Colo", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
    }
}
