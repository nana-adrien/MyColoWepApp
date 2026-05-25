package empire.digiprem.mycoloapp.features.profile.presentation.profile

sealed interface ProfileEvent {
    data object NavigateBack : ProfileEvent
    data object LoggedOut : ProfileEvent
}
