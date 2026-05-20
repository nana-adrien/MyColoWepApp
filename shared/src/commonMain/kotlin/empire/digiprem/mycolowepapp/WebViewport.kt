package empire.digiprem.mycolowepapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.compose.ui.window .ComposeViewportConfiguration
@OptIn(ExperimentalComposeUiApi::class)
expect fun PlatformComposeViewport( configure: ComposeViewportConfiguration.() -> Unit = {},content: @Composable () -> Unit)

expect suspend fun onNavHostReady(navController: NavController)
