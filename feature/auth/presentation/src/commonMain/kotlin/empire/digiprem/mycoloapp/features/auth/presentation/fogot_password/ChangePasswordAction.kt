package empire.digiprem.mycoloapp.features.auth.presentation.fogot_password

sealed interface ChangePasswordAction {
    data object OnConfirmClick : ChangePasswordAction
    data object OnToggleNewPasswordVisibility : ChangePasswordAction
    data object OnToggleConfirmPasswordVisibility : ChangePasswordAction
    data object OnClearError : ChangePasswordAction
}
