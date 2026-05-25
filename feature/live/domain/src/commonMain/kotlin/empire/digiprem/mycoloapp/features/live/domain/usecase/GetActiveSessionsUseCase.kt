package empire.digiprem.mycoloapp.features.live.domain.usecase

import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository
import kotlinx.coroutines.flow.Flow

class GetActiveSessionsUseCase(private val repository: ILiveRepository) {
    operator fun invoke(): Flow<List<LiveSession>> = repository.observeActiveSessions()
}
