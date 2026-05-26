package empire.digiprem.mycoloapp.config.di

import empire.digiprem.mycoloapp.core.data.services.AppSessionManagerHandler
import empire.digiprem.mycoloapp.core.data.supabase.createAppSupabaseClient
import empire.digiprem.mycoloapp.core.domain.service.AppSessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.SessionManager
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val coreConfigPlatformModule: Module

val coreConfigModule = module {
    includes(coreConfigPlatformModule)
    singleOf(::AppSessionManagerHandler) bind AppSessionManager::class
    singleOf(::AppSessionManagerHandler) bind SessionManager::class
    single<SupabaseClient> { createAppSupabaseClient(get()) }
}