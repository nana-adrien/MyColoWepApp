package empire.digiprem.mycoloapp.features.feed.presentation.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.components.MyColoBrandCompact
import empire.digiprem.mycoloapp.core.design_system.components.MyColoEmptyContent
import empire.digiprem.mycoloapp.core.design_system.components.MyColoLoadingContent
import empire.digiprem.mycoloapp.features.feed.presentation.component.CommentsBottomSheet
import empire.digiprem.mycoloapp.features.feed.presentation.component.PostCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoot(
    onNavigateToProfile: (String) -> Unit,
    viewModel: FeedViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is FeedEvent.NavigateToProfile -> onNavigateToProfile(event.userId)
            }
        }
    }

    FeedScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    state: FeedState,
    onAction: (FeedAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.selectedPostId != null) {
        val selectedPost = state.posts.find { it.id == state.selectedPostId }
        CommentsBottomSheet(
            comments = state.comments,
            commentInput = state.commentInput,
            isLoading = state.isCommentsLoading,
            isAddingComment = state.isAddingComment,
            commentsCount = selectedPost?.commentsCount ?: 0,
            onCommentInputChange = { onAction(FeedAction.OnCommentInputChange(it)) },
            onAddComment = { onAction(FeedAction.OnAddComment) },
            onDismiss = { onAction(FeedAction.OnDismissComments) },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { MyColoBrandCompact() },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Search, contentDescription = "Rechercher", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },

        modifier = modifier,
    ) { paddingValues ->
        when {
            state.isLoading -> MyColoLoadingContent(modifier = Modifier.padding(paddingValues))

            state.posts.isEmpty() -> MyColoEmptyContent(
                message = "Aucune publication pour le moment",
                modifier = Modifier.padding(paddingValues),
            )

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(state.posts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onLikeClick = { onAction(FeedAction.OnLikeClick(post.id, post.isLikedByMe)) },
                        onProfileClick = { onAction(FeedAction.OnProfileClick(post.authorId)) },
                        onCommentClick = { onAction(FeedAction.OnCommentClick(post.id)) },
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}
