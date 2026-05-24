package empire.digiprem.mycoloapp.core.design_system.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycoloapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycoloapp.core.design_system.theme.Primary

data class NavItem(val id: String, val label: String)

@Composable
fun ColumnScope.MyColoNavBar(
    containerColor: Color=Color.Transparent,
    contentColor: Color=Color.White,
    scrollState: LazyListState,
    navItems: List<NavItem> = emptyList(),
    selectedNavItem: NavItem? = null,
    onNavItemClick: (NavItem) -> Unit = {},
    onAdminClick: (() -> Unit)? = null,
    onRegisterClick: (() -> Unit)? = null,
) {
    val density = LocalDensity.current
    val isMobile = currentDeviceConfigure().isCompact()

    val scrollOffset = scrollState.firstVisibleItemScrollOffset.toFloat()
    val bgColor by animateColorAsState(
        targetValue = if (scrollState.firstVisibleItemIndex > 0 || scrollOffset > 100f)
            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        else containerColor,
        label = "navBg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (scrollState.firstVisibleItemIndex > 0 || scrollOffset > 100f)
            MaterialTheme.colorScheme.onSurface
        else contentColor,
        label = "navContent"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(bgColor)
            .padding(horizontal = if (isMobile) 16.dp else 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.dp).background(Primary, CircleShape),
                contentAlignment = Alignment.Center
            ) { Text("MC", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 11.sp) }
            Text(
                text = "  My Colo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }

        if (!isMobile) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                navItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onNavItemClick(item) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selectedNavItem?.id == item.id) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedNavItem?.id == item.id) Primary else contentColor
                        )
                    }
                }
                onAdminClick?.let {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(contentColor.copy(alpha = 0.1f))
                            .clickable(onClick = it)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Admin", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = contentColor)
                    }
                }
            }
        } else {
            onAdminClick?.let {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(contentColor.copy(alpha = 0.1f))
                        .clickable(onClick = it)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text("Admin", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = contentColor)
                }
            }
        }
    }

    AnimatedVisibility(scrollState.firstVisibleItemIndex > 0) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
