package empire.digiprem.mycoloapp.features.feed.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.feed.data.dto.CommentDto
import empire.digiprem.mycoloapp.features.feed.data.dto.PostDto
import empire.digiprem.mycoloapp.features.feed.data.dto.toDomain
import empire.digiprem.mycoloapp.features.feed.domain.model.Comment
import empire.digiprem.mycoloapp.features.feed.domain.model.Post
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedRepository(
    private val supabaseClient: SupabaseClient,
) : IFeedRepository {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())

    override fun observePosts(): Flow<List<Post>> = _posts.asStateFlow()

    override suspend fun fetchPosts(): Result<List<Post>, DataError.Remote> {
        return safeCall {
            val currentUserId = supabaseClient.auth.currentUserOrNull()?.id ?: ""
            val dtos = supabaseClient.postgrest
                .from("posts")
                .select {
                    order("created_at", Order.DESCENDING)
                    limit(50)
                }
                .decodeList<PostDto>()
            val posts = dtos.map { it.toDomain(currentUserId) }
            _posts.value = posts
            posts
        }
    }

    override suspend fun likePost(postId: String): Result<Unit, DataError.Remote> {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: return Result.Failure(DataError.Remote.Unauthorized)
        return safeCall {
            supabaseClient.postgrest.from("likes").insert(
                mapOf("post_id" to postId, "user_id" to userId)
            )
            Unit
        }
    }

    override suspend fun unlikePost(postId: String): Result<Unit, DataError.Remote> {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: return Result.Failure(DataError.Remote.Unauthorized)
        return safeCall {
            supabaseClient.postgrest.from("likes").delete {
                filter { eq("post_id", postId); eq("user_id", userId) }
            }
            Unit
        }
    }

    override suspend fun createPost(caption: String, mediaUrl: String?, mediaType: String?): Result<Post, DataError.Remote> =
        Result.Failure(DataError.Remote.Unknown)

    override suspend fun fetchComments(postId: String): Result<List<Comment>, DataError.Remote> {
        return safeCall {
            supabaseClient.postgrest
                .from("comments")
                .select { filter { eq("post_id", postId) }; order("created_at", Order.ASCENDING) }
                .decodeList<CommentDto>()
                .map { it.toDomain() }
        }
    }

    override suspend fun addComment(postId: String, content: String): Result<Comment, DataError.Remote> {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: return Result.Failure(DataError.Remote.Unauthorized)
        return safeCall {
            supabaseClient.postgrest
                .from("comments")
                .insert(mapOf("post_id" to postId, "user_id" to userId, "content" to content))
                .decodeSingle<CommentDto>()
                .toDomain()
        }
    }
}

private suspend fun <T> safeCall(block: suspend () -> T): Result<T, DataError.Remote> {
    return try {
        Result.Success(block())
    } catch (e: ClientRequestException) {
        when (e.response.status) {
            HttpStatusCode.Unauthorized -> Result.Failure(DataError.Remote.Unauthorized)
            HttpStatusCode.Forbidden -> Result.Failure(DataError.Remote.Forbidden)
            HttpStatusCode.NotFound -> Result.Failure(DataError.Remote.NotFound)
            else -> Result.Failure(DataError.Remote.Unknown)
        }
    } catch (e: Exception) {
        Result.Failure(DataError.Remote.Unknown)
    }
}
