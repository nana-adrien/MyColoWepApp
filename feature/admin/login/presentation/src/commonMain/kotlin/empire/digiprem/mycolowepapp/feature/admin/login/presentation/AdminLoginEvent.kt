package empire.digiprem.mycolowepapp.feature.admin.login.presentation

sealed interface AdminLoginEvent {
    data object OnLoginSuccess : AdminLoginEvent
}
