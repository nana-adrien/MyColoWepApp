package empire.digiprem.mycoloapp.core.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationGraph {

    @Serializable
    data object Landing : NavigationGraph

    @Serializable
    data object Registration : NavigationGraph

    @Serializable
    data class Confirmation(val referenceNumber: String="") : NavigationGraph
    @Serializable
    data  class Error404(val path: String) : NavigationGraph

    @Serializable
    data object AdminLogin : NavigationGraph

    @Serializable
    data object AdminDashboard : NavigationGraph

    @Serializable
    data object AdminSecurityCodes : NavigationGraph

    @Serializable
    data object LiveLobby : NavigationGraph

    @Serializable
    data object LiveBroadcast : NavigationGraph

    @Serializable
    data class LiveWatch(val sessionId: String) : NavigationGraph
}
