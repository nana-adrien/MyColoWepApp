package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

sealed interface AdminDashboardEvent {
    data object OnLogout : AdminDashboardEvent
}
