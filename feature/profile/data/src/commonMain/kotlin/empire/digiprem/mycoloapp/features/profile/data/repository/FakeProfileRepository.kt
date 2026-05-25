package empire.digiprem.mycoloapp.features.profile.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.profile.domain.model.UserProfile
import empire.digiprem.mycoloapp.features.profile.domain.repository.IProfileRepository

class FakeProfileRepository : IProfileRepository {

    private val fakeProfiles = mapOf(
        "me" to UserProfile("me", "Jean-Baptiste M.", null, "Passionné de musique et de sport 🎵⚽", 12, 48),
        "user_jean" to UserProfile("user_jean", "Jean-Baptiste M.", null, "Passionné de musique et de sport 🎵⚽", 12, 48),
        "user_sara" to UserProfile("user_sara", "Sara Kabuya", null, "Étudiante passionnée · My Colo 2026 🎓", 8, 31),
        "user_paul" to UserProfile("user_paul", "Paul Tshimanga", null, "Travailleur et photographe amateur 📸", 15, 72),
        "user_luc" to UserProfile("user_luc", "Luc Diamba", null, "Amateur de musique et de bonne humeur 🎶", 5, 19),
        "user_alice" to UserProfile("user_alice", "Alice Mbuyi", null, "Nouvelle dans la communauté, ravie d'être là ! 👋", 3, 11),
    )

    private val _profiles = fakeProfiles.toMutableMap()

    override suspend fun getProfile(userId: String): Result<UserProfile, DataError.Remote> {
        val profile = _profiles[userId] ?: _profiles["me"]!!
        return Result.Success(profile)
    }

    override suspend fun updateProfile(
        userId: String,
        fullName: String,
        bio: String,
    ): Result<Unit, DataError.Remote> {
        val existing = _profiles[userId] ?: return Result.Failure(DataError.Remote.NotFound)
        _profiles[userId] = existing.copy(fullName = fullName, bio = bio)
        return Result.Success(Unit)
    }
}
