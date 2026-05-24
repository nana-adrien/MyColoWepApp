package empire.digiprem.mycoloapp.core.domain.error

import empire.digiprem.mycoloapp.core.domain.util.ResultError


sealed interface DataError : ResultError {
    sealed interface Remote : DataError {
            // Auth
            data object Unauthorized                   : Remote // 401 — non connecté
            data object Forbidden                      : Remote // 403 — non autorisé

            // Données
            data object NotFound                       : Remote // 404
            data object Conflict                       : Remote // 409
            data object BadRequest                     : Remote // 400
            data object ReadOnly                       : Remote // 405 — lecture seule

            // Réseau
            data object Network                        : Remote
            data object RequestTimeOut                 : Remote

            // Serveur
            data object ServerError                    : Remote
            data object Serialization                  : Remote

            data class InvalidOperation(val errorCode: AppErrorCode) : Remote

            data object Unknown   : Remote

    }
}
