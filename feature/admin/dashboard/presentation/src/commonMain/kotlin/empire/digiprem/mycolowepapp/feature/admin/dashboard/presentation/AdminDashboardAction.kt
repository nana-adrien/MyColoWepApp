package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

sealed interface AdminDashboardAction {
    data class OnSearchChange(val query: String) : AdminDashboardAction
    data object OnExportPdf : AdminDashboardAction
    data object OnExportExcel : AdminDashboardAction
    data object OnLogoutClick : AdminDashboardAction
}
