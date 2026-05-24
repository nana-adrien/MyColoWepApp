package empire.digiprem.mycoloapp.core.extension

import empire.digiprem.mycoloapp.core.domain.error.AppErrorCode
import empire.digiprem.mycoloapp.core.domain.util.UiText
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.error_code_exhausted
import mycolowepapp.shared.generated.resources.error_code_inactive
import mycolowepapp.shared.generated.resources.error_code_not_found
import mycolowepapp.shared.generated.resources.error_code_reactivation_denied
import mycolowepapp.shared.generated.resources.error_code_required
import mycolowepapp.shared.generated.resources.error_duplicate_participant
import mycolowepapp.shared.generated.resources.error_invalid_max_uses
import mycolowepapp.shared.generated.resources.error_participant_not_found_delete
import mycolowepapp.shared.generated.resources.error_participant_not_found_update


// AppErrorCode.toUiText()
fun AppErrorCode.toUiText(): UiText = when (this) {
    AppErrorCode.CODE_REQUIRED              -> UiText. Resource(Res.string.error_code_required)
    AppErrorCode.CODE_NOT_FOUND             -> UiText. Resource(Res.string.error_code_not_found)
    AppErrorCode.CODE_INACTIVE              -> UiText. Resource(Res.string.error_code_inactive)
    AppErrorCode.CODE_EXHAUSTED             -> UiText. Resource(Res.string.error_code_exhausted)
    AppErrorCode.CODE_REACTIVATION_DENIED   -> UiText. Resource(Res.string.error_code_reactivation_denied)
    AppErrorCode.INVALID_MAX_USES           -> UiText. Resource(Res.string.error_invalid_max_uses)
    AppErrorCode.DUPLICATE_PARTICIPANT      -> UiText. Resource(Res.string.error_duplicate_participant)
    AppErrorCode.PARTICIPANT_NOT_FOUND_UPDATE -> UiText. Resource(Res.string.error_participant_not_found_update)
    AppErrorCode.PARTICIPANT_NOT_FOUND_DELETE -> UiText. Resource(Res.string.error_participant_not_found_delete)
}