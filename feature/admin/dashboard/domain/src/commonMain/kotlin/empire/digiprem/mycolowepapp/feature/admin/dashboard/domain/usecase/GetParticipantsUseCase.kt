package empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.usecase

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.mapFailure
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.error.ParticipantError
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.repository.IParticipantRepository

class GetParticipantsUseCase(private val repository: IParticipantRepository) {
    suspend operator fun invoke(): Result<List<Participant>, ParticipantError> =
        repository.getAll().mapFailure { dataError ->
            when (dataError) {
                DataError.Remote.Unauthorized -> ParticipantError.Unauthorized
                DataError.Remote.Network -> ParticipantError.LoadFailed
                else -> ParticipantError.Unknown(
                    (dataError as? DataError.Remote.Unknown)?.message ?: "Erreur inconnue"
                )
            }
        }
}
