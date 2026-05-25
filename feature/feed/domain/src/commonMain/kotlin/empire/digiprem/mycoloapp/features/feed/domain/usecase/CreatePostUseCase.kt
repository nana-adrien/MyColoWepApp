package empire.digiprem.mycoloapp.features.feed.domain.usecase

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.feed.domain.model.Post
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository

class CreatePostUseCase(private val repository: IFeedRepository) {
    suspend operator fun invoke(caption: String, mediaUrl: String? = null, mediaType: String? = null): Result<Post, DataError.Remote> {
        if (caption.isBlank() && mediaUrl == null) return Result.Failure(DataError.Remote.Unknown)
        return repository.createPost(caption, mediaUrl, mediaType)
    }
}
