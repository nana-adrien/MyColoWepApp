package empire.digiprem.mycoloapp.navigation.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import empire.digiprem.mycoloapp.features.auth.presentation.fogot_password.ChangePasswordScreen
import empire.digiprem.mycoloapp.features.auth.presentation.reset_password.ResetPasswordScreen
import empire.digiprem.mycoloapp.features.auth.presentation.verify_identity.VerifyOtpScreen


fun NavGraphBuilder.authNavGraph(
     navController: NavHostController,
     onNavigateBack: () -> Unit,
     onPasswordUpdated: () -> Unit,
     onOtpVerified: () -> Unit,
){
     navigation<AuthRoute.AuthRoot>(startDestination = AuthRoute.ResetPassword) {
         composable<AuthRoute.ResetPassword> {
             ResetPasswordScreen(
                 onNavigateBack = onNavigateBack,
                 onNavigateToVerification = { email ->
                     navController.navigate(AuthRoute.VerifyOtp(email)) {
                         launchSingleTop = true
                     }
                 },
             )
         }
         composable<AuthRoute.VerifyOtp> { backStackEntry ->
             val route = backStackEntry.toRoute<AuthRoute.VerifyOtp>()
             VerifyOtpScreen(
                 email = route.email,
                 onNavigateBack = { navController.popBackStack() },
                 onOtpVerified = onOtpVerified,
             )
         }
         composable<AuthRoute.ChangePassword> {
             ChangePasswordScreen(
                 onNavigateBack = { navController.popBackStack() },
                 onPasswordUpdated = onPasswordUpdated,
             )
         }
     }

 }
