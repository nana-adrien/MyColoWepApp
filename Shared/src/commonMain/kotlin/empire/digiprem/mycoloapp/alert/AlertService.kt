package empire.digiprem.mycoloapp.alert

import empire.digiprem.mycoloapp.core.domain.error.AlertEvent
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object AlertService: AlertSender {
    private val _eventChannel = Channel<AlertEvent>()
    val events = _eventChannel.receiveAsFlow()

    override suspend fun sendAlert(event: AlertEvent) {
        _eventChannel.send(event)
    }

}