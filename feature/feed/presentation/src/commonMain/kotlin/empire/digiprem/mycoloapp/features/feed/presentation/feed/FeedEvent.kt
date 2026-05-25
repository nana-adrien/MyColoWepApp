package empire.digiprem.mycoloapp.features.feed.presentation.feed

sealed interface FeedEvent {
    data class NavigateToProfile(val userId: String) : FeedEvent
}
