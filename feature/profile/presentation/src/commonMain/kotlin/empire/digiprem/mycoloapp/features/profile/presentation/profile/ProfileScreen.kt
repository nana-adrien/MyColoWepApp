package empire.digiprem.mycoloapp.features.profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.components.MyColoEmptyContent
import empire.digiprem.mycoloapp.core.design_system.components.MyColoLoadingContent
import empire.digiprem.mycoloapp.core.design_system.components.MyColoUserAvatar
import empire.digiprem.mycoloapp.features.profile.domain.model.UserProfile
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    userId: String,
    onNavigateBack: () -> Unit,
    onLogOut: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        viewModel.onAction(ProfileAction.LoadProfile(userId))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ProfileEvent.NavigateBack -> onNavigateBack()
                ProfileEvent.LoggedOut -> onLogOut()
            }
        }
    }

    ProfileScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mon profil") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ProfileAction.OnNavigateBack) }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(ProfileAction.OnLogOut) }) {
                        Icon(Icons.Outlined.Logout, contentDescription = "Déconnexion", tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        when {
            state.isLoading -> MyColoLoadingContent(modifier = Modifier.padding(paddingValues))
            state.profile != null -> ProfileContent(
                profile = state.profile,
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            )
            else -> MyColoEmptyContent(
                message = "Profil introuvable",
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {

        // ── Header card ─────────────────────────────────────────────────────
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(14.dp)) {

                // Avatar + info horizontal
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MyColoUserAvatar(name = profile.fullName, size = 72.dp, fontSize = 20.sp)

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            profile.fullName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            "Participant",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                        if (profile.bio.isNotBlank()) {
                            Text(
                                profile.bio,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 3.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Stats 2 colonnes
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    ProfileStatBox(
                        value = "${profile.postsCount}",
                        label = "Publications",
                        valueColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                    )
                    ProfileStatBox(
                        value = "${profile.likesReceivedCount}",
                        label = "Likes reçus",
                        valueColor = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Action buttons
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(100.dp),
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Modifier", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(100.dp),
                    ) {
                        Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Partager", fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(2.dp))

        // ── Post grid 3 × 2 ────────────────────────────────────────────────
        ProfilePostGrid(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun ProfileStatBox(
    value: String,
    label: String,
    valueColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = valueColor)
            Text(label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ProfilePostGrid(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        repeat(2) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                repeat(3) { col ->
                    val bgColor = when (row * 3 + col) {
                        1 -> MaterialTheme.colorScheme.tertiaryContainer
                        3 -> MaterialTheme.colorScheme.secondaryContainer
                        5 -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.primaryContainer
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(bgColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.Image,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
            if (row < 1) Spacer(Modifier.height(2.dp))
        }
    }
}
