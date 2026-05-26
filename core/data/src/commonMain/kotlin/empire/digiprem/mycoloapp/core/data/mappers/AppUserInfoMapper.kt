package empire.digiprem.mycoloapp.core.data.mappers

import empire.digiprem.mycoloapp.core.data.dto.AppUserInFoSerializable
import empire.digiprem.mycoloapp.core.domain.model.AppUserInFo
import io.github.jan.supabase.auth.user.UserInfo

fun UserInfo.toDomain(): AppUserInFo {
    return AppUserInFo(
        id=  id,
        email = email,
    )
}

fun AppUserInFo.toSerializable(): AppUserInFoSerializable {
    return AppUserInFoSerializable(
        id=  id,
        email = email,
    )
}
fun AppUserInFoSerializable.toDomain(): AppUserInFo {
    return AppUserInFo(
        id=  id,
        email = email,
    )
}