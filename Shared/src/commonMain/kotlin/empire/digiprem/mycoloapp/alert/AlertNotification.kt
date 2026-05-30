@file:OptIn(ExperimentalUuidApi::class)

package empire.digiprem.mycoloapp.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import empire.digiprem.mycoloapp.core.domain.error.AlertEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Modèle de notification affiché à l'écran
data class AlertNotification(
    val id: String = Uuid.random().toString(),
    val type: AlertType,
    val message: String,
    val durationMs: Long = 4000L,
)

enum class AlertType { ERROR, SUCCESS, WARNING, INFO }

class AlertViewModel(
    private val alertService: AlertService = AlertService,
) : ViewModel() {
    private val _alerts = MutableStateFlow<List<AlertNotification>>(emptyList())
    val alerts: StateFlow<List<AlertNotification>> = _alerts.asStateFlow()

    init {
        collectEvents()
    }

    private fun collectEvents() {
        viewModelScope.launch {
            alertService.events.collect { event ->
                val notification = event.toNotification()
                _alerts.update { current -> current + notification }

                // Auto-dismiss après durationMs
                launch {
                    delay(notification.durationMs)
                    dismiss(notification.id)
                }
            }
        }
    }

    fun dismiss(id: String) {
        _alerts.update { current -> current.filterNot { it.id == id } }
    }

    private suspend fun AlertEvent.toNotification(): AlertNotification = when (this) {
        is AlertEvent.Error   -> AlertNotification(type = AlertType.ERROR,   message = this.error.asStringAsync())
        is AlertEvent.Success -> AlertNotification(type = AlertType.SUCCESS, message = this.message.asStringAsync())
        is AlertEvent.Warning -> AlertNotification(type = AlertType.WARNING, message = this.message.asStringAsync())
        is AlertEvent.Info    -> AlertNotification(type = AlertType.INFO,    message =this.message.asStringAsync())
    }
}