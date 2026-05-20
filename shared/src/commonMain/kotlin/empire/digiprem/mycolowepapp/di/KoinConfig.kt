package empire.digiprem.mycolowepapp.di

import empire.digiprem.mycolowepapp.AppViewModel
import empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation.AdminDashboardViewModel
import empire.digiprem.mycolowepapp.feature.admin.login.presentation.AdminLoginViewModel
import empire.digiprem.mycolowepapp.feature.admin.security_code.presentation.SecurityCodeViewModel
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationViewModel
import empire.digiprem.mycolowepapp.feature.registration.presentation.form.RegisterFormViewModel
import empire.digiprem.mycolowepapp.supabase.AppSessionManager
import empire.digiprem.mycolowepapp.supabase.createAppSupabaseClient
import empire.digiprem.mycolowepapp.supabase.createPlatformSessionManager
import io.github.jan.supabase.createSupabaseClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val supabaseModule = module {
    single { createAppSupabaseClient() }
    single<AppSessionManager> { createPlatformSessionManager() }
}

val appModule = module {
    viewModelOf(::RegisterFormViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::AdminLoginViewModel)
    viewModelOf(::AdminDashboardViewModel)
    viewModelOf(::SecurityCodeViewModel)
    viewModelOf(::AppViewModel)
}
