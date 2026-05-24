package empire.digiprem.mycoloapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.countdown_days
import mycolowepapp.shared.generated.resources.countdown_expired
import mycolowepapp.shared.generated.resources.countdown_hours
import mycolowepapp.shared.generated.resources.countdown_minutes
import mycolowepapp.shared.generated.resources.countdown_seconds
import org.jetbrains.compose.resources.stringResource

@Composable
fun CountdownTimer(
    targetDate: Long,
    modifier: Modifier = Modifier
) {
    var remainingMillis by remember { mutableStateOf(targetDate - currentTimeMillis()) }

    LaunchedEffect(targetDate) {
        while (remainingMillis > 0) {
            delay(1000L)
            remainingMillis = targetDate - currentTimeMillis()
        }
    }

    if (remainingMillis <= 0) {
        Box(
            modifier = modifier
                .background(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.countdown_expired),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    } else {
        val days = remainingMillis / (1000 * 60 * 60 * 24)
        val hours = (remainingMillis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (remainingMillis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (remainingMillis % (1000 * 60)) / 1000

        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CountdownUnit(value = days, label = stringResource(Res.string.countdown_days))
            CountdownSeparator()
            CountdownUnit(value = hours, label = stringResource(Res.string.countdown_hours))
            CountdownSeparator()
            CountdownUnit(value = minutes, label = stringResource(Res.string.countdown_minutes))
            CountdownSeparator()
            CountdownUnit(value = seconds, label = stringResource(Res.string.countdown_seconds))
        }
    }
}

@Composable
private fun CountdownUnit(value: Long, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(width = 64.dp, height = 64.dp)
                .background(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.toString().padStart(2, '0'),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun CountdownSeparator() {
    Text(
        text = ":",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White.copy(alpha = 0.7f),
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

expect fun currentTimeMillis(): Long
