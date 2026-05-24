package empire.digiprem.mycoloapp.feature.admin.dashboard.data.repository

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.domain.util.map
import empire.digiprem.mycoloapp.feature.admin.dashboard.data.datasource.ParticipantRemoteDataSource
import empire.digiprem.mycoloapp.feature.admin.dashboard.data.dto.toDomain
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.model.Participant
import empire.digiprem.mycoloapp.feature.admin.dashboard.domain.repository.IParticipantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParticipantRepository(
    private val remoteDataSource: ParticipantRemoteDataSource
) : IParticipantRepository {
    override suspend fun getAll(): Result<List<Participant>, DataError.Remote> =
        remoteDataSource.getAll().map { dtos -> dtos.map { it.toDomain() } }

    override suspend fun observeParticipants(): Flow<Result<List<Participant>, DataError.Remote>> {
      return  remoteDataSource.observeParticipants()
          .map {flowResult->
              flowResult.map { result->result.map { data->data.toDomain() } }
          }
    }
}
