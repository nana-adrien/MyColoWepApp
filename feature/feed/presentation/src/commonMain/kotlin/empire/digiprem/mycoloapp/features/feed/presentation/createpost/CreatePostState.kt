package empire.digiprem.mycoloapp.features.feed.presentation.createpost

import empire.digiprem.mycoloapp.core.domain.model.AppFile

data class CreatePostState(
    val caption: String = "",
    val selectedFile: AppFile? = null,
    val isPublishing: Boolean = false,
    val uploadProgress: Float = 0f,
    val errorMessage: String? = null,
) {
    val selectedMediaType: String? get() = selectedFile?.mimeType?.value?.split("/")?.firstOrNull()
    val isFormValid: Boolean get() = caption.isNotBlank() || selectedFile != null
    val captionLength: Int get() = caption.length
}
