package empire.digiprem.mycoloapp.core.design_system.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import empire.digiprem.mycoloapp.core.design_system.currentDeviceConfigure

@Composable
fun AdaptativeContainerLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val device = currentDeviceConfigure()
    val widthFraction = if (device.isCompact()) 0.97f else 0.85f
    Box(
        modifier = modifier.fillMaxWidth(widthFraction),
        contentAlignment = Alignment.Center,
        content = { content() }
    )
}
