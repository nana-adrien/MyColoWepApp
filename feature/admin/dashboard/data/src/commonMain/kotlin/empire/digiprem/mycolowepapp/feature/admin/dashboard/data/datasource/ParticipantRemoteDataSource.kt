package empire.digiprem.mycolowepapp.feature.admin.dashboard.data.datasource

import empire.digiprem.mycolowepapp.core.domain.error.DataError
import empire.digiprem.mycolowepapp.core.domain.util.Result
import empire.digiprem.mycolowepapp.core.domain.util.safeSupabaseCall
import empire.digiprem.mycolowepapp.feature.admin.dashboard.data.dto.ParticipantDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class ParticipantRemoteDataSource(private val client: SupabaseClient) {

    suspend fun getAll(): Result<List<ParticipantDto>, DataError.Remote> =
        safeSupabaseCall { client.from("participants").select().decodeList() }
}
