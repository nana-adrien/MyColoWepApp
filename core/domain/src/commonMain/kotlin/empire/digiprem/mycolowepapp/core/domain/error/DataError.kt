package empire.digiprem.mycolowepapp.core.domain.error

import empire.digiprem.mycolowepapp.core.domain.util.ResultError

sealed interface DataError : ResultError {
    sealed interface Remote : DataError {
        data object Network : Remote
        data object Unauthorized : Remote
        data object ServerError : Remote
        data object Conflict : Remote           // violation contrainte UNIQUE
        data object NotFound : Remote
        data class Unknown(val message: String = "") : Remote
    }
}
