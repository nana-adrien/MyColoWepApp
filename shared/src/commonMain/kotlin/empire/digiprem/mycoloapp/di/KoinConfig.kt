package empire.digiprem.mycoloapp.di

import empire.digiprem.mycoloapp.AppViewModel
import empire.digiprem.mycoloapp.core.domain.service.AlertSender
import empire.digiprem.mycoloapp.core.domain.validator.EmailValidator
import empire.digiprem.mycoloapp.core.domain.validator.IEmailValidator
import empire.digiprem.mycoloapp.core.domain.validator.IPasswordValidator
import empire.digiprem.mycoloapp.core.domain.validator.PasswordValidator
import empire.digiprem.mycoloapp.core.ui.alert.AlertService
import empire.digiprem.mycoloapp.core.ui.alert.AlertViewModel
import empire.digiprem.mycoloapp.feature.admin.dashboard.presentation.AdminDashboardViewModel
import empire.digiprem.mycoloapp.feature.admin.login.presentation.AdminLoginViewModel
import empire.digiprem.mycoloapp.feature.admin.security_code.presentation.SecurityCodeViewModel
import empire.digiprem.mycoloapp.feature.registration.presentation.RegistrationViewModel
import empire.digiprem.mycoloapp.feature.registration.presentation.form.RegisterFormViewModel
import empire.digiprem.mycoloapp.supabase.AppSessionManager
import empire.digiprem.mycoloapp.supabase.createAppSupabaseClient
import empire.digiprem.mycoloapp.supabase.createPlatformSessionManager
import org.koin.core.module.dsl.viewModelOf
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
    viewModelOf(::RegisterFormViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::AdminLoginViewModel)
    viewModelOf(::AdminDashboardViewModel)
    viewModelOf(::SecurityCodeViewModel)
    viewModelOf(::AppViewModel)
}
