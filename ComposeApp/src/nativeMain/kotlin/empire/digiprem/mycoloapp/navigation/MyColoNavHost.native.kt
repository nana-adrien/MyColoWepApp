package empire.digiprem.mycoloapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import empire.digiprem.mycoloapp.core.navigation.NavigationGraph
import empire.digiprem.mycoloapp.features.auth.presentation.AdminLoginScreen
import empire.digiprem.mycoloapp.features.confirmation.ConfirmationScreen
import empire.digiprem.mycoloapp.features.participants.presentation.AdminDashboardScreen
import empire.digiprem.mycoloapp.features.pre_auth.presentation.LandingScreen
import empire.digiprem.mycoloapp.features.registration.presentation.RegistrationScreen
import empire.digiprem.mycoloapp.features.security_code.presentation.SecurityCodeScreen

@androidx.compose.runtime.Composable
actual fun MyColoNavHost(
    authenticated: Boolean,
    navController: androidx.navigation.NavHostController,
    commonNavGraph: NavGraphBuilder.() -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    NavHost(
        navController = navController,
        startDestination = NavigationGraph.AdminLogin,
        modifier = Modifier.fillMaxSize()
    ) {
        commonNavGraph()
    }
}