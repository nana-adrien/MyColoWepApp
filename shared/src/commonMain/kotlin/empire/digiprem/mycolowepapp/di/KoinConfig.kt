package empire.digiprem.mycolowepapp.di

import empire.digiprem.mycolowepapp.feature.admin.dashboard.presentation.AdminDashboardViewModel
import empire.digiprem.mycolowepapp.feature.admin.login.presentation.AdminLoginViewModel
import empire.digiprem.mycolowepapp.feature.admin.security_code.presentation.SecurityCodeViewModel
import empire.digiprem.mycolowepapp.feature.registration.presentation.RegistrationViewModel
import empire.digiprem.mycolowepapp.supabase.createAppSupabaseClient
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val supabaseModule = module {
    single { createAppSupabaseClient() }
}

val appModule = module {
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::AdminLoginViewModel)
    viewModelOf(::AdminDashboardViewModel)
    viewModelOf(::SecurityCodeViewModel)
}
