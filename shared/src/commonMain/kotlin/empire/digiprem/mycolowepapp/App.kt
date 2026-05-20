package empire.digiprem.mycolowepapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import empire.digiprem.mycolowepapp.core.navigation.NavigationGraph
import empire.digiprem.mycolowepapp.core.theme.MyColoTheme
import empire.digiprem.mycolowepapp.di.appModule
import empire.digiprem.mycolowepapp.di.supabaseModule
import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.di.dashboardDataModule
import empire.digiprem.mycolowepapp.feature.admin.login.data.di.adminLoginDataModule
import empire.digiprem.mycolowepapp.feature.admin.security_code.data.di.securityCodeDataModule
import empire.digiprem.mycolowepapp.feature.registration.data.di.registrationDataModule
import empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation.AdminDashboardScreen
import empire.digiprem.mycolowepapp.feature.admin.login.presentation.AdminLoginScreen
import empire.digiprem.mycolowepapp.feature.admin.security_code.presentation.SecurityCodeScreen
import empire.digiprem.mycolowepapp.feature.confirmation.ConfirmationScreen
import empire.digiprem.mycolowepapp.feature.landing.LandingScreen
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationScreen
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.first
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(
            supabaseModule,
            adminLoginDataModule,
            dashboardDataModule,
            registrationDataModule,
            securityCodeDataModule,
            appModule
        )
    }) {
        val appViewModel: AppViewModel = koinViewModel()
        val navController = rememberNavController()
        val state by appViewModel.state.collectAsState()
        val navBackStackEntry by navController.currentBackStackEntryAsState()


        MyColoTheme {
            // startDestination résolu UNE SEULE FOIS, après chargement complet
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
}
