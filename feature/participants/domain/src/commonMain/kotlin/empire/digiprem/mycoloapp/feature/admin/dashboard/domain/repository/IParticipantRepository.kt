package empire.digiprem.mycoloapp.feature.admin.dashboard.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.model.Participant
import kotlinx.coroutines.flow.Flow

interface IParticipantRepository {
    suspend fun getAll(): Result<List<Participant>, DataError.Remote>
    suspend fun observeParticipants(): Flow<Result<List<Participant>, DataError.Remote>>
}
