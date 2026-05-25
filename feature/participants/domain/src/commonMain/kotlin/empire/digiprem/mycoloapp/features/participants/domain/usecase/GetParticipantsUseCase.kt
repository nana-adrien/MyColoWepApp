package empire.digiprem.mycoloapp.features.participants.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.participants.domain.model.Participant
import empire.digiprem.mycoloapp.features.participants.domain.repository.IParticipantRepository
import kotlinx.coroutines.flow.Flow

class GetParticipantsUseCase(private val repository: IParticipantRepository) {
    suspend operator fun invoke(): Result<List<Participant>, DataError.Remote> =
        repository.getAll()
}

class ObserveParticipantsUseCase(private val repository: IParticipantRepository) {
    suspend operator fun invoke(): Flow<Result<List<Participant>, DataError.Remote>> = repository.observeParticipants()
}
