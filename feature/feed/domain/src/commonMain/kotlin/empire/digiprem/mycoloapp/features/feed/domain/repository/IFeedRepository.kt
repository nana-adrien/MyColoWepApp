package empire.digiprem.mycoloapp.features.feed.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.feed.domain.model.Comment
import empire.digiprem.mycoloapp.features.feed.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface IFeedRepository {
    fun observePosts(): Flow<List<Post>>
    suspend fun fetchPosts(): Result<List<Post>, DataError.Remote>
    suspend fun likePost(postId: String): Result<Unit, DataError.Remote>
    suspend fun unlikePost(postId: String): Result<Unit, DataError.Remote>
    suspend fun createPost(caption: String, mediaUrl: String?, mediaType: String?): Result<Post, DataError.Remote>
    suspend fun fetchComments(postId: String): Result<List<Comment>, DataError.Remote>
    suspend fun addComment(postId: String, content: String): Result<Comment, DataError.Remote>
}
