package empire.digiprem.mycoloapp.feature.admin.dashboard.presentation

sealed interface AdminDashboardEvent {
    data object OnLogout : AdminDashboardEvent
}
