package empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.repository

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.feature.admin.dashboard.domain.model.Participant

interface IParticipantRepository {
    suspend fun getAll(): Result<List<Participant>, DataError.Remote>
}
