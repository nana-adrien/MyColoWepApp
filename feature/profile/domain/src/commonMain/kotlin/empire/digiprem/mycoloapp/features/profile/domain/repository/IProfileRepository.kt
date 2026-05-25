package empire.digiprem.mycoloapp.features.profile.domain.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.features.profile.domain.model.UserProfile

interface IProfileRepository {
    suspend fun getProfile(userId: String): Result<UserProfile, DataError.Remote>
    suspend fun updateProfile(userId: String, fullName: String, bio: String): Result<Unit, DataError.Remote>
}
