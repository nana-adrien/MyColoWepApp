package empire.digiprem.mycoloapp.core.domain.error

enum class AppErrorCode(val code: String) {
    CODE_REQUIRED("P0001"),
    CODE_NOT_FOUND("P0002"),
    CODE_INACTIVE("P0003"),
    CODE_EXHAUSTED("P0004"),
    CODE_REACTIVATION_DENIED("P0005"),
    INVALID_MAX_USES("P0006"),
    DUPLICATE_PARTICIPANT("P0007"),
    PARTICIPANT_NOT_FOUND_UPDATE("P0008"),
    PARTICIPANT_NOT_FOUND_DELETE("P0009");

    companion object {
        fun fromCode(code: String?): AppErrorCode? =
            entries.firstOrNull { it.code == code }
    }
}