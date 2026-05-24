package empire.digiprem.mycoloapp.feature.admin.security_code.presentation

sealed interface SecurityCodeAction {
    data object OnGenerateCode : SecurityCodeAction
    data object OnRetryLoad : SecurityCodeAction
    data class OnToggleActive(val id: String, val currentIsActive: Boolean) : SecurityCodeAction
    data object OnDismissError : SecurityCodeAction
    data object OnDismissSuccess : SecurityCodeAction
}
