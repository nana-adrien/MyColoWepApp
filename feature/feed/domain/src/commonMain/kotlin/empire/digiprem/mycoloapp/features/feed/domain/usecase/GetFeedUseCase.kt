package empire.digiprem.mycoloapp.features.feed.domain.usecase

import empire.digiprem.mycoloapp.features.feed.domain.model.Post
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository
import kotlinx.coroutines.flow.Flow

class GetFeedUseCase(private val repository: IFeedRepository) {
    operator fun invoke(): Flow<List<Post>> = repository.observePosts()
}
