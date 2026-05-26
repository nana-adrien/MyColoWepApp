package empire.digiprem.mycoloapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import empire.digiprem.mycoloapp.core.navigation.NavigationGraph
import empire.digiprem.mycoloapp.core.design_system.theme.MyColoTheme
import empire.digiprem.mycoloapp.alert.AlertHandlerContainer
import empire.digiprem.mycoloapp.features.confirmation.ConfirmationScreen
import empire.digiprem.mycoloapp.features.registration.presentation.RegistrationScreen
import empire.digiprem.mycoloapp.features.auth.presentation.AdminLoginScreen
import empire.digiprem.mycoloapp.features.participants.presentation.AdminDashboardScreen
import empire.digiprem.mycoloapp.features.pre_auth.presentation.LandingScreen
import empire.digiprem.mycoloapp.features.security_code.presentation.SecurityCodeScreen
import empire.digiprem.mycoloapp.features.live.presentation.lobby.LiveLobbyRoot
import empire.digiprem.mycoloapp.features.live.presentation.broadcast.LiveBroadcastRoot
import empire.digiprem.mycoloapp.features.live.presentation.watch.LiveWatchRoot
import empire.digiprem.mycoloapp.navigation.MyColoNavHost
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    onNavHostReady: suspend (NavHostController) -> Unit = {}
) {
    val appViewModel: AppViewModel = koinViewModel()
    val navController = rememberNavController()
    val state by appViewModel.state.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val height = LocalWindowInfo.current.containerDpSize.height-40.dp
    MyColoTheme {
        // startDestination résolu UNE SEULE FOIS, après chargement complet
        AlertHandlerContainer {
            MyColoNavHost(
                navController = navController,
                authenticated = state.isAuthenticated,
                commonNavGraph = {
                    composable<NavigationGraph.AdminLogin> {
                        Column(
                            modifier = Modifier.heightIn(min=height)
                        ) {
                            AdminLoginScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onLoginSuccess = {
                                    navController.navigate(NavigationGraph.AdminDashboard) {
                                        popUpTo<NavigationGraph.AdminLogin> { inclusive = true }
                                    }
                                }
                            )
                        }
                    }

                    composable<NavigationGraph.AdminDashboard> {
                        key(navBackStackEntry?.id) {
                            Column(
                                modifier = Modifier.height(height).padding(top = 56.dp)
                            ) {
                                AdminDashboardScreen(
                                    onLogout = {
                                        navController.navigate(NavigationGraph.AdminLogin) {
                                            popUpTo(NavigationGraph.Landing) { inclusive = false }
                                        }
                                    }
                                )
                            }

                        }
                    }

                    composable<NavigationGraph.AdminSecurityCodes> {
                        key(navBackStackEntry?.id) {
                            SecurityCodeScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }

                    composable<NavigationGraph.LiveLobby> {
                        key(navBackStackEntry?.id) {
                            LiveLobbyRoot(
                                onNavigateToWatch = { sessionId ->
                                    navController.navigate(NavigationGraph.LiveWatch(sessionId))
                                },
                                onNavigateToBroadcast = {
                                    navController.navigate(NavigationGraph.LiveBroadcast)
                                }
                            )
                        }
                    }

                    composable<NavigationGraph.LiveBroadcast> {
                        key(navBackStackEntry?.id) {
                            LiveBroadcastRoot(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }

                    composable<NavigationGraph.LiveWatch> {
                        key(navBackStackEntry?.id) {
                            val route = it.toRoute<NavigationGraph.LiveWatch>()
                            LiveWatchRoot(
                                sessionId = route.sessionId,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            )
        }

    }

    // Garde réactive : gère les changements d'auth EN COURS de session
    // (ex: session expire pendant l'utilisation, login réussi depuis AdminLogin)
    LaunchedEffect(state.isAuthenticated, state.isLoadingSession) {
        if (state.isLoadingSession) return@LaunchedEffect

        val currentRoute = navController.currentDestination?.route.orEmpty()
        when {
            state.isAuthenticated && currentRoute.contains("AdminLogin") ->
                navController.navigate(NavigationGraph.AdminDashboard) {
                    popUpTo<NavigationGraph.AdminLogin> { inclusive = true }
                }

            !state.isAuthenticated && currentRoute.contains("AdminDashboard") ->
                navController.navigate(NavigationGraph.AdminLogin) {
                    popUpTo<NavigationGraph.AdminDashboard> { inclusive = true }
                }
        }
    }

}
