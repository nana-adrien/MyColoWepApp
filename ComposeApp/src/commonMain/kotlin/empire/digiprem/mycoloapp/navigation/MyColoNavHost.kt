package empire.digiprem.mycoloapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

@Composable
expect fun  MyColoNavHost(
    authenticated: Boolean=false, navController: NavHostController,
    commonNavGraph: NavGraphBuilder.() -> Unit
)