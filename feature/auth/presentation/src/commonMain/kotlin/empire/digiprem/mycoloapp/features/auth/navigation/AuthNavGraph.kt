package empire.digiprem.mycoloapp.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

expect fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onPasswordUpdated: () -> Unit,
    onOtpVerified: () -> Unit,
)
