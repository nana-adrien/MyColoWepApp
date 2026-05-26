package empire.digiprem.mycoloapp.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import empire.digiprem.mycoloapp.core.design_system.WebPageScaffold
import empire.digiprem.mycoloapp.core.design_system.components.MyColoFooter
import empire.digiprem.mycoloapp.core.design_system.components.MyColoNavBar
import empire.digiprem.mycoloapp.core.design_system.components.NavItem
import empire.digiprem.mycoloapp.core.design_system.theme.Primary
import empire.digiprem.mycoloapp.core.navigation.NavigationGraph
import empire.digiprem.mycoloapp.features.auth.presentation.AdminLoginScreen
import empire.digiprem.mycoloapp.features.confirmation.ConfirmationScreen
import empire.digiprem.mycoloapp.features.participants.presentation.AdminDashboardScreen
import empire.digiprem.mycoloapp.features.pre_auth.presentation.LandingScreen
import empire.digiprem.mycoloapp.features.registration.presentation.RegistrationScreen
import empire.digiprem.mycoloapp.features.security_code.presentation.SecurityCodeScreen
import empire.digiprem.mycoloapp.navigation.web_site.WebSiteNavGraphRoute
import empire.digiprem.mycoloapp.navigation.web_site.webSiteNavGraph
import empire.digiprem.mycoloapp.onNavHostReady
import kotlinx.coroutines.launch
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.stringResource
import web.cssom.ContainerType.Companion.scrollState

@Composable
actual fun MyColoNavHost(
    authenticated: Boolean,
    navController: NavHostController,
    commonNavGraph: NavGraphBuilder.() -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // ✅ Index de l'item actuel
    val currentIndex = listOf(
        NavItem("Home", "Home", WebSiteNavGraphRoute.LandingPage()),
        NavItem("contact", "setting"),
    ).indexOfFirst { item ->
        item.route?.let {route->
            currentDestination?.hierarchy?.any {
                it.hasRoute(route::class)
            } == true
        } ?:false

    }

    // ✅ Index précédent pour savoir la direction
    var previousIndex by remember { mutableIntStateOf(currentIndex) }
    val goingRight = currentIndex >= previousIndex

    LaunchedEffect(currentIndex) {
        previousIndex = currentIndex
    }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val navItems = listOf(
        NavItem("Home", "Home", WebSiteNavGraphRoute.LandingPage()),
        NavItem("contact", "setting"),
    )
    WebPageScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        scrollState = scrollState,
        header = {
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    if (goingRight) {
                        // ← vers la droite
                         fadeIn() togetherWith
                                  fadeOut()
                    } else {
                        // → vers la gauche
                        fadeIn() togetherWith
                                  fadeOut()
                    }
                }
            ){ _ ->
                MyColoNavBar(
                    //containerColor = MaterialTheme.colorScheme.surface,
                    //contentColor = MaterialTheme.colorScheme.onSurface,
                    scrollState = scrollState,
                    navItems = navItems,
                    onNavItemClick = { item ->
                        coroutineScope.launch {
                            when (item.id) {
                                "program" -> scrollState.animateScrollToItem(1)
                                "contact" -> scrollState.animateScrollToItem(scrollState.layoutInfo.totalItemsCount - 1)
                            }
                        }
                    },
                    onAdminClick = {
                        navController.navigate(
                            if (authenticated) NavigationGraph.AdminDashboard else
                                NavigationGraph.AdminLogin
                        )
                    }
                )
            }
        },
        footer = { MyColoFooter( stringResource(Res.string.powered_by)) },
        floatingButton = {
            AnimatedVisibility(
                modifier = Modifier.padding(end = 16.dp, bottom = 24.dp),
                visible = scrollState.firstVisibleItemIndex > 0,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally(),
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("↑", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        item {
            NavHost(
                navController = navController,
                startDestination = WebSiteNavGraphRoute.WebSiteRoute,
                modifier = Modifier.fillMaxSize()
            ) {
                webSiteNavGraph(
                    navController = navController,
                    onLoginButtonClick = {
                        navController.navigate(
                            if (authenticated) NavigationGraph.AdminDashboard else
                                NavigationGraph.AdminLogin
                        )
                    }
                )
                composable<NavigationGraph.Confirmation> {
                    key(navBackStackEntry?.id) {
                        val route = it.toRoute<NavigationGraph.Confirmation>()
                        ConfirmationScreen(
                            referenceNumber = route.referenceNumber,
                            onNavigateHome = {
                                navController.navigate(NavigationGraph.Landing) {
                                    popUpTo<NavigationGraph.Landing> { inclusive = true }
                                }
                            }
                        )

                    }
                }
                commonNavGraph()
            }

            LaunchedEffect(navController) {
                // 1. URL routing (browser hash → écran)
                onNavHostReady(navController)
            }
        }


    }
}