package empire.digiprem.mycoloapp.core.domain.model

data class AppUserSession(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user:AppUserInFo?,
)