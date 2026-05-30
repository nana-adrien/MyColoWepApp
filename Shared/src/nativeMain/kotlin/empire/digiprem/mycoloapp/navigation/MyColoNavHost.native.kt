package empire.digiprem.mycoloapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import empire.digiprem.mycoloapp.core.navigation.NavigationGraph

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