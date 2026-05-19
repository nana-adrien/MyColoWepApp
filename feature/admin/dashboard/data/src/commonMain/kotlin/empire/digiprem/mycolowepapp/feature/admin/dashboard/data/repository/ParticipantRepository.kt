package empire.digiprem.mycolowepapp.feature.admin.dashboard.data.repository

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.map
import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.datasource.ParticipantRemoteDataSource
import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.dto.toDomain
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.repository.IParticipantRepository

class ParticipantRepository(
    private val remoteDataSource: ParticipantRemoteDataSource
) : IParticipantRepository {
    override suspend fun getAll(): Result<List<Participant>, DataError.Remote> =
        remoteDataSource.getAll().map { dtos -> dtos.map { it.toDomain() } }
}
