package empire.digiprem.mycolowepapp.feature.registration.data.repository

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.map
import empire.digiprem.mycolowepapp.core.domain.util.onSuccess
import empire.digiprem.mycolowepapp.feature.registration.data.datasource.RegistrationRemoteDataSource
import empire.digiprem.mycolowepapp.feature.registration.data.dto.toInsertDto
import empire.digiprem.mycolowepapp.feature.registration.domain.model.RegistrationForm
import empire.digiprem.mycolowepapp.feature.registration.domain.repository.IRegistrationRepository

class RegistrationRepository(
    private val remoteDataSource: RegistrationRemoteDataSource
) : IRegistrationRepository {

    override suspend fun validateSecurityCode(code: String): Result<Boolean, DataError.Remote> =
        remoteDataSource.validateSecurityCode(code)

    override suspend fun register(form: RegistrationForm): Result<String, DataError.Remote> {
        val dto = form.toInsertDto(today = currentDateString())

        val insertResult = remoteDataSource.insert(dto)
        insertResult.onSuccess {
            // Incrémenter l'usage du code — erreur non bloquante
            remoteDataSource.incrementCodeUsage(form.securityCode)
        }

        return insertResult.map { generateReferenceNumber() }
    }
}

private fun generateReferenceNumber(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val suffix = (1..5).map { chars.random() }.joinToString("")
    return "#MC-2026-$suffix"
}

private fun currentDateString(): String {
    // Placeholder — remplacer par kotlinx-datetime si disponible dans ce module
    val day = (1..28).random().toString().padStart(2, '0')
    return "2026-05-$day"
}
