package empire.digiprem.mycoloapp.core.data.supabase

import empire.digiprem.mycoloapp.core.data.BuildKonfig
import empire.digiprem.mycoloapp.core.data.BuildKonfig.SUPABASE_URL
import empire.digiprem.mycoloapp.core.domain.service.AppSessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlin.time.Duration.Companion.seconds





@OptIn(ExperimentalSerializationApi::class)
fun createAppSupabaseClient(actualSessionManager: SessionManager): SupabaseClient = createSupabaseClient(
    supabaseUrl =SUPABASE_URL,
    supabaseKey = BuildKonfig.SUPABASE_KEY,

) {
    install(Postgrest) {
        serializer = KotlinXSerializer(
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                namingStrategy = JsonNamingStrategy.SnakeCase // ✅
            }
        )
    }
    install(Auth) {
        sessionManager = actualSessionManager
    }
    install(Realtime) {
        reconnectDelay = 7.seconds      // ✅ par défaut — délai entre tentatives
        heartbeatInterval = 15.seconds  // ✅ par défaut — détecte les déconnexions
        connectOnSubscribe = true       // ✅ par défaut — connexion auto au subscribe
        disconnectOnNoSubscriptions = true // ✅ déconnecte si plus de channels
        disconnectOnSessionLoss = true  // ✅ déconnecte si session expirée
    }
    install(Storage)
}
