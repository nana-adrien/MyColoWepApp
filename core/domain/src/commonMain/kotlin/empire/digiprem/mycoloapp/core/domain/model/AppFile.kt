package empire.digiprem.mycoloapp.core.domain.model

import empire.digiprem.mycoloapp.core.domain.enums.MimeType

data class AppFile(
    val byteArray: ByteArray?,
    val name: String,
    val mimeType: MimeType,
    val path: String? = null,
) {
    val isImage: Boolean get() = mimeType.value.startsWith("image/")
    val isVideo: Boolean get() = mimeType.value.startsWith("video/")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppFile) return false
        return name == other.name && mimeType == other.mimeType && path == other.path
    }

    override fun hashCode(): Int = 31 * name.hashCode() + mimeType.hashCode()
}
