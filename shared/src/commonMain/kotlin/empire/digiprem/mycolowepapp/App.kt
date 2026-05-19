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

@Composable
fun App() {
    KoinApplication(application = {
        modules(supabaseModule, adminLoginDataModule, dashboardDataModule, registrationDataModule, securityCodeDataModule, appModule)
    }) {
        MyColoTheme {
            val navController = rememberNavController()
            val supabaseClient: SupabaseClient = koinInject()

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
                                popUpTo(NavigationGraph.Landing) { inclusive = false }
                            }
                        }
                    )
                }

                composable<NavigationGraph.AdminSecurityCodes> {
                    SecurityCodeScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable<NavigationGraph.Error404> {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Red))
                }
            }

            LaunchedEffect(navController) {
                // 1. URL routing (browser hash → écran)
                onNavHostReady(navController)

                // 2. Attendre que Supabase termine le chargement de la session
                //    depuis le localStorage (évite le faux "pas de session" au refresh)
                supabaseClient.auth.sessionStatus
                    .first { it !is SessionStatus.Authenticated /*LoadingFromStorage*/ }

                // 3. Guards d'authentification (après que la session soit résolue)
                val hasSession = runCatching {
                    supabaseClient.auth.currentSessionOrNull() != null
                }.getOrDefault(false)

                val currentRoute = navController.currentDestination?.route.orEmpty()
                when {
                    hasSession && currentRoute.contains("AdminLogin") ->
                        navController.navigate(NavigationGraph.AdminDashboard) {
                            popUpTo<NavigationGraph.AdminLogin> { inclusive = true }
                        }
                    !hasSession && currentRoute.contains("AdminDashboard") ->
                        navController.navigate(NavigationGraph.AdminLogin) {
                            popUpTo<NavigationGraph.AdminDashboard> { inclusive = true }
                        }
                }
            }
        }
    }
}
