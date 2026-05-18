package empire.digiprem.mycolowepapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController

@OptIn(ExperimentalComposeUiApi::class)
expect fun PlatformComposeViewport(content: @Composable () -> Unit)

expect suspend fun onNavHostReady(navController: NavController)
