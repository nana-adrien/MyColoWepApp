package empire.digiprem.mycoloapp.features.live.presentation.watch

sealed interface LiveWatchAction {
    data class OnCommentInputChange(val text: String) : LiveWatchAction
    data object OnSendComment : LiveWatchAction
    data object OnNavigateBack : LiveWatchAction
}
