package empire.digiprem.mycoloapp.features.feed.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.feed.domain.model.Comment
import empire.digiprem.mycoloapp.features.feed.domain.model.MediaType
import empire.digiprem.mycoloapp.features.feed.domain.model.Post
import empire.digiprem.mycoloapp.features.feed.domain.repository.IFeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class FakeFeedRepository : IFeedRepository {

    private val _posts = MutableStateFlow(fakePostsData())
    private val _comments = mutableMapOf<String, MutableList<Comment>>(
        "post_1" to mutableListOf(
            Comment(id = "cmt_1", postId = "post_1", authorId = "user_sara", authorName = "Sara Kabuya", authorAvatarUrl = null, content = "Superbe journée ! Je partage ce souvenir 🌟", createdAt = Clock.System.now().minus(1.hours)),
            Comment(id = "cmt_2", postId = "post_1", authorId = "user_luc", authorName = "Luc Diamba", authorAvatarUrl = null, content = "Quelle énergie positive ! Merci à l'équipe 🙌", createdAt = Clock.System.now().minus(30.minutes)),
        ),
        "post_5" to mutableListOf(
            Comment(id = "cmt_3", postId = "post_5", authorId = "user_jean", authorName = "Jean-Baptiste M.", authorAvatarUrl = null, content = "Bienvenue parmi nous !", createdAt = Clock.System.now().minus(23.hours)),
        ),
    )

    override fun observePosts(): Flow<List<Post>> = _posts.asStateFlow()

    override suspend fun fetchPosts(): Result<List<Post>, DataError.Remote> =
        Result.Success(_posts.value)

    override suspend fun likePost(postId: String): Result<Unit, DataError.Remote> {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(isLikedByMe = true, likesCount = post.likesCount + 1)
            else post
        }
        return Result.Success(Unit)
    }

    override suspend fun unlikePost(postId: String): Result<Unit, DataError.Remote> {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(isLikedByMe = false, likesCount = (post.likesCount - 1).coerceAtLeast(0))
            else post
        }
        return Result.Success(Unit)
    }

    override suspend fun fetchComments(postId: String): Result<List<Comment>, DataError.Remote> =
        Result.Success(_comments[postId]?.toList() ?: emptyList())

    override suspend fun addComment(postId: String, content: String): Result<Comment, DataError.Remote> {
        val comment = Comment(
            id = "cmt_${Clock.System.now().toEpochMilliseconds()}",
            postId = postId,
            authorId = "user_me",
            authorName = "Moi",
            authorAvatarUrl = null,
            content = content,
            createdAt = Clock.System.now(),
        )
        _comments.getOrPut(postId) { mutableListOf() }.add(comment)
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(commentsCount = post.commentsCount + 1) else post
        }
        return Result.Success(comment)
    }

    override suspend fun createPost(caption: String, mediaUrl: String?, mediaType: String?): Result<Post, DataError.Remote> {
        val newPost = Post(
            id = "post_${Clock.System.now().toEpochMilliseconds()}",
            authorId = "me",
            authorName = "Jean-Baptiste M.",
            authorAvatarUrl = null,
            caption = caption,
            mediaUrl = mediaUrl ?: if (mediaType != null) "fake://media" else null,
            mediaType = when (mediaType?.lowercase()) {
                "image" -> MediaType.IMAGE
                "video" -> MediaType.VIDEO
                else -> null
            },
            likesCount = 0,
            commentsCount = 0,
            isLikedByMe = false,
            createdAt = Clock.System.now(),
        )
        _posts.value = listOf(newPost) + _posts.value
        return Result.Success(newPost)
    }
}

private fun fakePostsData() = listOf(
    Post(
        id = "post_1",
        authorId = "user_jean",
        authorName = "Jean-Baptiste M.",
        authorAvatarUrl = null,
        caption = "Belle journée à la colonie ! 🌟 Les activités du week-end étaient incroyables. Merci à toute l'équipe organisatrice !",
        mediaUrl = "fake://image_1",
        mediaType = MediaType.IMAGE,
        likesCount = 24,
        commentsCount = 8,
        isLikedByMe = true,
        createdAt = Clock.System.now().minus(2.hours),
    ),
    Post(
        id = "post_2",
        authorId = "user_sara",
        authorName = "Sara Kabuya",
        authorAvatarUrl = null,
        caption = "Super moment avec toute l'équipe 🎉 On a réussi à organiser une soirée mémorable !",
        mediaUrl = "fake://video_1",
        mediaType = MediaType.VIDEO,
        likesCount = 12,
        commentsCount = 3,
        isLikedByMe = false,
        createdAt = Clock.System.now().minus(5.hours),
    ),
    Post(
        id = "post_3",
        authorId = "user_luc",
        authorName = "Luc Diamba",
        authorAvatarUrl = null,
        caption = "Soirée mémorable hier soir 🎶 La musique, les rires, les souvenirs... Quelle communauté incroyable !",
        mediaUrl = "fake://image_2",
        mediaType = MediaType.IMAGE,
        likesCount = 7,
        commentsCount = 2,
        isLikedByMe = false,
        createdAt = Clock.System.now().minus(6.hours),
    ),
    Post(
        id = "post_4",
        authorId = "user_paul",
        authorName = "Paul Tshimanga",
        authorAvatarUrl = null,
        caption = "Magnifique coucher de soleil depuis le camp ☀️ Ces moments sont gravés dans ma mémoire pour toujours.",
        mediaUrl = "fake://image_3",
        mediaType = MediaType.IMAGE,
        likesCount = 31,
        commentsCount = 5,
        isLikedByMe = true,
        createdAt = Clock.System.now().minus(8.hours),
    ),
    Post(
        id = "post_5",
        authorId = "user_alice",
        authorName = "Alice Mbuyi",
        authorAvatarUrl = null,
        caption = "Première publication ! Je suis ravie de rejoindre cette belle communauté. À bientôt tout le monde 👋",
        mediaUrl = null,
        mediaType = null,
        likesCount = 18,
        commentsCount = 11,
        isLikedByMe = false,
        createdAt = Clock.System.now().minus(1.days),
    ),
    Post(
        id = "post_6",
        authorId = "user_jean",
        authorName = "Jean-Baptiste M.",
        authorAvatarUrl = null,
        caption = "Rappel : réunion communautaire dimanche à 15h. Tous les membres sont invités ! 📢",
        mediaUrl = null,
        mediaType = null,
        likesCount = 9,
        commentsCount = 4,
        isLikedByMe = false,
        createdAt = Clock.System.now().minus(2.days),
    ),
)
