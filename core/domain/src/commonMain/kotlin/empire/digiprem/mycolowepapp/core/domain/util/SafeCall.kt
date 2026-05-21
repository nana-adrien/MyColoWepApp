package empire.digiprem.mycolowepapp.core.domain.util

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeSupabaseCall(call: suspend () -> T): Result<T, DataError.Remote> {
    return try {
        Result.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        val message = e.message.orEmpty()
        val error = when {
            message.contains("23505") ||
            message.contains("unique", ignoreCase = true) ||
            message.contains("duplicate", ignoreCase = true) ||
            message.contains("409") -> DataError.Remote.Conflict

            message.contains("404") ||
            message.contains("not found", ignoreCase = true) -> DataError.Remote.NotFound

            message.contains("401") ||
            message.contains("400") ||
            message.contains("Unauthorized", ignoreCase = true) ||
            message.contains("Invalid login", ignoreCase = true) ||
            message.contains("invalid_grant", ignoreCase = true) -> DataError.Remote.Unauthorized

            message.contains("500") ||
            message.contains("server", ignoreCase = true) -> DataError.Remote.ServerError

            message.contains("network", ignoreCase = true) ||
            message.contains("connect", ignoreCase = true) ||
            message.contains("Failed to fetch", ignoreCase = true) ||
            message.contains("NetworkError", ignoreCase = true) ||
            message.contains("Load failed", ignoreCase = true) -> DataError.Remote.Network

            else -> DataError.Remote.Unknown(message)
        }
        Result.Failure(error)
    }
}
