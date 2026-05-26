package empire.digiprem.mycoloapp.core.data.mappers

import empire.digiprem.mycoloapp.core.data.dto.AppUserSessionSerializable
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
fun AppUserSession.toSerializable(): AppUserSessionSerializable {
    return AppUserSessionSerializable(
        user = user?.toSerializable(),
        refreshToken = refreshToken,
        accessToken = accessToken,
        expiresIn = expiresIn,
    )
}
fun AppUserSessionSerializable.toDomain(): AppUserSession {
    return AppUserSession(
        user = user?.toDomain(),
        refreshToken = refreshToken,
        accessToken = accessToken,
        expiresIn = expiresIn,
    )
}