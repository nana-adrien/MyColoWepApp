package empire.digiprem.mycoloapp.features.auth.config

import empire.digiprem.mycoloapp.features.auth.data.datasource.AdminAuthRemoteDataSource
import empire.digiprem.mycoloapp.features.auth.data.repository.AdminAuthRepository
import empire.digiprem.mycoloapp.features.auth.domain.repository.IAdminAuthRepository
import empire.digiprem.mycoloapp.features.auth.domain.usecase.AdminLoginUseCase
import empire.digiprem.mycoloapp.features.auth.presentation.AdminLoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::AdminAuthRemoteDataSource)
    singleOf(::AdminAuthRepository) bind IAdminAuthRepository::class
    singleOf(::AdminLoginUseCase)
    viewModelOf(::AdminLoginViewModel)
}
