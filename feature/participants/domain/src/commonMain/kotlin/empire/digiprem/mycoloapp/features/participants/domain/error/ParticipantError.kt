package empire.digiprem.mycoloapp.features.participants.domain.error

import empire.digiprem.mycoloapp.core.domain.util.ResultError

sealed interface ParticipantError : ResultError {
    data object LoadFailed : ParticipantError
    data object Unauthorized : ParticipantError
    data class Unknown(val message: String) : ParticipantError
}
