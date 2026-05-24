package empire.digiprem.mycoloapp.core.data.networking.mappers

import empire.digiprem.mycoloapp.core.domain.model.AppUserSession
import io.github.jan.supabase.auth.user.UserSession

fun UserSession.toDomain(): AppUserSession {
    return AppUserSession(
        user = user?.toDomain(),
        refreshToken = refreshToken,
        accessToken = accessToken,
        expiresIn = expiresIn,
    )
}