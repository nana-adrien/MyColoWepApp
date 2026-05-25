package empire.digiprem.mycoloapp.features.live.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository

class StopLiveUseCase(private val repository: ILiveRepository) {
    suspend operator fun invoke(sessionId: String): Result<Unit, DataError.Remote> =
        repository.stopLive(sessionId)
}
