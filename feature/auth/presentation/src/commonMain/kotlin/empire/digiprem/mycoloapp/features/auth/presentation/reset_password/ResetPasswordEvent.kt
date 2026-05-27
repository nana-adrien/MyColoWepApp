package empire.digiprem.mycoloapp.features.auth.presentation.reset_password

sealed interface ResetPasswordEvent {
    data object OnLinkSent : ResetPasswordEvent
    data class OnNavigateToVerification(val email: String) : ResetPasswordEvent
}
