package empire.digiprem.mycoloapp.features.live.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveSession
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository

class StartLiveUseCase(private val repository: ILiveRepository) {
    suspend operator fun invoke(title: String): Result<LiveSession, DataError.Remote> {
        if (title.isBlank()) return Result.Failure(DataError.Remote.Unknown)
        return repository.startLive(title.trim())
    }
}
