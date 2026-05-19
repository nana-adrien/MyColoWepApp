package empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.error

import empire.digiprem.mycolowepapp.core.domain.util.ResultError

sealed interface ParticipantError : ResultError {
    data object LoadFailed : ParticipantError
    data object Unauthorized : ParticipantError
    data class Unknown(val message: String) : ParticipantError
}
