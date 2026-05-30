package empire.digiprem.mycoloapp.dashboard

sealed interface DashboardEvent {
    object OnInitEvent : DashboardEvent
}