package empire.digiprem.mycolowepapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import empire.digiprem.mycolowepapp.core.navigation.NavigationGraph
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
actual fun PlatformComposeViewport(content: @Composable () -> Unit) {
    val root = document.getElementById("root")
    root?.addEventListener("contextmenu", { event ->
        event.stopPropagation()
    }, true)
    ComposeViewport(viewportContainerId = "root", content = content)
}

@OptIn(ExperimentalBrowserHistoryApi::class)
actual suspend fun onNavHostReady(navController: NavController) {
    val hash = window.location.hash.removePrefix("#").removePrefix("/").substringBefore("?")

    when {
        hash.isEmpty() || hash == "landing" -> { /* Landing est le startDestination — rien à faire */ }
        hash.startsWith("registration")    -> navController.navigate(NavigationGraph.Registration)
        hash.startsWith("admin/login")     -> navController.navigate(NavigationGraph.AdminLogin)
        hash.startsWith("admin/dashboard") -> navController.navigate(NavigationGraph.AdminLogin)
        hash.startsWith("confirmation")    -> { /* Nécessite un referenceNumber — redirige vers Landing */ }
        else -> navController.navigate(NavigationGraph.Error404(path = hash))
    }

    navController.bindToBrowserNavigation { entry ->
        val route = entry.destination.route.orEmpty()
        when {
            route.contains("NavigationGraph.Landing") ->
                "#/"
            route.contains("NavigationGraph.Registration") ->
                "#/registration"
            route.contains("NavigationGraph.Confirmation") ->
                "#/confirmation/${entry.toRoute<NavigationGraph.Confirmation>().referenceNumber}"
            route.contains("NavigationGraph.AdminLogin") ->
                "#/admin/login"
            route.contains("NavigationGraph.AdminDashboard") ->
                "#/admin/dashboard"
            route.contains("NavigationGraph.Error404") ->
                "#/${entry.toRoute<NavigationGraph.Error404>().path}"
            else -> ""
        }
    }
}
