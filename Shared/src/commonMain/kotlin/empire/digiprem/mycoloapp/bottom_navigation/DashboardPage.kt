package empire.digiprem.mycoloapp.bottom_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import empire.digiprem.mycoloapp.core.design_system.components.icon.AppIconResource
import empire.digiprem.mycoloapp.core.domain.model.UserRole
import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.features.feed.presentation.createpost.CreatePostRoot
import empire.digiprem.mycoloapp.features.feed.presentation.feed.FeedRoot
import empire.digiprem.mycoloapp.features.live.presentation.lobby.LiveLobbyRoot
import empire.digiprem.mycoloapp.features.profile.presentation.profile.ProfileRoot
import empire.digiprem.mycoloapp.home.IAppBottomBarNavItem

enum class BottomBarNavItem: IAppBottomBarNavItem {
    Feed {
        override val label get() = UiText.DynamicString("Feed")
        override val icon  get() = AppIconResource.VectorResource(Icons.Outlined.DynamicFeed)
    },
    Live {
        override val label get() = UiText.DynamicString("Live")
        override val icon  get() = AppIconResource.VectorResource(Icons.Outlined.LiveTv)
    },
    CreatePost {
        override val label get() = UiText.DynamicString("Post")
        override val icon  get() = AppIconResource.VectorResource(Icons.Outlined.AddBox)
    },
    Profile {
        override val label get() = UiText.DynamicString("Profil")
        override val icon  get() = AppIconResource.VectorResource(Icons.Outlined.Person)
    },
}
data class DashboardPage(
    val navIndicator: BottomBarNavItem,
    val page: @Composable () -> Unit,
)



fun UserRole.dashboardPages(
    onNavigateToProfile:   (String) -> Unit,
    onNavigateToLobby:     () -> Unit,
    onNavigateToBroadcast: () -> Unit,
    onNavigateToWatch:     (String) -> Unit,
    onNavigateToCreate:    () -> Unit,
): List<DashboardPage> = listOf(
    DashboardPage(
        navIndicator = BottomBarNavItem.Feed,
        page         = {
            FeedRoot(onNavigateToProfile = onNavigateToProfile)
        },
    ),
    DashboardPage(
        navIndicator = BottomBarNavItem.Live,
        page         = {
            LiveLobbyRoot(
                onNavigateToWatch     = onNavigateToWatch,
                onNavigateToBroadcast = onNavigateToBroadcast,
            )
        },
    ),
    DashboardPage(
        navIndicator = BottomBarNavItem.CreatePost,
        page         = {
            CreatePostRoot(onPublished = {})
        },
    ),
    DashboardPage(
        navIndicator = BottomBarNavItem.Profile,
        page         = {
            ProfileRoot(
                userId         = "me",
                onNavigateBack = {},
                onLogOut       = {},
            )
        },
    ),
   /* DashboardPage(
        navIndicator = BottomBarNavItem.Notifications,
        page         = {
            NotificationsRoot()
        },
    ),*/
)