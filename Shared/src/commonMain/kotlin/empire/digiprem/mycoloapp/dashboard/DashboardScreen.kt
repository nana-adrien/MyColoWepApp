package empire.digiprem.mycoloapp.dashboard


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.components.scaffold.DashBoardScaffold
import empire.digiprem.mycoloapp.core.design_system.components.scaffold.NavigationItem
import empire.digiprem.mycoloapp.features.participants.presentation.statistics.ParticipantStatisticsRoot
import empire.digiprem.mycoloapp.home.HomeRoot
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DashboardRoot(
    viewModel: DashboardViewModel = koinViewModel(),
    onLogout: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    DashboardScreen(
        state = state,
        onLogout = onLogout,
        onAction = onAction
    )
}


@Composable
fun DashboardScreen(
    state: DashboardState,
    onLogout: () -> Unit,
    onAction: (DashboardAction) -> Unit
) {
    DashBoardScaffold(
        selectedNavigationItemId = state.currentSection,
        navigations = DashboardSection.entries.map {
            NavigationItem(
                id = it,
                label = it.label,
                icon = it.icon,
            )
        },
        onNavigate = {
            onAction(DashboardAction.OnSectionChange(it ))
        }
    ) {
        when (state.currentSection) {
            DashboardSection.Overview -> HomeRoot()
            DashboardSection.Participants -> ParticipantStatisticsRoot()
           // DashboardSection.SecurityCodes -> SecurityCodesPage()
           // DashboardSection.Settings -> SettingsPage(state = state, onAction = onAction)
        }
    }
}


@Composable
private fun DashboardPreview() {
    DashboardScreen(
        state = DashboardState(),
        onLogout = {},
        onAction = {

        }
    )
}
