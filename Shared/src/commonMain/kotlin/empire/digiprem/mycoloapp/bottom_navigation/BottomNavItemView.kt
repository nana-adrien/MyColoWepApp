package empire.digiprem.mycoloapp.bottom_navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.components.icon.AppIcon
import empire.digiprem.mycoloapp.core.design_system.components.icon.AppIconResource

@Composable
 fun BottomNavItemView(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: AppIconResource,
) {
    val tint by animateColorAsState(
        targetValue   = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.7f),
        animationSpec = tween(200),
        label  = "navTint",
    )
    val animateWidth by animateDpAsState(
        targetValue   = if (isSelected)24.dp else 0.dp,
        animationSpec = tween(200),
        label  = "navWidth",
    )
    Column(
        modifier            = Modifier.animateContentSize()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        AppIcon(
            icon=icon,
            contentDescription = label,
            tint = tint,
        )
        Text(
            text       = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color      = tint,
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(animateWidth)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(tint),
            )
        }
    }
}