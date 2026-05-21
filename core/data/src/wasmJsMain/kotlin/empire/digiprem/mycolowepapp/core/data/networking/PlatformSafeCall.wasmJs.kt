package empire.digiprem.mycolowepapp.core.data.networking

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handlerResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>,
): Result<T, DataError.Remote> {
    return try {
        val response = execute()

        if (!response.status.isSuccess()) {
            val errorBody = runCatching {
                Json { ignoreUnknownKeys = true }
                    .decodeFromString<SupabaseErrorBody>(response.bodyAsText())
            }.getOrNull()
            return Result.Failure(mapSupabaseError(errorBody, response.status.value))
        }

        handlerResponse(response)

    } catch (e: SerializationException) {
        Result.Failure(DataError.Remote.Serialization)

    } catch (e: HttpRequestTimeoutException) {
        Result.Failure(DataError.Remote.Network)

    } catch (e: Throwable) {
        coroutineContext.ensureActive()
        val message = e.message.orEmpty()
        when {
            message.contains("Failed to fetch", ignoreCase = true) ||
            message.contains("NetworkError", ignoreCase = true) ||
            message.contains("Load failed", ignoreCase = true) ->
                Result.Failure(DataError.Remote.Network)

            message.contains("timeout", ignoreCase = true) ->
                Result.Failure(DataError.Remote.Network)

            else -> {
                println(">>> platformSafeCall unknown error (WASM): ${e::class} — $message")
                Result.Failure(DataError.Remote.Unknown(message))
            }
        }
    }
}
