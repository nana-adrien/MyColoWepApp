package empire.digiprem.mycoloapp.features.feed.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.feed.domain.model.Comment
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository

class GetCommentsUseCase(private val repository: IFeedRepository) {
    suspend operator fun invoke(postId: String): Result<List<Comment>, DataError.Remote> =
        repository.fetchComments(postId)
}
