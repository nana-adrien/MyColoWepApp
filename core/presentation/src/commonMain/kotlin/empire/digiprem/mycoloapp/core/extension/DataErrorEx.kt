package empire.digiprem.mycoloapp.core.extension

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.UiText
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.error_bad_request
import mycolowepapp.shared.generated.resources.error_conflict
import mycolowepapp.shared.generated.resources.error_forbidden
import mycolowepapp.shared.generated.resources.error_network
import mycolowepapp.shared.generated.resources.error_not_found
import mycolowepapp.shared.generated.resources.error_read_only
import mycolowepapp.shared.generated.resources.error_request_timeout
import mycolowepapp.shared.generated.resources.error_serialization
import mycolowepapp.shared.generated.resources.error_server
import mycolowepapp.shared.generated.resources.error_unauthorized
import mycolowepapp.shared.generated.resources.error_unknown


// DataError.Remote.toUiText()
fun DataError.Remote.toUiText(): UiText = when (this) {
    DataError.Remote.Unauthorized    -> UiText.Resource(Res.string.error_unauthorized)
    DataError.Remote.Forbidden       -> UiText.Resource(Res.string.error_forbidden)
    DataError.Remote.NotFound        -> UiText.Resource(Res.string.error_not_found)
    DataError.Remote.Conflict        -> UiText.Resource(Res.string.error_conflict)
    DataError.Remote.BadRequest      -> UiText.Resource(Res.string.error_bad_request)
    DataError.Remote.ReadOnly        -> UiText. Resource(Res.string.error_read_only)
    DataError.Remote.Network         -> UiText. Resource(Res.string.error_network)
    DataError.Remote.RequestTimeOut  -> UiText. Resource(Res.string.error_request_timeout)
    DataError.Remote.ServerError     -> UiText. Resource(Res.string.error_server)
    DataError.Remote.Serialization   -> UiText. Resource(Res.string.error_serialization)
    DataError.Remote.Unknown         -> UiText. Resource(Res.string.error_unknown)
    is DataError.Remote.InvalidOperation -> this.errorCode.toUiText()
}
