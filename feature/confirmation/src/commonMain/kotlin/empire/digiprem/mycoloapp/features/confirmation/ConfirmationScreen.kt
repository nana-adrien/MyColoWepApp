package empire.digiprem.mycoloapp.features.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycoloapp.core.design_system.theme.Primary
import empire.digiprem.mycoloapp.core.design_system.theme.PrimaryContainer
import empire.digiprem.mycoloapp.core.design_system.theme.Secondary
import empire.digiprem.mycoloapp.core.design_system.theme.SecondaryContainer
import empire.digiprem.mycoloapp.core.ui.MyColoFilledButton
import empire.digiprem.mycoloapp.core.ui.MyColoOutlinedButton
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.confirmation_go_home
import mycolowepapp.shared.generated.resources.confirmation_reference_label
import mycolowepapp.shared.generated.resources.confirmation_share
import mycolowepapp.shared.generated.resources.confirmation_step_confirmed
import mycolowepapp.shared.generated.resources.confirmation_step_submitted
import mycolowepapp.shared.generated.resources.confirmation_step_verification
import mycolowepapp.shared.generated.resources.confirmation_subtitle
import mycolowepapp.shared.generated.resources.confirmation_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmationScreen(
    referenceNumber: String,
    onNavigateHome: () -> Unit
) {
    val height = LocalWindowInfo.current.containerDpSize.height
    Column(
        modifier = Modifier
            .height(height)
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Success icon
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(SecondaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.confirmation_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(Res.string.confirmation_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Reference number card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.confirmation_reference_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = referenceNumber,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Primary,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Timeline steps
        RegistrationTimeline()

        Spacer(modifier = Modifier.height(40.dp))

        // Action buttons
        MyColoFilledButton(
            text = stringResource(Res.string.confirmation_go_home),
            onClick = onNavigateHome
        )

        Spacer(modifier = Modifier.height(12.dp))

        MyColoOutlinedButton(
            text = stringResource(Res.string.confirmation_share),
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun RegistrationTimeline() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            TimelineStep(
                number = 1,
                label = stringResource(Res.string.confirmation_step_submitted),
                state = TimelineState.DONE
            )
            TimelineConnector()
            TimelineStep(
                number = 2,
                label = stringResource(Res.string.confirmation_step_verification),
                state = TimelineState.CURRENT
            )
            TimelineConnector()
            TimelineStep(
                number = 3,
                label = stringResource(Res.string.confirmation_step_confirmed),
                state = TimelineState.TODO
            )
        }
    }
}

private enum class TimelineState { DONE, CURRENT, TODO }

@Composable
private fun TimelineStep(number: Int, label: String, state: TimelineState) {
    val (bgColor, iconColor, textColor) = when (state) {
        TimelineState.DONE -> Triple(Secondary, Color.White, MaterialTheme.colorScheme.onSurface)
        TimelineState.CURRENT -> Triple(Primary, Color.White, Primary)
        TimelineState.TODO -> Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (state == TimelineState.DONE) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = number.toString(),
                    fontWeight = FontWeight.Bold,
                    color = iconColor,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (state == TimelineState.CURRENT) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}

@Composable
private fun TimelineConnector() {
    Box(
        modifier = Modifier
            .padding(start = 15.dp)
            .width(2.dp)
            .height(24.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}
