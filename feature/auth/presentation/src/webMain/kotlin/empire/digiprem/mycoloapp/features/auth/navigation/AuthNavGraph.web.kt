package empire.digiprem.mycoloapp.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import empire.digiprem.mycoloapp.features.auth.presentation.ModifierMotDePasseWebScreen
import empire.digiprem.mycoloapp.features.auth.presentation.ReinitialiserMotDePasseWebScreen
import empire.digiprem.mycoloapp.features.auth.presentation.VerifierMotDePasseWebScreen

actual fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onPasswordUpdated: () -> Unit,
    onOtpVerified: () -> Unit,
) {
    navigation<AuthRoute.AuthRoot>(startDestination = AuthRoute.ReinitialiserMotDePasse) {
        composable<AuthRoute.ReinitialiserMotDePasse> {
            ReinitialiserMotDePasseWebScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToVerification = { email ->
                    navController.navigate(AuthRoute.VerifierMotDePasse(email)) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<AuthRoute.VerifierMotDePasse> { backStackEntry ->
            val route = backStackEntry.toRoute<AuthRoute.VerifierMotDePasse>()
            VerifierMotDePasseWebScreen(
                email = route.email,
                onNavigateBack = { navController.popBackStack() },
                onOtpVerified = onOtpVerified,
            )
        }
        composable<AuthRoute.ModifierMotDePasse> {
            ModifierMotDePasseWebScreen(
                onNavigateBack = { navController.popBackStack() },
                onPasswordUpdated = onPasswordUpdated,
            )
        }
    }
}
