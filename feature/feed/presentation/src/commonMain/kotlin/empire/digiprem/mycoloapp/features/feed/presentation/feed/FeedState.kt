package empire.digiprem.mycoloapp.features.feed.presentation.feed

import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.features.feed.domain.model.Comment
import empire.digiprem.mycoloapp.features.feed.domain.model.Post

data class FeedState(
    val posts: List<Post> = defaultPosts,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: UiText? = null,
    val selectedPostId: String? = null,
    val comments: List<Comment> = emptyList(),
    val isCommentsLoading: Boolean = false,
    val commentInput: String = "",
    val isAddingComment: Boolean = false,
)
