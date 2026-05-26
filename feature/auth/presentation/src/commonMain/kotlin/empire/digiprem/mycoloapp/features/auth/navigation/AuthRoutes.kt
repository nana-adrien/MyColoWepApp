package empire.digiprem.mycoloapp.features.auth.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRoute {
    @Serializable data object AuthRoot : AuthRoute
    @Serializable data object ModifierMotDePasse : AuthRoute
    @Serializable data object ReinitialiserMotDePasse : AuthRoute
    @Serializable data class VerifierMotDePasse(val email: String = "") : AuthRoute
}
