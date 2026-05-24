package empire.digiprem.mycoloapp.di

import empire.digiprem.mycoloapp.AppViewModel
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import empire.digiprem.mycoloapp.core.domain.validator.EmailValidator
import empire.digiprem.mycoloapp.core.domain.validator.IEmailValidator
import empire.digiprem.mycoloapp.core.domain.validator.IPasswordValidator
import empire.digiprem.mycoloapp.core.domain.validator.PasswordValidator
import empire.digiprem.mycoloapp.core.ui.alert.AlertService
import empire.digiprem.mycoloapp.core.ui.alert.AlertViewModel
import empire.digiprem.mycoloapp.feature.admin.dashboard.config.participantsModule
import empire.digiprem.mycoloapp.feature.admin.login.config.authModule
import empire.digiprem.mycoloapp.feature.admin.security_code.config.securityCodeModule
import empire.digiprem.mycoloapp.feature.pre_auth.config.preAuthModule
import empire.digiprem.mycoloapp.feature.registration.config.registrationModule
import empire.digiprem.mycoloapp.feature.settings.config.settingsModule
import empire.digiprem.mycoloapp.features.feed.config.feedModule
import empire.digiprem.mycoloapp.features.live.config.liveModule
import empire.digiprem.mycoloapp.features.profile.config.profileModule
import empire.digiprem.mycoloapp.supabase.AppSessionManager
import empire.digiprem.mycoloapp.supabase.createAppSupabaseClient
import empire.digiprem.mycoloapp.supabase.createPlatformSessionManager
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val supabaseModule = module {
    single { createAppSupabaseClient() }
    single<AlertSender> { AlertService }
    single<AlertService> { AlertService }
    single<AppSessionManager> { createPlatformSessionManager() }
}

val appModule = module {
    single<IEmailValidator> { EmailValidator() }
    single<IPasswordValidator> { PasswordValidator() }
    viewModelOf(::AlertViewModel)
    viewModelOf(::AppViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            supabaseModule,
            appModule,
            // Feature modules
            authModule,
            registrationModule,
            securityCodeModule,
            participantsModule,
            feedModule,
            liveModule,
            profileModule,
            preAuthModule,
            settingsModule,
        )
    }
}
