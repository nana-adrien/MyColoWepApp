package empire.digiprem.mycolowepapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import empire.digiprem.mycolowepapp.core.navigation.NavigationGraph
import empire.digiprem.mycolowepapp.core.theme.MyColoTheme
import empire.digiprem.mycolowepapp.di.appModule
import empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation.AdminDashboardScreen
import empire.digiprem.mycolowepapp.feature.admin.login.presentation.AdminLoginScreen
import empire.digiprem.mycolowepapp.feature.confirmation.ConfirmationScreen
import empire.digiprem.mycolowepapp.feature.landing.LandingScreen
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationScreen
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        MyColoTheme {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = NavigationGraph.Landing,
                modifier = Modifier.fillMaxSize()
            ) {
                composable<NavigationGraph.Landing> {
                    LandingScreen(
                        onNavigateToRegistration = {
                            navController.navigate(NavigationGraph.Registration)
                        },
                        onNavigateToAdmin = {
                            navController.navigate(NavigationGraph.AdminLogin)
                        }
                    )
                }

                composable<NavigationGraph.Registration> {
                    RegistrationScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToConfirmation = { ref ->
                            navController.navigate(NavigationGraph.Confirmation(ref)) {
                                popUpTo<NavigationGraph.Registration> { inclusive = true }
                            }
                        }
                    )
                }

                composable<NavigationGraph.Confirmation> {
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
                    AdminDashboardScreen(
                        onLogout = {
                            navController.navigate(NavigationGraph.AdminLogin) {
                                popUpTo<NavigationGraph.AdminDashboard> { inclusive = true }
                            }
                        }
                    )
                }
                composable<NavigationGraph.Error404> {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Red),
                    )
                }
            }

            LaunchedEffect(navController) {
                onNavHostReady(navController)
            }
        }
    }
}
