package empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model

import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Participant(
    val id: String,
    val fullName: String,
    val jobStatus: JobStatus,
    val familyName: String,
    val birthDate: LocalDate,
    val educationLevel: String = "",
    val registeredAt: String,
) {
    val age: Int
        get() {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            var years = today.year - birthDate.year
            if (today.monthNumber < birthDate.monthNumber ||
                (today.monthNumber == birthDate.monthNumber && today.dayOfMonth < birthDate.dayOfMonth)
            ) years--
            return years.coerceAtLeast(0)
        }
}
