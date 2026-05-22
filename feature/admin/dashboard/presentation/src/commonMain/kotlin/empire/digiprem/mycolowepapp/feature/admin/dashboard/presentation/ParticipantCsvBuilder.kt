package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.EducationLevel

internal object ParticipantCsvBuilder {

    private const val SEPARATOR = ";"

    fun buildCsv(participants: List<Participant>): String {
        val header = listOf(
            "N°",
            "Nom complet",
            "Prénom de famille",
            "Date de naissance",
            "Âge",
            "Niveau d'étude",
            "Date d'inscription"
        ).joinToString(SEPARATOR)

        val rows = participants.mapIndexed { index, p ->
            val birthFormatted = "%02d/%02d/%04d".format(
                p.birthDate.dayOfMonth, p.birthDate.monthNumber, p.birthDate.year
            )
            listOf(
                (index + 1).toString(),
                p.fullName.escapeCsv(),
                p.familyName.escapeCsv(),
                birthFormatted,
                "${p.age} ans",
                educationLevelLabel(p.educationLevel).escapeCsv(),
                p.registeredAt.escapeCsv()
            ).joinToString(SEPARATOR)
        }

        return (listOf(header) + rows).joinToString("\r\n")
    }

    private fun educationLevelLabel(level: EducationLevel): String = when (level) {
        EducationLevel.KINDERGARTEN  -> "Maternelle"
        EducationLevel.PRIMARY       -> "Primaire"
        EducationLevel.SECONDARY     -> "Collège / Lycée"
        EducationLevel.HIGHER_WORKER -> "Étudiant / Travailleur"
    }

    private fun String.escapeCsv(): String =
        if (contains(SEPARATOR) || contains('"') || contains('\n'))
            "\"${replace("\"", "\"\"")}\""
        else this
}
