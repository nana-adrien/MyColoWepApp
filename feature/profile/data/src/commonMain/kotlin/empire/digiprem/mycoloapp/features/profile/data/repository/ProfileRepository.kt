package empire.digiprem.mycoloapp.features.profile.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.profile.domain.model.UserProfile
import empire.digiprem.mycoloapp.features.profile.domain.repository.IProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

class ProfileRepository(
    private val supabaseClient: SupabaseClient,
) : IProfileRepository {

    override suspend fun getProfile(userId: String): Result<UserProfile, DataError.Remote> = safeCall {
        val dto = supabaseClient.postgrest.from("users")
            .select { filter { eq("id", userId) } }
            .decodeSingle<UserProfileDto>()
        dto.toDomain()
    }

    override suspend fun updateProfile(
        userId: String,
        fullName: String,
        bio: String,
    ): Result<Unit, DataError.Remote> = safeCall {
        supabaseClient.postgrest.from("users").update(
            mapOf("full_name" to fullName, "bio" to bio)
        ) { filter { eq("id", userId) } }
        Unit
    }
}

@Serializable
private data class UserProfileDto(
    val id: String = "",
    val full_name: String = "",
    val avatar_url: String? = null,
    val bio: String = "",
    val posts_count: Int = 0,
    val likes_received_count: Int = 0,
)

private fun UserProfileDto.toDomain() = UserProfile(
    id = id,
    fullName = full_name,
    avatarUrl = avatar_url,
    bio = bio,
    postsCount = posts_count,
    likesReceivedCount = likes_received_count,
)

private suspend fun <T> safeCall(block: suspend () -> T): Result<T, DataError.Remote> {
    return try {
        Result.Success(block())
    } catch (e: ClientRequestException) {
        when (e.response.status) {
            HttpStatusCode.Unauthorized -> Result.Failure(DataError.Remote.Unauthorized)
            HttpStatusCode.NotFound -> Result.Failure(DataError.Remote.NotFound)
            else -> Result.Failure(DataError.Remote.Unknown)
        }
    } catch (e: Exception) {
        Result.Failure(DataError.Remote.Unknown)
    }
}
