package empire.digiprem.mycoloapp.features.participants.presentation

sealed interface AdminDashboardEvent {
    data object OnLogout : AdminDashboardEvent
}
