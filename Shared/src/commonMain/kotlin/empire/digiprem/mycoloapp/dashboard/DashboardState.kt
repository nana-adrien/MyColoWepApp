package empire.digiprem.mycoloapp.dashboard

import empire.digiprem.mycoloapp.core.design_system.components.scaffold.NavigationItemId
import empire.digiprem.mycoloapp.core.domain.model.UserRole

data class DashboardState(
    val userRole: UserRole = UserRole.MEMBER,
    val currentSection: NavigationItemId = DashboardSection.Overview,
)