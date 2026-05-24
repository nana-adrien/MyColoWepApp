package empire.digiprem.mycoloapp.feature.admin.login.presentation

sealed interface AdminLoginEvent {
    data object OnLoginSuccess : AdminLoginEvent
}
