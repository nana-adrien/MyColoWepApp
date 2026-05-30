package empire.digiprem.mycoloapp.navigation.auth

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRoute {
    @Serializable data object AuthRoot : AuthRoute
    @Serializable data object ChangePassword : AuthRoute
    @Serializable data object ResetPassword : AuthRoute
    @Serializable data class VerifyOtp(val email: String = "") : AuthRoute
}
