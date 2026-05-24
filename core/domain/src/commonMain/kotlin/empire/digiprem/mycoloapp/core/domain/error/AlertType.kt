package empire.digiprem.mycoloapp.core.domain.error

import empire.digiprem.mycoloapp.core.domain.util.UiText

sealed interface AlertEvent {
    data class Error(val error: UiText) : AlertEvent
    data class Success(val message: UiText) : AlertEvent
    data class Warning(val message: UiText) : AlertEvent
    data class Info(val message: UiText) : AlertEvent
}