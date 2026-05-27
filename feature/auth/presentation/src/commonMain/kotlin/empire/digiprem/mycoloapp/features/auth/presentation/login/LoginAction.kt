package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface AdminLoginAction {
    data class OnEmailChange(val value: String) : AdminLoginAction
    data class OnPasswordChange(val value: String) : AdminLoginAction
    data object OnTogglePasswordVisibility : AdminLoginAction
    data object OnLoginClick : AdminLoginAction
    data object OnLeanErrorMessageClick : AdminLoginAction
    data object OnDismissErrorDialog : AdminLoginAction
}
