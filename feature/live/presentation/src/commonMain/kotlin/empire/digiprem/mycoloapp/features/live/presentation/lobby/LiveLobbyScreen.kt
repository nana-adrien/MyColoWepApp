package empire.digiprem.mycoloapp.features.live.presentation.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.components.MyColoEmptyContent
import empire.digiprem.mycoloapp.core.design_system.components.MyColoLoadingContent
import empire.digiprem.mycoloapp.core.design_system.components.MyColoUserAvatar
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LiveLobbyRoot(
    onNavigateToWatch: (String) -> Unit,
    onNavigateToBroadcast: () -> Unit = {},
    viewModel: LiveLobbyViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LiveLobbyEvent.NavigateToWatch -> onNavigateToWatch(event.sessionId)
                LiveLobbyEvent.NavigateToBroadcast -> onNavigateToBroadcast()
            }
        }
    }

    LiveLobbyScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveLobbyScreen(
    state: LiveLobbyState,
    onAction: (LiveLobbyAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.FiberManualRecord, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(10.dp))
                        Text("Live", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    if (state.isHost) {
                        IconButton(onClick = { onAction(LiveLobbyAction.OnStartLiveClick) }) {
                            Icon(Icons.Outlined.VideoCall, contentDescription = "Démarrer un live")
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (state.isHost) {
                ExtendedFloatingActionButton(
                    onClick = { onAction(LiveLobbyAction.OnStartLiveClick) },
                    icon = { Icon(Icons.Outlined.VideoCall, contentDescription = null) },
                    text = { Text("Démarrer un live") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        when {
            state.isLoading -> MyColoLoadingContent(modifier = Modifier.padding(paddingValues))
            state.sessions.isEmpty() -> MyColoEmptyContent(
                message = "Aucun live en cours pour le moment",
                modifier = Modifier.padding(paddingValues),
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.sessions, key = { it.id }) { session ->
                    LiveSessionCard(
                        session = session,
                        onClick = { onAction(LiveLobbyAction.OnJoinSession(session.id)) },
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun LiveSessionCard(
    session: LiveSession,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Vignette du live
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Outlined.PlayCircleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                )
                // Badge LIVE
                Surface(
                    modifier = Modifier.align(Alignment.TopStart).padding(10.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.error,
                ) {
                    Text(
                        "EN DIRECT",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onError,
                        letterSpacing = 0.5.sp,
                    )
                }
                // Compteur spectateurs
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(11.dp), tint = MaterialTheme.colorScheme.surface)
                        Text("${session.viewerCount}", fontSize = 10.sp, color = MaterialTheme.colorScheme.surface, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            // Infos
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MyColoUserAvatar(name = session.hostName, size = 36.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(session.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(session.hostName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                ) {
                    Text("Rejoindre", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
