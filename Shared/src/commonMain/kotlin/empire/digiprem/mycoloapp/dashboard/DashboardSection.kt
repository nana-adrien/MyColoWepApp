package empire.digiprem.mycoloapp.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import empire.digiprem.mycoloapp.core.design_system.components.icon.AppIconResource
import empire.digiprem.mycoloapp.core.design_system.components.scaffold.NavigationItemId
import empire.digiprem.mycoloapp.core.domain.util.UiText

enum class DashboardSection(
    val label: UiText,
    val icon: AppIconResource,
): NavigationItemId {
    Overview(
        label = UiText.DynamicString("Overview"),
        icon = AppIconResource.VectorResource(Icons.Default.Dashboard)
    ),
    Participants(
        label = UiText.DynamicString("Participants"),
        icon = AppIconResource.VectorResource(Icons.Default.Person4)
    ),
    SecurityCodes(
        label = UiText.DynamicString("Security Codes"),
        icon = AppIconResource.VectorResource(Icons.Default.Security)
    ),
    Settings(
        label = UiText.DynamicString("Settings"),
        icon = AppIconResource.VectorResource(Icons.Default.Settings)
    ),
}