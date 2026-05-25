package empire.digiprem.mycoloapp.features.feed.presentation.feed

import empire.digiprem.mycoloapp.features.feed.domain.model.Post
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

internal val defaultPosts: List<Post> = listOf(
    Post(
        id = "default_1",
        authorId = "user_sophie",
        authorName = "Sophie Martin",
        authorAvatarUrl = null,
        caption = "Bienvenue dans la communauté MyColo ! 🎉 Partagez vos aventures de colocation et aidez-vous mutuellement à trouver le colocataire idéal.",
        mediaUrl = null,
        mediaType = null,
        likesCount = 24,
        commentsCount = 8,
        isLikedByMe = false,
        createdAt = Clock.System.now().minus(42.minutes),
    ),
    Post(
        id = "default_2",
        authorId = "user_lucas",
        authorName = "Lucas Bernard",
        authorAvatarUrl = null,
        caption = "🏠 Cherche colocataire sympa pour appartement 3P à Paris 11e. Loyer 650€/mois charges comprises. Disponible dès le 1er octobre !",
        mediaUrl = null,
        mediaType = null,
        likesCount = 12,
        commentsCount = 3,
        isLikedByMe = true,
        createdAt = Clock.System.now().minus(3.hours),
    ),
    Post(
        id = "default_3",
        authorId = "user_amina",
        authorName = "Amina Diallo",
        authorAvatarUrl = null,
        caption = "💡 Tip du jour : créez un tableau de tâches partagé avec vos colocataires. Ça évite 90% des conflits ! On utilise Notion et c'est top.",
        mediaUrl = null,
        mediaType = null,
        likesCount = 47,
        commentsCount = 15,
        isLikedByMe = false,
        createdAt = Clock.System.now().minus(1.days),
    ),
    Post(
        id = "default_4",
        authorId = "user_théo",
        authorName = "Théo Dupont",
        authorAvatarUrl = null,
        caption = "Après 2 ans de colocation à Lyon, je peux dire que la communication est la clé. On a mis en place des réunions rapides le dimanche soir et ça change tout ! 🙌",
        mediaUrl = null,
        mediaType = null,
        likesCount = 33,
        commentsCount = 7,
        isLikedByMe = false,
        createdAt = Clock.System.now().minus(2.days),
    ),
)
