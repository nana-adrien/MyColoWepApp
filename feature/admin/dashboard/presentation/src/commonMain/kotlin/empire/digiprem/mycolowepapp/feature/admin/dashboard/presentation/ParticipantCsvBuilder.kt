package empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation

import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.ParticipantStatus
import empire.digiprem.mycolowepapp.feature.registration.domain.model.JobStatus

internal object ParticipantCsvBuilder {

    private const val SEPARATOR = ";"

    fun buildCsv(participants: List<Participant>): String {
        val header = listOf(
            "N°",
            "Nom complet",
            "Prénom de famille",
            "Âge",
            "Situation professionnelle",
            "Statut d'inscription",
            "Date d'inscription"
        ).joinToString(SEPARATOR)

        val rows = participants.mapIndexed { index, p ->
            listOf(
                (index + 1).toString(),
                p.fullName.escapeCsv(),
                p.familyName.escapeCsv(),
                "${p.age} ans",
                jobStatusLabel(p.jobStatus).escapeCsv(),
                statusLabel(p.status),
                p.registrationDate.escapeCsv()
            ).joinToString(SEPARATOR)
        }

        return (listOf(header) + rows).joinToString("\r\n")
    }

    private fun jobStatusLabel(status: JobStatus): String = when (status) {
        JobStatus.STUDENT_SCHOOL -> "Élève"
        JobStatus.STUDENT_HIGHER -> "Étudiant"
        JobStatus.WORKER         -> "Travailleur"
        JobStatus.SEEKING_WORK   -> "Sans emploi"
    }

    private fun statusLabel(status: ParticipantStatus): String = when (status) {
        ParticipantStatus.VALIDATED -> "Validé"
        ParticipantStatus.PENDING   -> "En attente"
        ParticipantStatus.REJECTED  -> "Rejeté"
    }

    // RFC 4180 : encadre de guillemets si le champ contient le séparateur ou des guillemets
    private fun String.escapeCsv(): String =
        if (contains(SEPARATOR) || contains('"') || contains('\n'))
            "\"${replace("\"", "\"\"")}\""
        else this
}
