package empire.digiprem.mycoloapp.features.live.presentation.watch

sealed interface LiveWatchEvent {
    data object NavigateBack : LiveWatchEvent
}
