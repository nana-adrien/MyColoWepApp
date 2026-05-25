package empire.digiprem.mycoloapp.features.feed.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.feed.domain.model.Comment
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository

class AddCommentUseCase(private val repository: IFeedRepository) {
    suspend operator fun invoke(postId: String, content: String): Result<Comment, DataError.Remote> {
        if (content.isBlank()) return Result.Failure(DataError.Remote.Unknown)
        return repository.addComment(postId, content.trim())
    }
}
