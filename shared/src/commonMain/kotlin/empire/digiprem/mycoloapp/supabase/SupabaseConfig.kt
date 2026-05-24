package empire.digiprem.mycoloapp.supabase

import empire.digiprem.mycoloapp.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlin.time.Duration.Companion.seconds

/*
internal const val SUPABASE_URL = "https://pncutcctbpnbimxidexx.supabase.co"
internal const val SUPABASE_ANON_KEY = "sb_publishable_MfoCAgtvnsK69mBgd-nr4Q_FF-safIz"
*/

 interface  AppSessionManager:SessionManager {
     fun observeSession(): Flow<UserSession?>
 }
expect fun createPlatformSessionManager(): AppSessionManager

@OptIn(ExperimentalSerializationApi::class)
fun createAppSupabaseClient(): SupabaseClient = createSupabaseClient(
    supabaseUrl = BuildKonfig.SUPABASE_URL,
    supabaseKey =BuildKonfig.SUPABASE_KEY
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
        sessionManager = createPlatformSessionManager()
    }
    install(Realtime){
        reconnectDelay = 7.seconds      // ✅ par défaut — délai entre tentatives
        heartbeatInterval = 15.seconds  // ✅ par défaut — détecte les déconnexions
        connectOnSubscribe = true       // ✅ par défaut — connexion auto au subscribe
        disconnectOnNoSubscriptions = true // ✅ déconnecte si plus de channels
        disconnectOnSessionLoss = true  // ✅ déconnecte si session expirée
    }
    install(Storage)
}
