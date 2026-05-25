package empire.digiprem.mycoloapp.features.profile.presentation.profile

import empire.digiprem.mycoloapp.core.domain.util.UiText
import empire.digiprem.mycoloapp.features.profile.domain.model.UserProfile

data class ProfileState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null,
)
