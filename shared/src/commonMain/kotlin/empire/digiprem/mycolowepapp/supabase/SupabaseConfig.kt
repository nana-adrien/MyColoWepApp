package empire.digiprem.mycolowepapp.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

internal const val SUPABASE_URL = "https://pncutcctbpnbimxidexx.supabase.co"
internal const val SUPABASE_ANON_KEY = "sb_publishable_MfoCAgtvnsK69mBgd-nr4Q_FF-safIz"

expect fun createPlatformSessionManager(): SessionManager

fun createAppSupabaseClient(): SupabaseClient = createSupabaseClient(
    supabaseUrl = SUPABASE_URL,
    supabaseKey = SUPABASE_ANON_KEY
) {
    install(Postgrest)
    install(Auth) {
        sessionManager = createPlatformSessionManager()
    }

}
