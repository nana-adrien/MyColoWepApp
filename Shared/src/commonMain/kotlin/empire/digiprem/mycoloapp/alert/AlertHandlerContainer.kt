package empire.digiprem.mycoloapp.alert

import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.DeviceConfiguration
import empire.digiprem.mycoloapp.core.design_system.currentDeviceConfigure
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock


@Composable
fun AlertHandlerContainer(
    viewModel: AlertViewModel = koinViewModel(),
    content: @Composable () -> Unit,
) {
    val currentConfiguration = currentDeviceConfigure()
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize()
        //.recalculateWindowInsets()//(WindowInsets.systemBars))
    ) {

        // Contenu principal
        content()

        // Overlay des alertes — positionnement responsive
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(999f)
                .padding(alertPadding(currentConfiguration)),
            contentAlignment = alertAlignment(currentConfiguration),
        ) {
            Column(
                modifier = Modifier.widthIn(max = alertMaxWidth(currentConfiguration)),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End,
            ) {
                alerts.forEach { alert ->
                    key(alert.id) {
                        AnimatedAlertItem(
                            notification = alert,
                            onDismiss = { viewModel.dismiss(alert.id) },
                            currentConfiguration = currentConfiguration,
                        )
                    }
                }
            }
        }
    }

}

// ─────────────────────────────────────────────────────────────────────────────
// Helpers responsive
// ─────────────────────────────────────────────────────────────────────────────
private fun alertPadding(configuration: DeviceConfiguration) = when  {
    configuration.isMobileDevice() -> 12.dp  // Mobile : bas/centre
    !configuration.isCompact() -> 20.dp  // Tablet : coin supérieur droit
    else -> 24.dp  // Desktop : coin supérieur droit
}

private fun alertMaxWidth(configuration: DeviceConfiguration): Dp =when  {
    configuration.isMobileDevice() -> 250.dp  // Pleine largeur mobile
    !configuration.isCompact() -> 300.dp
    else-> 300.dp
}

@Composable
private fun alertAlignment(configuration: DeviceConfiguration): Alignment =when  {
    configuration.isMobileDevice() -> Alignment.TopCenter        // Mobile : bas centré
    else -> Alignment.TopEnd              // Tablet/Desktop : haut droite
}

// ─────────────────────────────────────────────────────────────────────────────
// Item animé
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AnimatedAlertItem(
    notification: AlertNotification,
    onDismiss: () -> Unit,
    currentConfiguration: DeviceConfiguration,
) {
    val slideIn = if (currentConfiguration.isCompact())
        slideInVertically { -it } + fadeIn()
    else
        slideInHorizontally { it } + fadeIn()

    val slideOut = if (currentConfiguration.isCompact())
        slideOutVertically { -it } + fadeOut()
    else
        slideOutHorizontally { it } + fadeOut()


    var showAlert by remember { mutableStateOf(false) }
    var showAlert2 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showAlert=true
        showAlert2=true
    }

    LaunchedEffect(showAlert) {
        if (!showAlert) {
            showAlert2=false
        }
    }
    LaunchedEffect(showAlert) {
        if (!showAlert2) {
            delay(300)
            onDismiss()
        }
    }


    AnimatedVisibility(
        visible = showAlert,
        enter = slideIn,
        exit = slideOut,
    ) {
        AlertItem(
            notification = notification,
            onDismiss = {
                showAlert=false
            },
            fullWidth = currentConfiguration.isCompact(),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Visuel d'une alerte — style Supabase-inspired
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AlertItem(
    notification: AlertNotification,
    onDismiss: () -> Unit,
    fullWidth: Boolean,
) {
    val scheme = notification.type.scheme()
    val shape = RoundedCornerShape(10.dp)

    // Barre de progression auto-dismiss
  var progress by remember { mutableFloatStateOf(1f) }
    LaunchedEffect(notification.id) {
        val start = Clock.System.now().toEpochMilliseconds()
        while (progress > 0f) {
            delay(16)
            val elapsed = Clock.System.now().toEpochMilliseconds() - start
            progress = 1f - (elapsed / notification.durationMs.toFloat()).coerceIn(0f, 1f)
        }
    }

    Box(
        modifier = Modifier
            .then(if (fullWidth) Modifier.fillMaxWidth().height(80.dp) else Modifier.widthIn(min = 300.dp)).height( 80.dp)
            .shadow(elevation = 6.dp, shape = shape)
            .clip(shape)
            .background(scheme.background)
            .border(width = 1.dp, color = scheme.border, shape = shape)
    ) {
        // Barre colorée gauche
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(scheme.accent)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            // Corps principal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Icône
                Icon(
                    imageVector = notification.type.icon(),
                    contentDescription = null,
                    tint = scheme.accent,
                    modifier = Modifier.size(18.dp).padding(top = 1.dp),
                )

                // Texte
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.type.label(),
                        color = scheme.accent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text =notification.message,
                        color = scheme.text,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                    )
                }

                // Bouton fermer
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = scheme.text.copy(alpha = 0.5f),
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            // Barre de progression
            LinearProgressIndicator(
                progress = { .5f },
                modifier = Modifier.fillMaxWidth().height(2.dp),
                color = scheme.accent.copy(alpha = 0.6f),
                trackColor = Color.Transparent,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Schéma couleur par type
// ─────────────────────────────────────────────────────────────────────────────
private data class AlertColorScheme(
    val background: Color,
    val border: Color,
    val accent: Color,
    val text: Color,
)

private fun AlertType.scheme() = when (this) {
    AlertType.ERROR -> AlertColorScheme(
        background = Color(0xFF1C1014),
        border = Color(0xFF4A1D22),
        accent = Color(0xFFF87171),
        text = Color(0xFFFCA5A5),
    )

    AlertType.SUCCESS -> AlertColorScheme(
        background = Color(0xFF0D1C15),
        border = Color(0xFF16532D),
        accent = Color(0xFF4ADE80),
        text = Color(0xFF86EFAC),
    )

    AlertType.WARNING -> AlertColorScheme(
        background = Color(0xFF1C1608),
        border = Color(0xFF4A3720),
        accent = Color(0xFFFBBF24),
        text = Color(0xFFFDE68A),
    )

    AlertType.INFO -> AlertColorScheme(
        background = Color(0xFF0D1520),
        border = Color(0xFF1D3A5C),
        accent = Color(0xFF60A5FA),
        text = Color(0xFF93C5FD),
    )
}

private fun AlertType.icon(): ImageVector = when (this) {
    AlertType.ERROR -> Icons.Default.Error
    AlertType.SUCCESS -> Icons.Default.CheckCircle
    AlertType.WARNING -> Icons.Default.Warning
    AlertType.INFO -> Icons.Default.Info
}

private fun AlertType.label(): String = when (this) {
    AlertType.ERROR -> "ERREUR"
    AlertType.SUCCESS -> "SUCCÈS"
    AlertType.WARNING -> "ATTENTION"
    AlertType.INFO -> "INFO"
}