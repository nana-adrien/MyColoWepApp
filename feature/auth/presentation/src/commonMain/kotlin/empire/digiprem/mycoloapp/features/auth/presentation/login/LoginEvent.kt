package empire.digiprem.mycoloapp.features.auth.presentation

sealed interface AdminLoginEvent {
    data object OnLoginSuccess : AdminLoginEvent
}
