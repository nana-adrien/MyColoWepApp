package empire.digiprem.mycoloapp.features.feed.presentation.createpost

sealed interface CreatePostEvent {
    data object OnPublished : CreatePostEvent
    data object OnNavigateBack : CreatePostEvent
}
