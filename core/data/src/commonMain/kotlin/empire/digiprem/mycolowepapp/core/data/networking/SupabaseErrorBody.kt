package empire.digiprem.mycolowepapp.core.data.networking

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import kotlinx.serialization.Serializable

@Serializable
data class SupabaseErrorBody(
    val code: String? = null,
    val message: String? = null,
    val details: String? = null,
    val hint: String? = null,
)

internal fun mapSupabaseError(body: SupabaseErrorBody?, httpStatus: Int): DataError.Remote {
    return when (body?.code) {
        "23505"      -> DataError.Remote.Conflict       // unique violation
        "23503"      -> DataError.Remote.Conflict       // foreign key violation
        "PGRST116"   -> DataError.Remote.NotFound       // row not found
        "invalid_grant" -> DataError.Remote.Unauthorized
        "email_exists"  -> DataError.Remote.Conflict
        else -> when (httpStatus) {
            400, 401 -> DataError.Remote.Unauthorized
            404      -> DataError.Remote.NotFound
            409      -> DataError.Remote.Conflict
            500, 502, 503 -> DataError.Remote.ServerError
            else     -> DataError.Remote.Unknown(body?.message ?: "HTTP $httpStatus")
        }
    }
}
