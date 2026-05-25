package empire.digiprem.mycoloapp.features.participants.data.datasource

import empire.digiprem.mycoloapp.core.domain.error.DataError
import empire.digiprem.mycoloapp.core.domain.util.Result
import empire.digiprem.mycoloapp.core.data.safeSupabaseCall
import empire.digiprem.mycoloapp.core.domain.util.onSuccess
import empire.digiprem.mycoloapp.features.participants.data.dto.ParticipantDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

class ParticipantRemoteDataSource(private val client: SupabaseClient) {

    suspend fun getAll(): Result<List<ParticipantDto>, DataError.Remote> =
        safeSupabaseCall { client.from("participants").select().decodeList() }
    // ParticipantRemoteDataSource.kt
    fun observeParticipants(): Flow<Result<List<ParticipantDto>, DataError.Remote>> = flow {

        // 1. Charge la liste initiale
        emit(
            safeSupabaseCall {
                client.from("participants")
                    .select()
                    .decodeList<ParticipantDto>()
            }.onSuccess {
                println(">>> Liste initiale : ${it.size} participants")
            }
        )
       /* val initial =
        emit(initial)*/


        // 2. Crée le channel
        val channel = client.realtime.channel("participants") {}

        // 3. ✅ postgresChangeFlow AVANT subscribe
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(
            schema = "public"
        ) {
            table = "participants"
        }

        // 4. ✅ Subscribe AVANT collect
        channel.subscribe()
        println(">>> Channel souscrit ✅")

        // 5. Collect — gère INSERT + UPDATE + DELETE
        changeFlow.collect { action ->
            when (action) {
                is PostgresAction.Insert -> println(">>> INSERT détecté")
                is PostgresAction.Update -> println(">>> UPDATE détecté")
                is PostgresAction.Delete -> println(">>> DELETE détecté")
                is PostgresAction.Select -> {}
            }
            // Recharge la liste complète
            val updated = client.from("participants")
                .select()
                .decodeList<ParticipantDto>()
            emit(Result.Success(updated))
            println(">>> Liste mise à jour : ${updated.size} participants")
        }

        awaitCancellation()

    }.onCompletion {
        client.realtime.removeAllChannels()
    }
}
