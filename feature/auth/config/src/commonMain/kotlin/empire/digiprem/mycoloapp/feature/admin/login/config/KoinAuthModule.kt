package empire.digiprem.mycoloapp.feature.admin.login.config

import empire.digiprem.mycoloapp.feature.admin.login.data.di.adminLoginDataModule
import empire.digiprem.mycoloapp.feature.admin.login.presentation.AdminLoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    includes(adminLoginDataModule)
    viewModelOf(::AdminLoginViewModel)
}
