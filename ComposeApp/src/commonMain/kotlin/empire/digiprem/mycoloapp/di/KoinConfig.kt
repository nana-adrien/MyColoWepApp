package empire.digiprem.mycoloapp.di

import empire.digiprem.mycoloapp.AppViewModel
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import empire.digiprem.mycoloapp.core.domain.validator.EmailValidator
import empire.digiprem.mycoloapp.core.domain.validator.IEmailValidator
import empire.digiprem.mycoloapp.core.domain.validator.IPasswordValidator
import empire.digiprem.mycoloapp.core.domain.validator.PasswordValidator
import empire.digiprem.mycoloapp.alert.AlertService
import empire.digiprem.mycoloapp.alert.AlertViewModel
import empire.digiprem.mycoloapp.config.di.coreConfigModule
import empire.digiprem.mycoloapp.features.participants.config.participantsModule
import empire.digiprem.mycoloapp.features.security_code.config.securityCodeModule
import empire.digiprem.mycoloapp.feature.pre_auth.config.preAuthModule
import empire.digiprem.mycoloapp.features.registration.config.registrationModule
import empire.digiprem.mycoloapp.features.auth.config.authModule
import empire.digiprem.mycoloapp.features.settings.config.settingsModule
import empire.digiprem.mycoloapp.features.feed.config.feedModule
import empire.digiprem.mycoloapp.features.live.config.liveModule
import empire.digiprem.mycoloapp.features.profile.config.profileModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


expect val appPlatformModule: Module


val appModule = module {
    includes(appPlatformModule)
    single<IEmailValidator> { EmailValidator() }
    single<IPasswordValidator> { PasswordValidator() }
    single<AlertSender> { AlertService }
    single<AlertService> { AlertService }
    viewModelOf(::AlertViewModel)
    viewModelOf(::AppViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            coreConfigModule,
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
