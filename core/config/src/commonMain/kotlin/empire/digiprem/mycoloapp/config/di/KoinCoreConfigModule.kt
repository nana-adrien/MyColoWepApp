package empire.digiprem.mycoloapp.config.di

import empire.digiprem.mycoloapp.core.data.supabase.createAppSupabaseClient
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import org.koin.core.module.Module
import org.koin.dsl.module

expect val coreConfigPlatformModule : Module

val coreConfigModule = module {
    includes(coreConfigPlatformModule)

    single { createAppSupabaseClient(get()) }
}