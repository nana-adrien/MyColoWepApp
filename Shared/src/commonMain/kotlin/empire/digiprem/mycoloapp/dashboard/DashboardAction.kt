package empire.digiprem.mycoloapp.dashboard

import empire.digiprem.mycoloapp.core.design_system.components.scaffold.NavigationItemId

sealed interface DashboardAction {
    data object OnInitAction : DashboardAction
    data class OnSectionChange(val section: NavigationItemId) : DashboardAction
}