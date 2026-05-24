package empire.digiprem.mycoloapp.feature.admin.dashboard.domain.model

import empire.digiprem.mycoloapp.feature.registration.domain.model.EducationLevel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class Participant(
    val id: String,
    val fullName: String,
    val educationLevel: EducationLevel,
    val familyName: String,
    val birthDate: LocalDate,
    val registeredAt: String,
) {
    val age: Int
        get() {
            val now = Clock.System.now()
            // Convertir selon le fuseau horaire local
            val timeZone = TimeZone.currentSystemDefault()
            val today = now.toLocalDateTime(timeZone).date
            val userAge: Int = today.year-birthDate.year
            return  if (userAge < 0) 0 else userAge
        }
}