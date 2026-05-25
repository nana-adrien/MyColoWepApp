package empire.digiprem.mycoloapp.features.registration.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.map
import empire.digiprem.mycoloapp.features.registration.data.datasource.RegistrationRemoteDataSource
import empire.digiprem.mycoloapp.features.registration.data.dto.toInsertDto
import empire.digiprem.mycoloapp.features.registration.domain.model.RegistrationForm
import empire.digiprem.mycoloapp.features.registration.domain.repository.IRegistrationRepository

class RegistrationRepository(
    private val remoteDataSource: RegistrationRemoteDataSource
) : IRegistrationRepository {

    override suspend fun validateSecurityCode(code: String): Result<String, DataError.Remote> =
        remoteDataSource.validateSecurityCode(code)

    override suspend fun register(form: RegistrationForm): Result<String, DataError.Remote> {
        val dto = form.toInsertDto()

        val insertResult = remoteDataSource.insert(dto)


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
