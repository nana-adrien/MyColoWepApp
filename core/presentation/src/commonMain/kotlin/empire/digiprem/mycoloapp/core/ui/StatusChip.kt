package empire.digiprem.mycoloapp.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.theme.AmberContainer
import empire.digiprem.mycoloapp.core.design_system.theme.OnAmberContainer
import empire.digiprem.mycoloapp.core.design_system.theme.SecondaryContainer
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.status_pending
import mycolowepapp.shared.generated.resources.status_rejected
import mycolowepapp.shared.generated.resources.status_validated
import org.jetbrains.compose.resources.stringResource

enum class ParticipantChipStatus {
    VALIDATED,
    PENDING,
    REJECTED
}

@Composable
fun StatusChip(status: ParticipantChipStatus, modifier: Modifier = Modifier) {
    val (text, backgroundColor, textColor) = when (status) {
        ParticipantChipStatus.VALIDATED -> Triple(
            stringResource(Res.string.status_validated),
            SecondaryContainer,
            Color(0xFF1B5E20)
        )
        ParticipantChipStatus.PENDING -> Triple(
            stringResource(Res.string.status_pending),
            AmberContainer,
            OnAmberContainer
        )
        ParticipantChipStatus.REJECTED -> Triple(
            stringResource(Res.string.status_rejected),
            Color(0xFFFFDAD6),
            Color(0xFF410002)
        )
    }

    Text(
        text = text,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        color = textColor,
        fontWeight = FontWeight.Medium
    )
}
