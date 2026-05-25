package empire.digiprem.mycoloapp.features.live.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.live.domain.model.LiveComment
import empire.digiprem.mycoloapp.features.live.domain.repository.ILiveRepository

class SendLiveCommentUseCase(private val repository: ILiveRepository) {
    suspend operator fun invoke(sessionId: String, content: String): Result<LiveComment, DataError.Remote> {
        if (content.isBlank()) return Result.Failure(DataError.Remote.Unknown)
        return repository.sendComment(sessionId, content.trim())
    }
}
