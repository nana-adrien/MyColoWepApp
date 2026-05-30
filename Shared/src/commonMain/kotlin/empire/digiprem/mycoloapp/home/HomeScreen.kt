package empire.digiprem.mycoloapp.home


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.bottom_navigation.BottomBarNavItem
import empire.digiprem.mycoloapp.bottom_navigation.DashboardBottomBar
import empire.digiprem.mycoloapp.bottom_navigation.DashboardPage
import empire.digiprem.mycoloapp.core.design_system.components.icon.AppIconResource
import empire.digiprem.mycoloapp.core.domain.util.Platform
import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.core.domain.util.getCurrentPlatform
import empire.digiprem.mycoloapp.features.feed.presentation.createpost.CreatePostRoot
import empire.digiprem.mycoloapp.features.feed.presentation.feed.FeedRoot
import empire.digiprem.mycoloapp.features.live.presentation.lobby.LiveLobbyRoot
import empire.digiprem.mycoloapp.features.profile.presentation.profile.ProfileRoot
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeRoot(
    onNavigateToLiveBroadcast: () -> Unit={},
    onNavigateToLiveWatch: (String) -> Unit={},
    onNavigateToCreatePost: () -> Unit={},
    onNavigateToProfile: (String) -> Unit={},
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    val pages = remember<List<DashboardPage>> {
        listOf(
            DashboardPage(
                navIndicator = BottomBarNavItem.Feed,
                page = {
                    FeedRoot(onNavigateToProfile = onNavigateToProfile)
                },
            ),
            DashboardPage(
                navIndicator = BottomBarNavItem.Live,
                page = {
                    LiveLobbyRoot(
                        onNavigateToWatch = onNavigateToLiveWatch,
                        onNavigateToBroadcast = onNavigateToLiveBroadcast,
                    )
                },
            ),
            DashboardPage(
                navIndicator = BottomBarNavItem.CreatePost,
                page = {
                    CreatePostRoot(onPublished = {})
                },
            ),
            DashboardPage(
                navIndicator = BottomBarNavItem.Profile,
                page = {
                    ProfileRoot(
                        userId = "me",
                        onNavigateBack = {},
                        onLogOut = {},
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
    }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }


    HomeScreen(
        state = state,
        onAction = onAction,
        pagerState = pagerState,
        pages = pages,
    )
}


@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    pagerState: PagerState,
    pages: List<DashboardPage>
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = Color.Transparent, //MaterialTheme.colorScheme.surface,
        bottomBar = {
            if (getCurrentPlatform() == Platform.ANDROID) {
                DashboardBottomBar(
                    modifier = Modifier.shadow(
                        elevation = 20.dp,
                    ),
                    selectedItem = pages[pagerState.currentPage].navIndicator,
                    items = pages.map { it.navIndicator },
                    onItemSelected = { selectPage ->
                        scope.launch {
                            pagerState.animateScrollToPage(
                                pages.withIndex().first { it.value.navIndicator==selectPage }.index
                            )
                        }
                        // onAction(DashBoardAction.OnChangePage(pages.map { it.navIndicator }.indexOf(selectPage)))
                    },
                )
            }
        },
    ) {
        Box(Modifier.fillMaxSize().padding(it)) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState
            ) { currentPage ->
                pages[currentPage].page()
            }
            Box(
                Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (getCurrentPlatform() == Platform.IOS) {
                    DashboardBottomBar(
                        modifier = Modifier.shadow(
                            elevation = 20.dp,
                        ),
                        selectedItem = pages[pagerState.currentPage].navIndicator,
                        items = pages.map { it.navIndicator },
                        onItemSelected = { selectPage ->
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    pages.withIndex().first { it.value.navIndicator==selectPage }.index
                                )
                            }
                           // onAction(DashBoardAction.OnChangePage(pages.map { it.navIndicator }.indexOf(selectPage)))
                        },
                    )
                }
            }

        }

    }

}

interface IAppBottomBarNavItem {
    val label: UiText
    val icon: AppIconResource
}
