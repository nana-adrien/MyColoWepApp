package empire.digiprem.mycoloapp.core.data

import kotlinx.serialization.Serializable

@Serializable
data class  SuperbaseErrorResponse(
    val error:SupabaseErrorBody
)

@Serializable
data class SupabaseErrorBody(
    val code: String? = null,
    val message: String? = null,
    val details: String? = null,
    val hint: String? = null,
)


