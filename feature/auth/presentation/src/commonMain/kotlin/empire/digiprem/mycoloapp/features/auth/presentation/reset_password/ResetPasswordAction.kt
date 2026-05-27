package empire.digiprem.mycoloapp.features.auth.presentation.reset_password

sealed interface ResetPasswordAction {
    data object OnSendLinkClick : ResetPasswordAction
    data object OnClearError : ResetPasswordAction
    data object OnNavigateToVerification : ResetPasswordAction
}
