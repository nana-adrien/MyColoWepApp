package empire.digiprem.mycolowepapp.core.domain.util

sealed interface Result<out D, out E : ResultError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : ResultError>(val error: E) : Result<Nothing, E>
}

interface ResultError

inline fun <D, E : ResultError> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <D, E : ResultError> Result<D, E>.onFailure(action: (E) -> Unit): Result<D, E> {
    if (this is Result.Failure) action(error)
    return this
}

inline fun <D, E : ResultError, R> Result<D, E>.map(transform: (D) -> R): Result<R, E> =
    when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Failure -> this
    }

inline fun <D, E : ResultError, F : ResultError> Result<D, E>.mapFailure(
    transform: (E) -> F
): Result<D, F> = when (this) {
    is Result.Success -> this
    is Result.Failure -> Result.Failure(transform(error))
}
