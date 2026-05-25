package empire.digiprem.mycoloapp.features.live.presentation.watch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import empire.digiprem.mycoloapp.core.design_system.components.MyColoLoadingContent
import empire.digiprem.mycoloapp.core.design_system.components.MyColoUserAvatar
import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import empire.digiprem.mycoloapp.features.live.presentation.LiveNotAvailableScreen
import empire.digiprem.mycoloapp.features.live.presentation.component.LiveVideoView
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun LiveWatchRoot(
    sessionId: String,
    onNavigateBack: () -> Unit,
    viewModel: LiveWatchViewModel = koinViewModel(parameters = { parametersOf(sessionId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LiveWatchEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    if (state.isNotSupported) {
        LiveNotAvailableScreen(onNavigateBack = onNavigateBack)
    } else {
        LiveWatchScreen(state = state, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveWatchScreen(
    state: LiveWatchState,
    onAction: (LiveWatchAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.comments.size) {
        if (state.comments.isNotEmpty()) {
            scope.launch { listState.animateScrollToItem(state.comments.lastIndex) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onAction(LiveWatchAction.OnNavigateBack) }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Retour", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Outlined.FiberManualRecord, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(10.dp))
                        Text(state.session?.title ?: "Live", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    state.session?.let { session ->
                        Row(
                            modifier = Modifier.padding(end = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                            Text("${session.viewerCount}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        },
        bottomBar = {
            if (!state.sessionEnded) {
                LiveCommentInput(
                    value = state.commentInput,
                    onValueChange = { onAction(LiveWatchAction.OnCommentInputChange(it)) },
                    onSend = { onAction(LiveWatchAction.OnSendComment) },
                    isSending = state.isSendingComment,
                )
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Zone vidéo — LiveKit VideoView
            Box(
                modifier = Modifier.fillMaxWidth().height(240.dp),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    state.isLoading -> {
                        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.inverseSurface), contentAlignment = Alignment.Center) {
                            MyColoLoadingContent()
                        }
                    }
                    state.sessionEnded -> {
                        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.inverseSurface), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Outlined.VideocamOff, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f))
                                Text("Live terminé", color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f), fontSize = 13.sp)
                            }
                        }
                    }
                    else -> {
                        LiveVideoView(
                            videoTrack = state.remoteVideoTrack,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
                // Info hôte en overlay
                state.session?.let { session ->
                    Row(
                        modifier = Modifier.align(Alignment.BottomStart).padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        MyColoUserAvatar(name = session.hostName, size = 28.dp)
                        Text(session.hostName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.inverseOnSurface)
                    }
                }
            }

            // Commentaires
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
            ) {
                items(state.comments, key = { it.id }) { comment ->
                    LiveCommentItem(comment = comment)
                }
            }
        }
    }
}

@Composable
private fun LiveCommentItem(comment: LiveComment, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MyColoUserAvatar(name = comment.authorName, size = 28.dp)
        Column {
            Text(comment.authorName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(comment.content, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun LiveCommentInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isSending: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Commenter…", fontSize = 13.sp) },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                ),
            )
            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank() && !isSending,
                modifier = Modifier.size(40.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
            ) {
                if (isSending) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Outlined.Send, contentDescription = "Envoyer", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
