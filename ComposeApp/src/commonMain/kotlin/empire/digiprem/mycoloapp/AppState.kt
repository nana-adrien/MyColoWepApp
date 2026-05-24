package empire.digiprem.mycoloapp

import io.github.jan.supabase.auth.user.UserSession

data class AppState(
    val isAuthenticated: Boolean = false,
    val isLoadingSession: Boolean = true,
    val userSession: UserSession?=null,
)