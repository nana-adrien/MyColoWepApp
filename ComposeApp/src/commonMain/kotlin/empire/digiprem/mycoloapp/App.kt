package empire.digiprem.mycoloapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    onNavHostReady: suspend (NavHostController) -> Unit = {}
) {
    val appViewModel: AppViewModel = koinViewModel()
    val navController = rememberNavController()
    val state by appViewModel.state.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()


    MyColoTheme {
        // startDestination résolu UNE SEULE FOIS, après chargement complet
        AlertHandlerContainer {
            NavHost(
                navController = navController,
                startDestination = NavigationGraph.Landing,
                modifier = Modifier.fillMaxSize()
            ) {
                composable<NavigationGraph.Landing> {
                    key(navBackStackEntry?.id) {
                        LandingScreen(
                            onNavigateToRegistration = {
                                navController.navigate(NavigationGraph.Registration)
                            },
                            onNavigateToAdmin = {
                                navController.navigate(
                                    if (state.isAuthenticated) NavigationGraph.AdminDashboard else
                                        NavigationGraph.AdminLogin
                                )
                            }
                        )
                    }
                }

                composable<NavigationGraph.Registration> {
                    key(navBackStackEntry?.id) {

                        RegistrationScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToConfirmation = { ref ->
                                navController.navigate(NavigationGraph.Confirmation(ref)) {
                                    popUpTo<NavigationGraph.Registration> { inclusive = true }
                                }
                            }
                        )
                    }
                }

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

                composable<NavigationGraph.AdminLogin> {
                    AdminLoginScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onLoginSuccess = {
                            navController.navigate(NavigationGraph.AdminDashboard) {
                                popUpTo<NavigationGraph.AdminLogin> { inclusive = true }
                            }
                        }
                    )
                }

                composable<NavigationGraph.AdminDashboard> {
                    key(navBackStackEntry?.id) {

                        AdminDashboardScreen(
                            onLogout = {
                                navController.navigate(NavigationGraph.AdminLogin) {
                                    popUpTo(NavigationGraph.Landing) { inclusive = false }
                                }
                            }
                        )
                    }
                }

                composable<NavigationGraph.AdminSecurityCodes> {
                    key(navBackStackEntry?.id) {

                        SecurityCodeScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }

                composable<NavigationGraph.Error404> {
                    key(navBackStackEntry?.id) {

                        Box(modifier = Modifier.fillMaxSize().background(Color.Red))
                    }
                }
            }

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
    LaunchedEffect(navController) {
        // 1. URL routing (browser hash → écran)
        onNavHostReady(navController)
    }

}
