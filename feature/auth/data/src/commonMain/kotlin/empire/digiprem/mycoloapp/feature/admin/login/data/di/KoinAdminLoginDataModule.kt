package empire.digiprem.mycoloapp.feature.admin.login.data.di

import empire.digiprem.mycoloapp.feature.admin.login.data.datasource.AdminAuthRemoteDataSource
import empire.digiprem.mycoloapp.feature.admin.login.data.repository.AdminAuthRepository
import empire.digiprem.mycoloapp.feature.admin.login.domain.repository.IAdminAuthRepository
import empire.digiprem.mycoloapp.feature.admin.login.domain.usecase.AdminLoginUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val adminLoginDataModule = module {
    singleOf(::AdminAuthRemoteDataSource)
    singleOf(::AdminAuthRepository) bind IAdminAuthRepository::class
    singleOf(::AdminLoginUseCase)
}
