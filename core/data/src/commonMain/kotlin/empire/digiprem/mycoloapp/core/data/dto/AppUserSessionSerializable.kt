package empire.digiprem.mycoloapp.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppUserSessionSerializable(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user:AppUserInFoSerializable?,
)
