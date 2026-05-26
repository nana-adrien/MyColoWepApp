package empire.digiprem.mycoloapp.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppUserInFoSerializable(
    public final val id: String?,
    public final val email: String? = null
)