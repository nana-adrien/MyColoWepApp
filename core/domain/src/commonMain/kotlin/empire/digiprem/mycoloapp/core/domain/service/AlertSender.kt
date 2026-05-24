package empire.digiprem.mycoloapp.core.domain.service

import empire.digiprem.mycoloapp.core.domain.error.AlertEvent

interface AlertSender {
    suspend fun sendAlert(event: AlertEvent)
}