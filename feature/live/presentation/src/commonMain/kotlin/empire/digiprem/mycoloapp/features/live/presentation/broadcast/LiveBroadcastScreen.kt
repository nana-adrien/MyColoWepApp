package empire.digiprem.mycoloapp.features.live.presentation.broadcast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.components.MyColoPrimaryButton
import empire.digiprem.mycoloapp.features.live.presentation.LiveNotAvailableScreen
import empire.digiprem.mycoloapp.features.live.presentation.component.LiveVideoView
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LiveBroadcastRoot(
    onNavigateBack: () -> Unit,
    viewModel: LiveBroadcastViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LiveBroadcastEvent.NavigateBack -> onNavigateBack()
                LiveBroadcastEvent.LiveStarted -> {}
                LiveBroadcastEvent.LiveStopped -> onNavigateBack()
            }
        }
    }

    if (state.isNotSupported) {
        LiveNotAvailableScreen(onNavigateBack = onNavigateBack)
    } else {
        LiveBroadcastScreen(state = state, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveBroadcastScreen(
    state: LiveBroadcastState,
    onAction: (LiveBroadcastAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onAction(LiveBroadcastAction.OnNavigateBack) }) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = "Retour",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        if (state.isLive) "En direct" else "Démarrer un live",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Flux caméra local (LiveKit VideoView)
            Box(
                modifier = Modifier.fillMaxWidth().height(220.dp)
                    .background(MaterialTheme.colorScheme.inverseSurface, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                if (state.isCameraEnabled && state.localVideoTrack != null) {
                    LiveVideoView(
                        videoTrack = state.localVideoTrack,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else if (!state.isCameraEnabled) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.VideocamOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.4f)
                        )
                        Text(
                            "Caméra désactivée",
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Videocam,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            "Initialisation caméra…",
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }
                if (state.isLive) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.error,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                Icons.Outlined.FiberManualRecord,
                                contentDescription = null,
                                modifier = Modifier.size(8.dp),
                                tint = MaterialTheme.colorScheme.onError
                            )
                            Text(
                                "EN DIRECT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onError,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                    state.session?.let { session ->
                        Row(
                            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(
                                Icons.Outlined.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.inverseOnSurface
                            )
                            Text(
                                "${session.viewerCount}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        }
                    }
                }
            }

            // Contrôles micro / caméra
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MediaControlChip(
                    label = if (state.isMicEnabled) "Micro activé" else "Micro coupé",
                    icon = if (state.isMicEnabled) Icons.Outlined.Mic else Icons.Outlined.MicOff,
                    enabled = state.isMicEnabled,
                    onClick = { onAction(LiveBroadcastAction.OnToggleMic) },
                )
                MediaControlChip(
                    label = if (state.isCameraEnabled) "Caméra" else "Caméra off",
                    icon = if (state.isCameraEnabled) Icons.Outlined.Videocam else Icons.Outlined.VideocamOff,
                    enabled = state.isCameraEnabled,
                    onClick = { onAction(LiveBroadcastAction.OnToggleCamera) },
                )
            }

            // Titre du live (seulement avant démarrage)
            if (!state.isLive) {
                OutlinedTextField(
                    value = state.titleInput,
                    onValueChange = { onAction(LiveBroadcastAction.OnTitleChange(it)) },
                    label = { Text("Titre du live") },
                    placeholder = { Text("Ex: Séance coiffure en direct…") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                )
            } else {
                state.session?.let { session ->
                    Text(
                        session.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            // Message d'erreur
            state.error?.let { error ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                ) {
                    Text(
                        error,
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Bouton principal
            if (!state.isLive) {
                MyColoPrimaryButton(
                    text = "Démarrer le live",
                    onClick = { onAction(LiveBroadcastAction.OnStartLive) },
                    isLoading = state.isStarting,
                    enabled = state.titleInput.isNotBlank() && !state.isStarting,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                OutlinedButton(
                    onClick = { onAction(LiveBroadcastAction.OnStopLive) },
                    enabled = !state.isStopping,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)
                    ),
                ) {
                    if (state.isStopping) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.error,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Outlined.StopCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("Arrêter le live", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaControlChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = enabled,
        onClick = onClick,
        label = { Text(label, fontSize = 11.sp) },
        leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(15.dp)) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )
}
