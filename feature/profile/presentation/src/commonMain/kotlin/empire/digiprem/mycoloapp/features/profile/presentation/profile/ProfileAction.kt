package empire.digiprem.mycoloapp.features.profile.presentation.profile

sealed interface ProfileAction {
    data class LoadProfile(val userId: String) : ProfileAction
    data object OnNavigateBack : ProfileAction
    data object OnLogOut : ProfileAction
}
