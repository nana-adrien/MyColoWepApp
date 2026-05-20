package empire.digiprem.mycolowepapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.compose.ui.window.ComposeViewportConfiguration
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.bindToBrowserNavigation
import empire.digiprem.mycolowepapp.core.navigation.NavigationGraph
import kotlinx.browser.document
import kotlinx.browser.window


@OptIn(ExperimentalBrowserHistoryApi::class)
actual suspend fun onNavHostReady(navController: NavController) {
// 1. Extraction propre de la route initiale (nettoyage des prefixes # et /)
    val currentHash = window.location.hash // ex: "#/registration?section=home" ou "#/"
    val initRoute = when {
        currentHash.isNotEmpty() -> currentHash.removePrefix("#").substringBefore("?")
        else -> window.location.pathname.substringAfter("/", "")
    }.removePrefix("/").trim()

// 2. Navigation initiale
    when (initRoute) {
        "", "landing" -> {
            navController.navigate(NavigationGraph.Landing)
        }
        "registration" -> {
            navController.navigate(NavigationGraph.Registration) // Remplace par ton objet d'écran réel
        }
        "confirmation" -> {
            navController.navigate(NavigationGraph.Confirmation)
        }
        "admin/login" -> {
            navController.navigate(NavigationGraph.AdminLogin)
        }
        "admin/dashboard" -> {
            navController.navigate(NavigationGraph.AdminDashboard)
        }
        else -> {
            // Si la route n'est pas reconnue, 404
            navController.navigate(NavigationGraph.Error404(path = initRoute))
        }
    }

// 3. Synchronisation propre avec la barre d'adresse du navigateur
    navController.bindToBrowserNavigation { entry ->
        val route = entry.destination.route.orEmpty()
        when {
            route.contains("Landing") -> "#/"
            route.contains("Registration") -> "#/registration"
            route.contains("Confirmation") -> "#/confirmation"
            route.contains("AdminLogin") -> "#/admin/login"
            route.contains("AdminDashboard") -> "#/admin/dashboard"
            else -> ""
        }
    }
}

@OptIn(markerClass = [ExperimentalComposeUiApi::class])
actual fun PlatformComposeViewport(
    configure: ComposeViewportConfiguration.() -> Unit,
    content: @Composable (() -> Unit)
) {
    val body=document.body?:return
    // On cherche l'élément où Compose s'affiche (souvent d'ID "root")
    val root = document.getElementById("root")

    // On force l'autorisation du clic droit sur cet élément
    root?.addEventListener("contextmenu", { event ->
        // On NE FAIT PAS event.preventDefault() ici.
        // On laisse l'événement remonter au navigateur.
        event.stopPropagation()
    }, true)

    ComposeViewport (
        viewportContainerId = "root",
        configure = configure,
        content=content
    )
}