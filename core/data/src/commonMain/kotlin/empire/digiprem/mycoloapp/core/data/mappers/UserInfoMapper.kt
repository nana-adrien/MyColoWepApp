package empire.digiprem.mycoloapp.core.data.mappers

import empire.digiprem.mycoloapp.core.domain.model.AppUserInFo
import empire.digiprem.mycoloapp.core.domain.model.AppUserSession
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession

fun UserInfo.toDomain(): AppUserInFo {
    return AppUserInFo(
        id=  id,
        email = email,
    )
}