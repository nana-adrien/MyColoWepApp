package empire.digiprem.mycoloapp.feature.admin.dashboard.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.repository.IParticipantRepository
import kotlinx.coroutines.flow.Flow

class GetParticipantsUseCase(private val repository: IParticipantRepository) {
    suspend operator fun invoke(): Result<List<Participant>, DataError.Remote> =
        repository.getAll()
}

class ObserveParticipantsUseCase(private val repository: IParticipantRepository) {
    suspend operator fun invoke(): Flow<Result<List<Participant>, DataError.Remote>> = repository.observeParticipants()
}
