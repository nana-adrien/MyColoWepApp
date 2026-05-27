package empire.digiprem.mycoloapp.features.auth.presentation.fogot_password

sealed interface ChangePasswordEvent {
    data object OnPasswordChanged : ChangePasswordEvent
}
