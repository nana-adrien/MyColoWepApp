package empire.digiprem.mycoloapp.features.feed.presentation.feed

sealed interface FeedAction {
    data object OnRefresh : FeedAction
    data class OnLikeClick(val postId: String, val isLiked: Boolean) : FeedAction
    data class OnProfileClick(val userId: String) : FeedAction
    data class OnCommentClick(val postId: String) : FeedAction
    data object OnDismissComments : FeedAction
    data class OnCommentInputChange(val text: String) : FeedAction
    data object OnAddComment : FeedAction
}
