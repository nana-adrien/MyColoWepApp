package empire.digiprem.mycoloapp.features.profile.domain.model

data class UserProfile(
    val id: String,
    val fullName: String,
    val avatarUrl: String?,
    val bio: String,
    val postsCount: Int,
    val likesReceivedCount: Int,
)
