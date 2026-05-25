package empire.digiprem.mycoloapp.features.feed.presentation.createpost

import empire.digiprem.mycoloapp.core.domain.model.AppFile

sealed interface CreatePostAction {
    data class OnCaptionChange(val caption: String) : CreatePostAction
    data class OnMediaPicked(val file: AppFile) : CreatePostAction
    data object OnRemoveMedia : CreatePostAction
    data object OnPublishClick : CreatePostAction
    data object OnNavigateBack : CreatePostAction
}
