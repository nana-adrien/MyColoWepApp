package empire.digiprem.mycoloapp.core.domain.enums

// commonMain
enum class MimeType(val value: String, val extensions: List<String> = emptyList()) {
    ALL("*/*"),

    IMAGE_ALL("image/*"),
    IMAGE_JPEG("image/jpeg", listOf("jpg", "jpeg")),
    IMAGE_PNG("image/png", listOf("png")),
    IMAGE_GIF("image/gif", listOf("gif")),
    IMAGE_WEBP("image/webp", listOf("webp")),
    IMAGE_SVG("image/svg+xml", listOf("svg")),

    VIDEO_ALL("video/*"),
    VIDEO_MP4("video/mp4", listOf("mp4")),
    VIDEO_AVI("video/x-msvideo", listOf("avi")),
    VIDEO_MOV("video/quicktime", listOf("mov")),
    VIDEO_MKV("video/x-matroska", listOf("mkv")),

    AUDIO_ALL("audio/*"),
    AUDIO_MP3("audio/mpeg", listOf("mp3")),
    AUDIO_WAV("audio/wav", listOf("wav")),
    AUDIO_OGG("audio/ogg", listOf("ogg")),

    PDF("application/pdf", listOf("pdf")),
    WORD("application/msword", listOf("doc")),
    WORD_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", listOf("docx")),
    EXCEL("application/vnd.ms-excel", listOf("xls")),
    EXCEL_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", listOf("xlsx")),

    TEXT_PLAIN("text/plain", listOf("txt")),
    TEXT_CSV("text/csv", listOf("csv")),
    JSON("application/json", listOf("json")),
    XML("application/xml", listOf("xml")),
    ZIP("application/zip", listOf("zip"));

    companion object {
        fun fromString(mimeType: String): MimeType {
            return MimeType.entries.find { it.value.equals(mimeType, ignoreCase = true) }
                ?: MimeType.ALL  // fallback si non trouvé
        }

        fun fromExtension(extension: String): MimeType=
            entries.find { ext ->
                ext.extensions.any { it.equals(extension, ignoreCase = true) }
            }?: MimeType.ALL
    }
}